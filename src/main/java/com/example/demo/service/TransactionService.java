package com.example.demo.service;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        log.info("Creating transaction for account ID: {}", request.getAccountId());

        // Validate account exists
        Account account = accountRepository.findByAccountId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.getAccountId()));

        Transaction transaction = new Transaction();
        transaction.setTransactionTypeCode(request.getTransactionTypeCode());
        transaction.setTransactionCategoryCode(request.getTransactionCategoryCode());
        transaction.setTransactionSource(request.getTransactionSource());
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setCardNumber(request.getCardNumber());
        transaction.setMerchantId(request.getMerchantId());
        transaction.setMerchantName(request.getMerchantName());
        transaction.setMerchantCity(request.getMerchantCity());
        transaction.setMerchantZip(request.getMerchantZip());
        transaction.setOriginationTimestamp(LocalDateTime.now());
        transaction.setProcessingTimestamp(LocalDateTime.now());
        transaction.setAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", savedTransaction.getTransactionId());
        
        return convertToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionById(Long transactionId) {
        log.info("Retrieving transaction by ID: {}", transactionId);
        return transactionRepository.findById(transactionId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByAccountId(String accountId) {
        log.info("Retrieving all transactions for account ID: {}", accountId);
        return transactionRepository.findByAccount_AccountId(accountId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByAccountId(String accountId, Pageable pageable) {
        log.info("Retrieving transactions for account ID: {} with pagination", accountId);
        return transactionRepository.findByAccount_AccountId(accountId, pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber) {
        log.info("Retrieving all transactions for card number: {}", cardNumber);
        return transactionRepository.findByCardNumber(cardNumber).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * BR006: Get all bill payment transactions (type code 02, category 2)
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getBillPaymentTransactions() {
        log.info("Retrieving all bill payment transactions");
        return transactionRepository.findBillPaymentTransactions().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * BR006: Get bill payment transactions for a specific account
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getBillPaymentTransactionsByAccount(String accountId) {
        log.info("Retrieving bill payment transactions for account ID: {}", accountId);
        return transactionRepository.findBillPaymentTransactionsByAccount(accountId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByTypeCode(String transactionTypeCode) {
        log.info("Retrieving transactions by type code: {}", transactionTypeCode);
        return transactionRepository.findByTransactionTypeCode(transactionTypeCode).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByCategoryCode(Integer transactionCategoryCode) {
        log.info("Retrieving transactions by category code: {}", transactionCategoryCode);
        return transactionRepository.findByTransactionCategoryCode(transactionCategoryCode).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Retrieving transactions between {} and {}", startDate, endDate);
        return transactionRepository.findTransactionsByDateRange(startDate, endDate).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        log.info("Retrieving transactions with amount between {} and {}", minAmount, maxAmount);
        return transactionRepository.findTransactionsByAmountRange(minAmount, maxAmount).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getRecentTransactions(int days) {
        log.info("Retrieving transactions from last {} days", days);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return transactionRepository.findRecentTransactions(since).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByMerchantId(Long merchantId) {
        log.info("Retrieving transactions for merchant ID: {}", merchantId);
        return transactionRepository.findByMerchantId(merchantId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByMerchantName(String merchantName) {
        log.info("Retrieving transactions for merchant name: {}", merchantName);
        return transactionRepository.findByMerchantName(merchantName).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.info("Retrieving all transactions with pagination");
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        log.info("Deleting transaction with ID: {}", transactionId);

        if (!transactionRepository.existsById(transactionId)) {
            throw new IllegalArgumentException("Transaction not found");
        }

        transactionRepository.deleteById(transactionId);
        log.info("Transaction deleted successfully with ID: {}", transactionId);
    }

    @Transactional(readOnly = true)
    public long countTransactionsByAccountId(String accountId) {
        return transactionRepository.countByAccount_AccountId(accountId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumTransactionAmountsByAccount(String accountId) {
        BigDecimal sum = transactionRepository.sumTransactionAmountsByAccount(accountId);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    /**
     * BR005: Get next transaction ID (max + 1)
     */
    @Transactional(readOnly = true)
    public Long getNextTransactionId() {
        Optional<Long> maxId = transactionRepository.findMaxTransactionId();
        return maxId.map(id -> id + 1).orElse(1L);
    }

    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setTransactionId(transaction.getTransactionId());
        response.setTransactionTypeCode(transaction.getTransactionTypeCode());
        response.setTransactionCategoryCode(transaction.getTransactionCategoryCode());
        response.setTransactionSource(transaction.getTransactionSource());
        response.setDescription(transaction.getDescription());
        response.setAmount(transaction.getAmount());
        response.setCardNumber(transaction.getCardNumber());
        response.setMerchantId(transaction.getMerchantId());
        response.setMerchantName(transaction.getMerchantName());
        response.setMerchantCity(transaction.getMerchantCity());
        response.setMerchantZip(transaction.getMerchantZip());
        response.setOriginationTimestamp(transaction.getOriginationTimestamp());
        response.setProcessingTimestamp(transaction.getProcessingTimestamp());
        response.setAccountId(transaction.getAccount().getAccountId());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
