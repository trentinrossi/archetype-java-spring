package com.example.demo.controller;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.enums.TransactionStatus;
import com.example.demo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Transaction operations
 * Provides endpoints for transaction management
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for managing transactions")
@RequestMapping("/api/transactions")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @Operation(summary = "Get all transactions", description = "Retrieve a paginated list of all transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/transactions - Fetching all transactions");
        Page<TransactionResponseDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by card number and transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transaction"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable String cardNumber,
            @PathVariable String transactionId) {
        log.info("GET /api/transactions/{}/{} - Fetching transaction", cardNumber, transactionId);
        return transactionService.getTransactionById(cardNumber, transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create a new transaction", description = "Create a new transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionRequestDto request) {
        log.info("POST /api/transactions - Creating new transaction");
        TransactionResponseDto response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing transaction", description = "Update transaction details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}/{transactionId}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable String cardNumber,
            @PathVariable String transactionId,
            @RequestBody UpdateTransactionRequestDto request) {
        log.info("PUT /api/transactions/{}/{} - Updating transaction", cardNumber, transactionId);
        TransactionResponseDto response = transactionService.updateTransaction(cardNumber, transactionId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a transaction", description = "Delete a transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable String cardNumber,
            @PathVariable String transactionId) {
        log.info("DELETE /api/transactions/{}/{} - Deleting transaction", cardNumber, transactionId);
        transactionService.deleteTransaction(cardNumber, transactionId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Get transactions by card number", description = "Retrieve all transactions for a specific card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid card number"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByCardNumber(
            @PathVariable String cardNumber,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/transactions/card/{} - Fetching transactions for card", cardNumber);
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByCardNumber(cardNumber, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get transactions by card number and date range", 
               description = "Retrieve transactions for a card within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNumber}/date-range")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCardNumberAndDateRange(
            @PathVariable String cardNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("GET /api/transactions/card/{}/date-range?startDate={}&endDate={}", cardNumber, startDate, endDate);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByCardNumberAndDateRange(
                cardNumber, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get transactions by status", description = "Retrieve transactions by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByStatus(
            @PathVariable TransactionStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/transactions/status/{} - Fetching transactions by status", status);
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByStatus(status, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get pending transactions", description = "Retrieve all pending transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/pending")
    public ResponseEntity<Page<TransactionResponseDto>> getPendingTransactions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/transactions/pending - Fetching pending transactions");
        Page<TransactionResponseDto> transactions = transactionService.getPendingTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get disputed transactions", description = "Retrieve all disputed transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/disputed")
    public ResponseEntity<Page<TransactionResponseDto>> getDisputedTransactions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/transactions/disputed - Fetching disputed transactions");
        Page<TransactionResponseDto> transactions = transactionService.getDisputedTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Post a transaction", description = "Change transaction status from PENDING to POSTED")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction posted successfully"),
        @ApiResponse(responseCode = "400", description = "Transaction cannot be posted"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{cardNumber}/{transactionId}/post")
    public ResponseEntity<TransactionResponseDto> postTransaction(
            @PathVariable String cardNumber,
            @PathVariable String transactionId) {
        log.info("POST /api/transactions/{}/{}/post - Posting transaction", cardNumber, transactionId);
        TransactionResponseDto response = transactionService.postTransaction(cardNumber, transactionId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Dispute a transaction", description = "Mark a transaction as disputed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction disputed successfully"),
        @ApiResponse(responseCode = "400", description = "Transaction cannot be disputed"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{cardNumber}/{transactionId}/dispute")
    public ResponseEntity<TransactionResponseDto> disputeTransaction(
            @PathVariable String cardNumber,
            @PathVariable String transactionId,
            @RequestParam String reason) {
        log.info("POST /api/transactions/{}/{}/dispute - Disputing transaction", cardNumber, transactionId);
        TransactionResponseDto response = transactionService.disputeTransaction(cardNumber, transactionId, reason);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Reverse a transaction", description = "Reverse a transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction reversed successfully"),
        @ApiResponse(responseCode = "400", description = "Transaction cannot be reversed"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{cardNumber}/{transactionId}/reverse")
    public ResponseEntity<TransactionResponseDto> reverseTransaction(
            @PathVariable String cardNumber,
            @PathVariable String transactionId,
            @RequestParam String reason) {
        log.info("POST /api/transactions/{}/{}/reverse - Reversing transaction", cardNumber, transactionId);
        TransactionResponseDto response = transactionService.reverseTransaction(cardNumber, transactionId, reason);
        return ResponseEntity.ok(response);
    }
}
