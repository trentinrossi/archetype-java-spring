package com.example.demo.service;

import com.example.demo.dto.DateRangeRequestDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.dto.TransactionReportDTO;
import com.example.demo.dto.TransactionValidationResultDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final TransactionCategoryBalanceRepository categoryBalanceRepository;
    
    /**
     * Validates and processes a daily transaction
     * Implements CBTRN01C validation logic
     */
    @Transactional(readOnly = true)
    public TransactionValidationResultDTO validateTransaction(TransactionDTO transactionDTO) {
        TransactionValidationResultDTO result = new TransactionValidationResultDTO();
        result.setTransactionId(transactionDTO.getTransactionId());
        result.setValid(true);
        
        // Validate card number through cross-reference
        CardCrossReference crossRef = cardCrossReferenceRepository.findByCardNumber(transactionDTO.getCardNumber())
                .orElse(null);
        
        if (crossRef == null) {
            result.setValid(false);
            result.setValidationFailureCode(100);
            result.setValidationFailureReason("INVALID CARD NUMBER FOUND");
            return result;
        }
        
        result.setCardNumber(crossRef.getCardNumber());
        result.setAccountId(crossRef.getAccountId());
        result.setCustomerId(crossRef.getCustomerId());
        
        // Validate account exists
        Account account = accountRepository.findByAccountId(crossRef.getAccountId())
                .orElse(null);
        
        if (account == null) {
            result.setValid(false);
            result.setValidationFailureCode(101);
            result.setValidationFailureReason("ACCOUNT RECORD NOT FOUND");
            return result;
        }
        
        // Validate credit limit
        BigDecimal tempBalance = account.getCurrentCycleCredit()
                .subtract(account.getCurrentCycleDebit())
                .add(transactionDTO.getTransactionAmount());
        
        if (tempBalance.compareTo(account.getCreditLimit()) > 0) {
            result.setValid(false);
            result.setValidationFailureCode(102);
            result.setValidationFailureReason("OVERLIMIT TRANSACTION");
            return result;
        }
        
        // Validate account expiration
        LocalDate transactionDate = transactionDTO.getOriginalTimestamp().toLocalDate();
        if (transactionDate.isAfter(account.getExpirationDate())) {
            result.setValid(false);
            result.setValidationFailureCode(103);
            result.setValidationFailureReason("TRANSACTION RECEIVED AFTER ACCT EXPIRATION");
            return result;
        }
        
        return result;
    }
    
    /**
     * Posts a validated transaction and updates account balances
     * Implements CBTRN02C posting logic
     */
    @Transactional
    public TransactionDTO postTransaction(TransactionDTO transactionDTO) {
        // Validate transaction first
        TransactionValidationResultDTO validationResult = validateTransaction(transactionDTO);
        
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("Transaction validation failed: " + validationResult.getValidationFailureReason());
        }
        
        // Generate transaction ID
        String newTransactionId = generateTransactionId();
        transactionDTO.setTransactionId(newTransactionId);
        transactionDTO.setProcessingTimestamp(LocalDateTime.now());
        
        // Create transaction entity
        Transaction transaction = mapToEntity(transactionDTO);
        transaction.setAccountId(validationResult.getAccountId());
        
        // Save transaction
        transaction = transactionRepository.save(transaction);
        
        // Update account balances
        updateAccountBalances(validationResult.getAccountId(), transactionDTO.getTransactionAmount());
        
        // Update category balances
        updateCategoryBalance(validationResult.getAccountId(), 
                transactionDTO.getTransactionTypeCode(), 
                transactionDTO.getTransactionCategoryCode(), 
                transactionDTO.getTransactionAmount());
        
        log.info("Transaction posted successfully: {}", newTransactionId);
        
        return mapToDTO(transaction);
    }
    
    /**
     * Retrieves transaction by ID
     * Implements COTRN01C view functionality
     */
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction ID NOT found: " + transactionId));
        
        return mapToDTO(transaction);
    }
    
    /**
     * Generates transaction detail report for date range
     * Implements CBTRN03C report generation
     */
    @Transactional(readOnly = true)
    public List<TransactionReportDTO> generateTransactionReport(DateRangeRequestDTO dateRange) {
        LocalDateTime startDateTime = dateRange.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = dateRange.getEndDate().atTime(LocalTime.MAX);
        
        List<Transaction> transactions = transactionRepository.findByProcessingTimestampBetween(startDateTime, endDateTime);
        
        return transactions.stream()
                .map(this::mapToReportDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Retrieves paginated transactions for date range
     */
    @Transactional(readOnly = true)
    public Page<TransactionReportDTO> getTransactionsByDateRange(DateRangeRequestDTO dateRange, Pageable pageable) {
        LocalDateTime startDateTime = dateRange.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = dateRange.getEndDate().atTime(LocalTime.MAX);
        
        Page<Transaction> transactions = transactionRepository.findByProcessingTimestampBetweenOrderByAccountId(
                startDateTime, endDateTime, pageable);
        
        return transactions.map(this::mapToReportDTO);
    }
    
    /**
     * Retrieves transactions by card number
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCardNumber(String cardNumber) {
        List<Transaction> transactions = transactionRepository.findByCardNumber(cardNumber);
        return transactions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Retrieves transactions by account ID
     */
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByAccountId(String accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private String generateTransactionId() {
        Transaction lastTransaction = transactionRepository.findTopByOrderByTransactionIdDesc()
                .orElse(null);
        
        if (lastTransaction == null) {
            return String.format("%016d", 1L);
        }
        
        long lastId = Long.parseLong(lastTransaction.getTransactionId());
        return String.format("%016d", lastId + 1);
    }
    
    private void updateAccountBalances(String accountId, BigDecimal amount) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        account.setCurrentBalance(account.getCurrentBalance().add(amount));
        
        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            account.setCurrentCycleCredit(account.getCurrentCycleCredit().add(amount));
        } else {
            account.setCurrentCycleDebit(account.getCurrentCycleDebit().add(amount));
        }
        
        accountRepository.save(account);
    }
    
    private void updateCategoryBalance(String accountId, String typeCode, Integer categoryCode, BigDecimal amount) {
        TransactionCategoryBalance balance = categoryBalanceRepository
                .findByAccountIdAndTransactionTypeCodeAndTransactionCategoryCode(accountId, typeCode, categoryCode)
                .orElse(new TransactionCategoryBalance());
        
        if (balance.getAccountId() == null) {
            balance.setAccountId(accountId);
            balance.setTransactionTypeCode(typeCode);
            balance.setTransactionCategoryCode(categoryCode);
            balance.setCategoryBalance(amount);
        } else {
            balance.setCategoryBalance(balance.getCategoryBalance().add(amount));
        }
        
        categoryBalanceRepository.save(balance);
    }
    
    private Transaction mapToEntity(TransactionDTO dto) {
        Transaction entity = new Transaction();
        entity.setTransactionId(dto.getTransactionId());
        entity.setCardNumber(dto.getCardNumber());
        entity.setAccountId(dto.getAccountId());
        entity.setTransactionTypeCode(dto.getTransactionTypeCode());
        entity.setTransactionCategoryCode(dto.getTransactionCategoryCode());
        entity.setTransactionSource(dto.getTransactionSource());
        entity.setTransactionDescription(dto.getTransactionDescription());
        entity.setTransactionAmount(dto.getTransactionAmount());
        entity.setMerchantId(dto.getMerchantId());
        entity.setMerchantName(dto.getMerchantName());
        entity.setMerchantCity(dto.getMerchantCity());
        entity.setMerchantZip(dto.getMerchantZip());
        entity.setOriginalTimestamp(dto.getOriginalTimestamp());
        entity.setProcessingTimestamp(dto.getProcessingTimestamp());
        return entity;
    }
    
    private TransactionDTO mapToDTO(Transaction entity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(entity.getTransactionId());
        dto.setCardNumber(entity.getCardNumber());
        dto.setAccountId(entity.getAccountId());
        dto.setTransactionTypeCode(entity.getTransactionTypeCode());
        dto.setTransactionCategoryCode(entity.getTransactionCategoryCode());
        dto.setTransactionSource(entity.getTransactionSource());
        dto.setTransactionDescription(entity.getTransactionDescription());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setMerchantId(entity.getMerchantId());
        dto.setMerchantName(entity.getMerchantName());
        dto.setMerchantCity(entity.getMerchantCity());
        dto.setMerchantZip(entity.getMerchantZip());
        dto.setOriginalTimestamp(entity.getOriginalTimestamp());
        dto.setProcessingTimestamp(entity.getProcessingTimestamp());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
    
    private TransactionReportDTO mapToReportDTO(Transaction entity) {
        TransactionReportDTO dto = new TransactionReportDTO();
        dto.setTransactionId(entity.getTransactionId());
        dto.setAccountId(entity.getAccountId());
        dto.setTransactionTypeCode(entity.getTransactionTypeCode());
        dto.setTransactionCategoryCode(entity.getTransactionCategoryCode());
        dto.setTransactionSource(entity.getTransactionSource());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setOriginalTimestamp(entity.getOriginalTimestamp());
        dto.setProcessingTimestamp(entity.getProcessingTimestamp());
        return dto;
    }
}
