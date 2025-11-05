package com.example.demo.controller;

import com.example.demo.dto.DateRangeRequestDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.dto.TransactionReportDTO;
import com.example.demo.dto.TransactionValidationResultDTO;
import com.example.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    
    private final TransactionService transactionService;
    
    /**
     * Validates a transaction without posting
     * Endpoint: POST /api/transactions/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<TransactionValidationResultDTO> validateTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionValidationResultDTO result = transactionService.validateTransaction(transactionDTO);
        return ResponseEntity.ok(result);
    }
    
    /**
     * Posts a new transaction after validation
     * Endpoint: POST /api/transactions
     */
    @PostMapping
    public ResponseEntity<TransactionDTO> postTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO posted = transactionService.postTransaction(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(posted);
    }
    
    /**
     * Retrieves a transaction by ID
     * Endpoint: GET /api/transactions/{transactionId}
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable String transactionId) {
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }
    
    /**
     * Retrieves transactions by card number
     * Endpoint: GET /api/transactions/card/{cardNumber}
     */
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCardNumber(@PathVariable String cardNumber) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByCardNumber(cardNumber);
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * Retrieves transactions by account ID
     * Endpoint: GET /api/transactions/account/{accountId}
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByAccountId(@PathVariable String accountId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
    
    /**
     * Generates transaction report for date range
     * Endpoint: POST /api/transactions/report
     */
    @PostMapping("/report")
    public ResponseEntity<List<TransactionReportDTO>> generateTransactionReport(@Valid @RequestBody DateRangeRequestDTO dateRange) {
        List<TransactionReportDTO> report = transactionService.generateTransactionReport(dateRange);
        return ResponseEntity.ok(report);
    }
    
    /**
     * Retrieves paginated transactions for date range
     * Endpoint: POST /api/transactions/report/paginated
     */
    @PostMapping("/report/paginated")
    public ResponseEntity<Page<TransactionReportDTO>> getTransactionsByDateRange(
            @Valid @RequestBody DateRangeRequestDTO dateRange,
            Pageable pageable) {
        Page<TransactionReportDTO> transactions = transactionService.getTransactionsByDateRange(dateRange, pageable);
        return ResponseEntity.ok(transactions);
    }
}
