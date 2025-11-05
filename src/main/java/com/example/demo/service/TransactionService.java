package com.example.demo.service;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.TransactionCategoryBalance;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final AccountRepository accountRepository;
    private final TransactionCategoryBalanceRepository transactionCategoryBalanceRepository;

    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        log.info("Creating new transaction for card number: {}", request.getCardNumber());

        // Validate card number exists in cross-reference (Error Code 100)
        CardCrossReference cardXref = cardCrossReferenceRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> {
                    log.error("Card not found with number: {}", request.getCardNumber());
                    return new IllegalArgumentException("Error Code 100: INVALID CARD NUMBER FOUND");
                });

        // Validate account exists (Error Code 101)
        Account account = accountRepository.findByAccountId(cardXref.getAccountId())
                .orElseThrow(() -> {
                    log.error("Account not found with ID: {}", cardXref.getAccountId());
                    return new IllegalArgumentException("Error Code 101: ACCOUNT RECORD NOT FOUND");
                });

        // Check credit limit (Error Code 102)
        BigDecimal tempBalance = account.getCurrentCycleCredit()
                .subtract(account.getCurrentCycleDebit())
                .add(request.getAmount());
        
        if (tempBalance.compareTo(account.getCreditLimit()) > 0) {
            log.error("Credit limit exceeded for account: {}", account.getAccountId());
            throw new IllegalArgumentException("Error Code 102: OVERLIMIT TRANSACTION");
        }

        // Check account expiration date (Error Code 103)
        LocalDate transactionDate = request.getOriginalTimestamp().toLocalDate();
        if (transactionDate.isAfter(account.getExpirationDate())) {
            log.error("Transaction after account expiration: {}", account.getAccountId());
            throw new IllegalArgumentException("Error Code 103: TRANSACTION RECEIVED AFTER ACCT EXPIRATION");
        }

        // Generate sequential transaction ID
        String transactionId = generateSequentialTransactionId();

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setCardNumber(request.getCardNumber());
        transaction.setTypeCode(request.getTypeCode());
        transaction.setCategoryCode(request.getCategoryCode());
        transaction.setSource(request.getSource());
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setMerchantId(request.getMerchantId());
        transaction.setMerchantName(request.getMerchantName());
        transaction.setMerchantCity(request.getMerchantCity());
        transaction.setMerchantZip(request.getMerchantZip());
        transaction.setOriginalTimestamp(request.getOriginalTimestamp());
        transaction.setProcessingTimestamp(request.getProcessingTimestamp());

        // Update account balances
        account.setCurrentBalance(account.getCurrentBalance().add(request.getAmount()));
        if (request.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
            account.setCurrentCycleCredit(account.getCurrentCycleCredit().add(request.getAmount()));
        } else {
            account.setCurrentCycleDebit(account.getCurrentCycleDebit().add(request.getAmount()));
        }
        accountRepository.save(account);

        // Update transaction category balance
        updateTransactionCategoryBalance(cardXref.getAccountId(), request.getTypeCode(), 
                request.getCategoryCode(), request.getAmount());

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", savedTransaction.getTransactionId());

        return convertToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionById(Long id) {
        log.info("Fetching transaction by ID: {}", id);
        return transactionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionByTransactionId(String transactionId) {
        log.info("Fetching transaction by transaction ID: {}", transactionId);
        return transactionRepository.findByTransactionId(transactionId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber, Pageable pageable) {
        log.info("Fetching transactions for card number: {}", cardNumber);
        List<TransactionResponseDto> transactions = transactionRepository.findByCardNumber(cardNumber)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), transactions.size());
        return new PageImpl<>(transactions.subList(start, end), pageable, transactions.size());
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByDateRange(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.info("Fetching transactions between {} and {}", startDate, endDate);
        List<TransactionResponseDto> transactions = transactionRepository
                .findByProcessingTimestampBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), transactions.size());
        return new PageImpl<>(transactions.subList(start, end), pageable, transactions.size());
    }

    @Transactional
    public void deleteTransaction(Long id) {
        log.info("Deleting transaction with ID: {}", id);
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction not found");
        }
        transactionRepository.deleteById(id);
        log.info("Transaction deleted successfully: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.info("Fetching all transactions with pagination");
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }

    private String generateSequentialTransactionId() {
        Optional<Transaction> lastTransaction = transactionRepository.findTopByOrderByTransactionIdDesc();
        if (lastTransaction.isPresent()) {
            String lastId = lastTransaction.get().getTransactionId();
            try {
                long numericPart = Long.parseLong(lastId.replaceAll("[^0-9]", ""));
                return String.format("TXN%013d", numericPart + 1);
            } catch (NumberFormatException e) {
                return "TXN0000000000001";
            }
        }
        return "TXN0000000000001";
    }

    private void updateTransactionCategoryBalance(Long accountId, String typeCode, 
            String categoryCode, BigDecimal amount) {
        Optional<TransactionCategoryBalance> balanceOpt = transactionCategoryBalanceRepository
                .findByAccountIdAndTypeCodeAndCategoryCode(accountId, typeCode, categoryCode);

        if (balanceOpt.isPresent()) {
            TransactionCategoryBalance balance = balanceOpt.get();
            balance.setBalance(balance.getBalance().add(amount));
            transactionCategoryBalanceRepository.save(balance);
            log.info("Updated category balance for account {} and category {}-{}", 
                    accountId, typeCode, categoryCode);
        } else {
            TransactionCategoryBalance newBalance = new TransactionCategoryBalance();
            newBalance.setAccountId(accountId);
            newBalance.setTypeCode(typeCode);
            newBalance.setCategoryCode(categoryCode);
            newBalance.setBalance(amount);
            transactionCategoryBalanceRepository.save(newBalance);
            log.info("Created category balance for account {} and category {}-{}", 
                    accountId, typeCode, categoryCode);
        }
    }

    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setId(transaction.getId());
        response.setTransactionId(transaction.getTransactionId());
        response.setCardNumber(transaction.getCardNumber());
        response.setTypeCode(transaction.getTypeCode());
        response.setCategoryCode(transaction.getCategoryCode());
        response.setSource(transaction.getSource());
        response.setDescription(transaction.getDescription());
        response.setAmount(transaction.getAmount());
        response.setMerchantId(transaction.getMerchantId());
        response.setMerchantName(transaction.getMerchantName());
        response.setMerchantCity(transaction.getMerchantCity());
        response.setMerchantZip(transaction.getMerchantZip());
        response.setOriginalTimestamp(transaction.getOriginalTimestamp());
        response.setProcessingTimestamp(transaction.getProcessingTimestamp());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
