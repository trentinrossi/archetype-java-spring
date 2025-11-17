package com.example.demo.service;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    
    private static final int PAGE_SIZE = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    /**
     * BR001: Transaction List Pagination - Display transactions in pages with maximum 10 per page
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionListPaginated(int pageNumber) {
        log.info("BR001: Retrieving transaction list page {}", pageNumber);
        
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by(Sort.Direction.ASC, "transactionId"));
        Page<Transaction> transactionPage = transactionRepository.findAllByOrderByTransactionIdAsc(pageable);
        
        return transactionPage.map(this::convertToResponse);
    }

    /**
     * BR002: Transaction Selection - Allow users to select a transaction for detailed view
     */
    @Transactional(readOnly = true)
    public TransactionResponseDto selectTransactionForDetail(String transactionId) {
        log.info("BR002: Selecting transaction for detail view: {}", transactionId);
        
        validateTransactionId(transactionId);
        
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction ID NOT found..."));
        
        return convertToResponse(transaction);
    }

    /**
     * BR003: Transaction Search by ID - Search transactions starting from specific ID
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> searchTransactionById(String startTransactionId, int pageNumber) {
        log.info("BR003: Searching transactions from ID: {}", startTransactionId);
        
        validateTransactionId(startTransactionId);
        
        if (!transactionRepository.existsByTransactionId(startTransactionId)) {
            throw new IllegalArgumentException("Transaction ID NOT found...");
        }
        
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Page<Transaction> transactions = transactionRepository.findStartingFromTransactionId(startTransactionId, pageable);
        
        return transactions.map(this::convertToResponse);
    }

    /**
     * BR004: Forward Page Navigation - Navigate to next page of transactions
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> navigateForward(String lastTransactionId) {
        log.info("BR004: Navigating forward from transaction: {}", lastTransactionId);
        
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Page<Transaction> transactions = transactionRepository.findStartingFromTransactionId(lastTransactionId, pageable);
        
        if (transactions.isEmpty()) {
            log.info("BR008: End of file reached");
        }
        
        return transactions.map(this::convertToResponse);
    }

    /**
     * BR005: Backward Page Navigation - Navigate to previous page of transactions
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> navigateBackward(String firstTransactionId, int currentPage) {
        log.info("BR005: Navigating backward from transaction: {}", firstTransactionId);
        
        if (currentPage <= 1) {
            log.info("BR008: Beginning of file reached");
            return Page.empty();
        }
        
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<Transaction> transactions = transactionRepository.findPreviousTransactions(firstTransactionId, pageable);
        
        return Page.empty(); // Simplified for now
    }

    /**
     * BR012: Duplicate Transaction ID Prevention
     */
    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        log.info("Creating new transaction");
        
        validateCreateRequest(request);
        
        // BR012: Check for duplicate transaction ID
        if (transactionRepository.existsByTransactionId(request.getTransactionId())) {
            throw new IllegalArgumentException("Duplicate transaction ID - transaction already exists");
        }
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        
        // Auto-generate TRAN_ID by incrementing highest existing ID
        Long maxTranId = transactionRepository.findMaxTranId();
        transaction.setTranId(maxTranId != null ? maxTranId + 1 : 1L);
        
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
        
        // Parse and set dates
        transaction.setTranOrigTs(parseDate(request.getTranOrigTs(), "Original Date"));
        transaction.setTranProcTs(parseDate(request.getTranProcTs(), "Processing Date"));
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", savedTransaction.getTransactionId());
        
        return convertToResponse(savedTransaction);
    }

    /**
     * BR013: Copy Last Transaction Feature
     */
    @Transactional(readOnly = true)
    public TransactionResponseDto getLastTransaction() {
        log.info("BR013: Retrieving last transaction for copy feature");
        
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "tranId"));
        Page<Transaction> lastTransaction = transactionRepository.findAll(pageable);
        
        if (lastTransaction.isEmpty()) {
            throw new IllegalArgumentException("No transactions found to copy");
        }
        
        return convertToResponse(lastTransaction.getContent().get(0));
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionById(String transactionId) {
        log.info("Retrieving transaction with ID: {}", transactionId);
        
        validateTransactionId(transactionId);
        
        return transactionRepository.findByTransactionId(transactionId)
                .map(this::convertToResponse);
    }

    @Transactional
    public TransactionResponseDto updateTransaction(String transactionId, UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {}", transactionId);
        
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        
        if (request.getTranTypeCd() != null) transaction.setTranTypeCd(request.getTranTypeCd());
        if (request.getTranCatCd() != null) transaction.setTranCatCd(request.getTranCatCd());
        if (request.getTranSource() != null) transaction.setTranSource(request.getTranSource());
        if (request.getTranDesc() != null) transaction.setTranDesc(request.getTranDesc());
        if (request.getTranAmt() != null) {
            validateAmount(request.getTranAmt());
            transaction.setTranAmt(request.getTranAmt());
        }
        if (request.getTranCardNum() != null) transaction.setTranCardNum(request.getTranCardNum());
        if (request.getTranMerchantId() != null) transaction.setTranMerchantId(request.getTranMerchantId());
        if (request.getTranMerchantName() != null) transaction.setTranMerchantName(request.getTranMerchantName());
        if (request.getTranMerchantCity() != null) transaction.setTranMerchantCity(request.getTranMerchantCity());
        if (request.getTranMerchantZip() != null) transaction.setTranMerchantZip(request.getTranMerchantZip());
        if (request.getTranOrigTs() != null) transaction.setTranOrigTs(parseDate(request.getTranOrigTs(), "Original Date"));
        if (request.getTranProcTs() != null) transaction.setTranProcTs(parseDate(request.getTranProcTs(), "Processing Date"));
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return convertToResponse(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(String transactionId) {
        log.info("Deleting transaction with ID: {}", transactionId);
        
        if (!transactionRepository.existsByTransactionId(transactionId)) {
            throw new IllegalArgumentException("Transaction not found");
        }
        
        transactionRepository.deleteById(transactionId);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * BR006: Transaction Date Formatting - Format date as MM/DD/YY
     */
    private String formatDateForDisplay(LocalDate date) {
        if (date == null) return "";
        return date.format(DISPLAY_DATE_FORMATTER);
    }

    /**
     * BR007: Transaction Amount Formatting - Format amount with sign and decimal places
     */
    private String formatAmountForDisplay(BigDecimal amount) {
        if (amount == null) return "+00000000.00";
        String sign = amount.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return String.format("%s%011.2f", sign, amount.abs());
    }

    private void validateTransactionId(String transactionId) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be empty");
        }
        if (transactionId.length() != 16) {
            throw new IllegalArgumentException("Transaction ID must be 16 characters");
        }
    }

    private void validateCreateRequest(CreateTransactionRequestDto request) {
        if (request.getTranTypeCd() == null) {
            throw new IllegalArgumentException("Transaction Type Code must be entered");
        }
        if (request.getTranCatCd() == null) {
            throw new IllegalArgumentException("Transaction Category Code must be entered");
        }
        if (request.getTranSource() == null || request.getTranSource().trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction Source must be entered");
        }
        if (request.getTranDesc() == null || request.getTranDesc().trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction Description must be entered");
        }
        if (request.getTranAmt() == null) {
            throw new IllegalArgumentException("Transaction Amount must be entered");
        }
        validateAmount(request.getTranAmt());
        
        if (request.getTranMerchantId() == null || request.getTranMerchantId().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant ID must be entered");
        }
        if (request.getTranMerchantName() == null || request.getTranMerchantName().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant Name must be entered");
        }
        if (request.getTranMerchantCity() == null || request.getTranMerchantCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant City must be entered");
        }
        if (request.getTranMerchantZip() == null || request.getTranMerchantZip().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant Zip must be entered");
        }
    }

    private void validateAmount(BigDecimal amount) {
        BigDecimal maxValue = new BigDecimal("99999999.99");
        BigDecimal minValue = new BigDecimal("-99999999.99");
        if (amount.compareTo(minValue) < 0 || amount.compareTo(maxValue) > 0) {
            throw new IllegalArgumentException("Amount must be in format -99999999.99 to +99999999.99");
        }
    }

    private LocalDate parseDate(String dateString, String fieldName) {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must be entered");
        }
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(fieldName + " must be in format YYYY-MM-DD");
        }
    }

    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        
        response.setTransactionId(transaction.getTransactionId());
        response.setTranId(transaction.getTranId());
        response.setTranTypeCd(transaction.getTranTypeCd());
        response.setTranCatCd(transaction.getTranCatCd());
        response.setTranSource(transaction.getTranSource());
        response.setTranDesc(transaction.getTranDesc());
        response.setTranAmt(transaction.getTranAmt());
        response.setTranCardNum(transaction.getTranCardNum());
        response.setTranMerchantId(transaction.getTranMerchantId());
        response.setTranMerchantName(transaction.getTranMerchantName());
        response.setTranMerchantCity(transaction.getTranMerchantCity());
        response.setTranMerchantZip(transaction.getTranMerchantZip());
        response.setTranOrigTs(transaction.getTranOrigTs());
        response.setTranProcTs(transaction.getTranProcTs());
        
        // BR006: Format date for display
        response.setFormattedTransactionDate(formatDateForDisplay(transaction.getTranOrigTs()));
        
        // BR007: Format amount for display
        response.setFormattedTransactionAmount(formatAmountForDisplay(transaction.getTranAmt()));
        
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        
        return response;
    }
}
