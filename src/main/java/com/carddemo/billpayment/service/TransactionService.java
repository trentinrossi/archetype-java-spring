package com.carddemo.billpayment.service;

import com.carddemo.billpayment.dto.CreateTransactionRequestDto;
import com.carddemo.billpayment.dto.TransactionResponseDto;
import com.carddemo.billpayment.dto.UpdateTransactionRequestDto;
import com.carddemo.billpayment.entity.Account;
import com.carddemo.billpayment.entity.Transaction;
import com.carddemo.billpayment.repository.AccountRepository;
import com.carddemo.billpayment.repository.TransactionRepository;
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
        log.info("Creating new transaction for account: {}", request.getAccountId());

        // Validate account exists
        Account account = accountRepository.findByAcctId(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        Transaction transaction = new Transaction();
        transaction.setTranTypeCd(request.getTranTypeCd());
        transaction.setTranCatCd(request.getTranCatCd());
        transaction.setTranSource(request.getTranSource());
        transaction.setTranDesc(request.getTranDesc());
        transaction.setTranAmt(request.getTranAmt());
        transaction.setTranCardNum(request.getTranCardNum());
        transaction.setTranMerchantId(request.getTranMerchantId());
        transaction.setTranMerchantName(request.getTranMerchantName());
        transaction.setTranMerchantCity(request.getTranMerchantCity());
        transaction.setTranMerchantZip(request.getTranMerchantZip());
        transaction.setTranOrigTs(request.getTranOrigTs() != null ? request.getTranOrigTs() : LocalDateTime.now());
        transaction.setTranProcTs(request.getTranProcTs() != null ? request.getTranProcTs() : LocalDateTime.now());
        transaction.setAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", savedTransaction.getTranId());
        return convertToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionById(Long id) {
        log.info("Retrieving transaction by ID: {}", id);
        return transactionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional
    public TransactionResponseDto updateTransaction(Long id, UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        if (request.getTranDesc() != null) {
            transaction.setTranDesc(request.getTranDesc());
        }
        if (request.getTranMerchantName() != null) {
            transaction.setTranMerchantName(request.getTranMerchantName());
        }
        if (request.getTranMerchantCity() != null) {
            transaction.setTranMerchantCity(request.getTranMerchantCity());
        }
        if (request.getTranMerchantZip() != null) {
            transaction.setTranMerchantZip(request.getTranMerchantZip());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction updated successfully with ID: {}", updatedTransaction.getTranId());
        return convertToResponse(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        log.info("Deleting transaction with ID: {}", id);

        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction not found");
        }

        transactionRepository.deleteById(id);
        log.info("Transaction deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.info("Retrieving all transactions with pagination");
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByCardNum(String cardNum, Pageable pageable) {
        log.info("Retrieving transactions by card number: {}", maskCardNumber(cardNum));
        return transactionRepository.findByTranCardNum(cardNum, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByAccountId(String accountId, Pageable pageable) {
        log.info("Retrieving transactions by account ID: {}", accountId);
        return transactionRepository.findByAccountId(accountId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getBillPaymentTransactions(Pageable pageable) {
        log.info("Retrieving bill payment transactions");
        return transactionRepository.findBillPaymentTransactions(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getBillPaymentTransactionsByCardNum(String cardNum, Pageable pageable) {
        log.info("Retrieving bill payment transactions by card number: {}", maskCardNumber(cardNum));
        return transactionRepository.findBillPaymentTransactionsByCardNum(cardNum, pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByDateRange(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.info("Retrieving transactions by date range: {} to {}", startDate, endDate);
        return transactionRepository.findByTranOrigTsBetween(startDate, endDate, pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getRecentTransactions(LocalDateTime since, Pageable pageable) {
        log.info("Retrieving recent transactions since: {}", since);
        return transactionRepository.findRecentTransactions(since, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public long countTransactionsByCardNum(String cardNum) {
        log.info("Counting transactions by card number: {}", maskCardNumber(cardNum));
        return transactionRepository.countByTranCardNum(cardNum);
    }

    @Transactional(readOnly = true)
    public long countBillPaymentTransactions() {
        log.info("Counting bill payment transactions");
        return transactionRepository.countBillPaymentTransactions();
    }

    @Transactional(readOnly = true)
    public BigDecimal sumTransactionAmountsByCardNum(String cardNum) {
        log.info("Summing transaction amounts by card number: {}", maskCardNumber(cardNum));
        return transactionRepository.sumTransactionAmountsByCardNum(cardNum);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumBillPaymentTransactionAmounts() {
        log.info("Summing bill payment transaction amounts");
        return transactionRepository.sumBillPaymentTransactionAmounts();
    }

    /**
     * BR005: Transaction ID Generation - Generates unique sequential transaction ID
     * (Handled automatically by database sequence)
     */
    @Transactional(readOnly = true)
    public Long getNextTransactionId() {
        log.info("Getting next transaction ID");
        Optional<Transaction> lastTransaction = transactionRepository.findTopByOrderByTranIdDesc();
        return lastTransaction.map(t -> t.getTranId() + 1).orElse(1L);
    }

    /**
     * BR006: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes
     */
    @Transactional
    public TransactionResponseDto createBillPaymentTransaction(
            String accountId, String cardNum, BigDecimal paymentAmount) {
        log.info("Creating bill payment transaction for account: {} with amount: {}", 
                 accountId, paymentAmount);

        Account account = accountRepository.findByAcctId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        // Create bill payment transaction with specific attributes
        Transaction transaction = Transaction.createBillPaymentTransaction(paymentAmount, cardNum, account);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Bill payment transaction created successfully with ID: {}", savedTransaction.getTranId());
        
        return convertToResponse(savedTransaction);
    }

    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setTranId(transaction.getTranId());
        response.setTranTypeCd(transaction.getTranTypeCd());
        response.setTranCatCd(transaction.getTranCatCd());
        response.setTranSource(transaction.getTranSource());
        response.setTranDesc(transaction.getTranDesc());
        response.setTranAmt(transaction.getTranAmt());
        response.setTranCardNum(transaction.getTranCardNum());
        response.setMaskedCardNum(maskCardNumber(transaction.getTranCardNum()));
        response.setTranMerchantId(transaction.getTranMerchantId());
        response.setTranMerchantName(transaction.getTranMerchantName());
        response.setTranMerchantCity(transaction.getTranMerchantCity());
        response.setTranMerchantZip(transaction.getTranMerchantZip());
        response.setTranOrigTs(transaction.getTranOrigTs());
        response.setTranProcTs(transaction.getTranProcTs());
        response.setAccountId(transaction.getAccount().getAcctId());
        response.setFormattedAmount(transaction.getFormattedAmount());
        response.setIsBillPayment(transaction.isBillPayment());
        response.setTransactionSummary(transaction.getTransactionSummary());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }

    private String maskCardNumber(String cardNum) {
        if (cardNum == null || cardNum.length() != 16) {
            return "****************";
        }
        return "************" + cardNum.substring(12);
    }
}
