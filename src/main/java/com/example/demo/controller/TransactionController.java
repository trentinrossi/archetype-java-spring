package com.example.demo.controller;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for managing credit card transaction records")
@RequestMapping("/api/transactions")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @Operation(summary = "Get all transactions", description = "Retrieve a paginated list of all transactions in sequential order by composite key")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all transactions with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<TransactionResponseDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transaction by composite key", description = "Retrieve a transaction by card number and transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transaction"),
        @ApiResponse(responseCode = "400", description = "Invalid card number or transaction ID length"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{cardNumber}/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionByCompositeKey(
            @PathVariable String cardNumber,
            @PathVariable String transactionId) {
        log.info("Retrieving transaction for card number: {} and transaction ID: {}", cardNumber, transactionId);
        return transactionService.getTransactionByCompositeKey(cardNumber, transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get transactions by card number", description = "Retrieve all transactions for a specific card number in sequential order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid card number length"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByCardNumber(
            @PathVariable String cardNumber,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving transactions for card number: {}", cardNumber);
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByCardNumber(cardNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Create a new transaction", description = "Create a new credit card transaction record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - card number and transaction ID must be exactly 16 characters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionRequestDto request) {
        log.info("Creating new transaction for card number: {} with transaction ID: {}", 
                request.getCardNumber(), request.getTransactionId());
        TransactionResponseDto response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing transaction", description = "Update transaction details by composite key")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or validation error"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{cardNumber}/{transactionId}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable String cardNumber,
            @PathVariable String transactionId,
            @RequestBody UpdateTransactionRequestDto request) {
        log.info("Updating transaction for card number: {} and transaction ID: {}", cardNumber, transactionId);
        TransactionResponseDto response = transactionService.updateTransaction(cardNumber, transactionId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a transaction", description = "Delete a transaction by composite key")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid card number or transaction ID length"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{cardNumber}/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable String cardNumber,
            @PathVariable String transactionId) {
        log.info("Deleting transaction for card number: {} and transaction ID: {}", cardNumber, transactionId);
        transactionService.deleteTransaction(cardNumber, transactionId);
        return ResponseEntity.noContent().build();
    }
}