package com.example.demo.service;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionStatus;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Transaction operations
 * Implements business logic for transaction management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    /**
     * Create a new transaction
     */
    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        log.info("Creating new transaction with ID: {} for card: {}", 
                request.getTransactionId(), maskCardNumber(request.getCardNumber()));
        
        // Validate card number format (must be exactly 16 characters)
        if (request.getCardNumber().length() != 16) {
            throw new IllegalArgumentException("Card number must be exactly 16 characters");
        }
        
        // Validate transaction ID format (must be exactly 16 characters)
        if (request.getTransactionId().length() != 16) {
            throw new IllegalArgumentException("Transaction ID must be exactly 16 characters");
        }
        
        // Check if transaction already exists
        Transaction.TransactionId txnId = new Transaction.TransactionId(
                request.getCardNumber(), request.getTransactionId());
        if (transactionRepository.existsById(txnId)) {
            throw new IllegalArgumentException("Transaction already exists with ID: " + request.getTransactionId());
        }
        
        // Validate transaction amount
        if (request.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
        
        Transaction transaction = new Transaction();
        transaction.setCardNumber(request.getCardNumber());
        transaction.setTransactionId(request.getTransactionId());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setTransactionAmount(request.getTransactionAmount());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setTransactionTime(request.getTransactionTime() != null ? 
                request.getTransactionTime() : LocalDateTime.now());
        transaction.setMerchantName(request.getMerchantName());
        transaction.setMerchantCategory(request.getMerchantCategory());
        transaction.setMerchantId(request.getMerchantId());
        transaction.setMerchantCity(request.getMerchantCity());
        transaction.setMerchantState(request.getMerchantState());
        transaction.setMerchantCountry(request.getMerchantCountry());
        transaction.setMerchantZip(request.getMerchantZip());
        transaction.setAuthorizationCode(request.getAuthorizationCode());
        transaction.setAuthorizationDate(request.getTransactionDate());
        transaction.setAuthorizationAmount(request.getTransactionAmount());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setCurrencyCode(request.getCurrencyCode() != null ? request.getCurrencyCode() : "USD");
        transaction.setOriginalAmount(request.getOriginalAmount());
        transaction.setOriginalCurrency(request.getOriginalCurrency());
        transaction.setExchangeRate(request.getExchangeRate());
        transaction.setReferenceNumber(request.getReferenceNumber());
        transaction.setDescription(request.getDescription());
        transaction.setIsInternational(request.getIsInternational() != null ? request.getIsInternational() : false);
        transaction.setIsRecurring(request.getIsRecurring() != null ? request.getIsRecurring() : false);
        transaction.setIsDisputed(false);
        transaction.setIsReversed(false);
        transaction.setRewardPointsEarned(calculateRewardPoints(request.getTransactionAmount()));
        transaction.setCashbackAmount(calculateCashback(request.getTransactionAmount()));
        transaction.setFeeAmount(BigDecimal.ZERO);
        transaction.setInterestAmount(BigDecimal.ZERO);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", savedTransaction.getTransactionId());
        
        return convertToResponse(savedTransaction);
    }
    
    /**
     * Get transaction by composite ID
     */
    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionById(String cardNumber, String transactionId) {
        log.debug("Fetching transaction with ID: {} for card: {}", transactionId, maskCardNumber(cardNumber));
        Transaction.TransactionId txnId = new Transaction.TransactionId(cardNumber, transactionId);
        return transactionRepository.findById(txnId).map(this::convertToResponse);
    }
    
    /**
     * Update transaction
     */
    @Transactional
    public TransactionResponseDto updateTransaction(String cardNumber, String transactionId, 
                                                   UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {} for card: {}", transactionId, maskCardNumber(cardNumber));
        
        Transaction.TransactionId txnId = new Transaction.TransactionId(cardNumber, transactionId);
        Transaction transaction = transactionRepository.findById(txnId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        
        // Update only non-null fields
        if (request.getTransactionStatus() != null) transaction.setTransactionStatus(request.getTransactionStatus());
        if (request.getPostDate() != null) transaction.setPostDate(request.getPostDate());
        if (request.getDescription() != null) transaction.setDescription(request.getDescription());
        if (request.getIsDisputed() != null) transaction.setIsDisputed(request.getIsDisputed());
        if (request.getDisputeDate() != null) transaction.setDisputeDate(request.getDisputeDate());
        if (request.getDisputeReason() != null) transaction.setDisputeReason(request.getDisputeReason());
        if (request.getIsReversed() != null) transaction.setIsReversed(request.getIsReversed());
        if (request.getReversalDate() != null) transaction.setReversalDate(request.getReversalDate());
        if (request.getReversalReason() != null) transaction.setReversalReason(request.getReversalReason());
        if (request.getRewardPointsEarned() != null) transaction.setRewardPointsEarned(request.getRewardPointsEarned());
        if (request.getCashbackAmount() != null) transaction.setCashbackAmount(request.getCashbackAmount());
        if (request.getFeeAmount() != null) transaction.setFeeAmount(request.getFeeAmount());
        if (request.getInterestAmount() != null) transaction.setInterestAmount(request.getInterestAmount());
        if (request.getStatementDate() != null) transaction.setStatementDate(request.getStatementDate());
        if (request.getBillingCycle() != null) transaction.setBillingCycle(request.getBillingCycle());
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction updated successfully with ID: {}", updatedTransaction.getTransactionId());
        
        return convertToResponse(updatedTransaction);
    }
    
    /**
     * Delete transaction
     */
    @Transactional
    public void deleteTransaction(String cardNumber, String transactionId) {
        log.info("Deleting transaction with ID: {} for card: {}", transactionId, maskCardNumber(cardNumber));
        
        Transaction.TransactionId txnId = new Transaction.TransactionId(cardNumber, transactionId);
        if (!transactionRepository.existsById(txnId)) {
            throw new IllegalArgumentException("Transaction not found");
        }
        
        transactionRepository.deleteById(txnId);
        log.info("Transaction deleted successfully with ID: {}", transactionId);
    }
    
    /**
     * Get all transactions with pagination
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.debug("Fetching all transactions with pagination");
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get transactions by card number
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber, Pageable pageable) {
        log.debug("Fetching transactions for card: {}", maskCardNumber(cardNumber));
        return transactionRepository.findByCardNumber(cardNumber, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get transactions by card number and date range
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByCardNumberAndDateRange(
            String cardNumber, LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching transactions for card: {} between {} and {}", 
                maskCardNumber(cardNumber), startDate, endDate);
        return transactionRepository.findByCardNumberAndDateRange(cardNumber, startDate, endDate)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get transactions by status
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByStatus(TransactionStatus status, Pageable pageable) {
        log.debug("Fetching transactions with status: {}", status);
        return transactionRepository.findByTransactionStatus(status, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get pending transactions
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getPendingTransactions(Pageable pageable) {
        log.debug("Fetching pending transactions");
        return transactionRepository.findPendingTransactions(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get disputed transactions
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getDisputedTransactions(Pageable pageable) {
        log.debug("Fetching disputed transactions");
        return transactionRepository.findByIsDisputedTrue(pageable).map(this::convertToResponse);
    }
    
    /**
     * Post a transaction (change status from PENDING to POSTED)
     */
    @Transactional
    public TransactionResponseDto postTransaction(String cardNumber, String transactionId) {
        log.info("Posting transaction with ID: {} for card: {}", transactionId, maskCardNumber(cardNumber));
        
        Transaction.TransactionId txnId = new Transaction.TransactionId(cardNumber, transactionId);
        Transaction transaction = transactionRepository.findById(txnId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        
        if (!transaction.isPending()) {
            throw new IllegalArgumentException("Only pending transactions can be posted");
        }
        
        transaction.setTransactionStatus(TransactionStatus.POSTED);
        transaction.setPostDate(LocalDate.now());
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction posted successfully with ID: {}", transactionId);
        
        return convertToResponse(updatedTransaction);
    }
    
    /**
     * Dispute a transaction
     */
    @Transactional
    public TransactionResponseDto disputeTransaction(String cardNumber, String transactionId, String reason) {
        log.info("Disputing transaction with ID: {} for card: {}", transactionId, maskCardNumber(cardNumber));
        
        Transaction.TransactionId txnId = new Transaction.TransactionId(cardNumber, transactionId);
        Transaction transaction = transactionRepository.findById(txnId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        
        if (!transaction.canBeDisputed()) {
            throw new IllegalArgumentException("Transaction cannot be disputed");
        }
        
        transaction.setIsDisputed(true);
        transaction.setDisputeDate(LocalDate.now());
        transaction.setDisputeReason(reason);
        transaction.setTransactionStatus(TransactionStatus.DISPUTED);
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction disputed successfully with ID: {}", transactionId);
        
        return convertToResponse(updatedTransaction);
    }
    
    /**
     * Reverse a transaction
     */
    @Transactional
    public TransactionResponseDto reverseTransaction(String cardNumber, String transactionId, String reason) {
        log.info("Reversing transaction with ID: {} for card: {}", transactionId, maskCardNumber(cardNumber));
        
        Transaction.TransactionId txnId = new Transaction.TransactionId(cardNumber, transactionId);
        Transaction transaction = transactionRepository.findById(txnId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        
        if (!transaction.canBeReversed()) {
            throw new IllegalArgumentException("Transaction cannot be reversed");
        }
        
        transaction.setIsReversed(true);
        transaction.setReversalDate(LocalDate.now());
        transaction.setReversalReason(reason);
        transaction.setTransactionStatus(TransactionStatus.REVERSED);
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction reversed successfully with ID: {}", transactionId);
        
        return convertToResponse(updatedTransaction);
    }
    
    /**
     * Calculate reward points based on transaction amount
     */
    private Integer calculateRewardPoints(BigDecimal amount) {
        // 1 point per dollar spent
        return amount.intValue();
    }
    
    /**
     * Calculate cashback based on transaction amount
     */
    private BigDecimal calculateCashback(BigDecimal amount) {
        // 1% cashback
        return amount.multiply(new BigDecimal("0.01")).setScale(2, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Mask card number for security
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "************";
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }
    
    /**
     * Convert Transaction entity to TransactionResponseDto
     */
    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setCardNumber(maskCardNumber(transaction.getCardNumber()));
        response.setTransactionId(transaction.getTransactionId());
        response.setTransactionType(transaction.getTransactionType());
        response.setTransactionAmount(transaction.getTransactionAmount());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setTransactionTime(transaction.getTransactionTime());
        response.setPostDate(transaction.getPostDate());
        response.setMerchantName(transaction.getMerchantName());
        response.setMerchantCategory(transaction.getMerchantCategory());
        response.setMerchantId(transaction.getMerchantId());
        response.setMerchantCity(transaction.getMerchantCity());
        response.setMerchantState(transaction.getMerchantState());
        response.setMerchantCountry(transaction.getMerchantCountry());
        response.setMerchantZip(transaction.getMerchantZip());
        response.setAuthorizationCode(transaction.getAuthorizationCode());
        response.setAuthorizationDate(transaction.getAuthorizationDate());
        response.setAuthorizationAmount(transaction.getAuthorizationAmount());
        response.setTransactionStatus(transaction.getTransactionStatus());
        response.setTransactionStatusDisplayName(transaction.getTransactionStatus().getDisplayName());
        response.setCurrencyCode(transaction.getCurrencyCode());
        response.setOriginalAmount(transaction.getOriginalAmount());
        response.setOriginalCurrency(transaction.getOriginalCurrency());
        response.setExchangeRate(transaction.getExchangeRate());
        response.setReferenceNumber(transaction.getReferenceNumber());
        response.setDescription(transaction.getDescription());
        response.setIsInternational(transaction.getIsInternational());
        response.setIsRecurring(transaction.getIsRecurring());
        response.setIsDisputed(transaction.getIsDisputed());
        response.setDisputeDate(transaction.getDisputeDate());
        response.setDisputeReason(transaction.getDisputeReason());
        response.setIsReversed(transaction.getIsReversed());
        response.setReversalDate(transaction.getReversalDate());
        response.setReversalReason(transaction.getReversalReason());
        response.setRewardPointsEarned(transaction.getRewardPointsEarned());
        response.setCashbackAmount(transaction.getCashbackAmount());
        response.setFeeAmount(transaction.getFeeAmount());
        response.setInterestAmount(transaction.getInterestAmount());
        response.setTotalAmount(transaction.getTotalAmount());
        response.setStatementDate(transaction.getStatementDate());
        response.setBillingCycle(transaction.getBillingCycle());
        response.setIsPending(transaction.isPending());
        response.setIsPosted(transaction.isPosted());
        response.setCanBeDisputed(transaction.canBeDisputed());
        response.setCanBeReversed(transaction.canBeReversed());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
