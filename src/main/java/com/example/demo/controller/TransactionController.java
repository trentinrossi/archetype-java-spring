package com.example.demo.controller;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for managing credit card transactions and bill payments")
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
        log.info("Retrieving all transactions with pagination: {}", pageable);
        Page<TransactionResponseDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by their internal ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transaction"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        log.info("Retrieving transaction by ID: {}", id);
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get transaction by transaction ID", description = "Retrieve a transaction by their unique transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transaction"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-tran-id/{tranId}")
    public ResponseEntity<TransactionResponseDto> getTransactionByTranId(@PathVariable Long tranId) {
        log.info("Retrieving transaction by transaction ID: {}", tranId);
        return transactionService.getTransactionByTranId(tranId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get transactions by account ID", description = "Retrieve all transactions for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid account ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByAccountId(@PathVariable String accountId) {
        log.info("Retrieving transactions for account ID: {}", accountId);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by card number", description = "Retrieve all transactions for a specific card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid card number"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-card/{cardNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCardNumber(@PathVariable String cardNumber) {
        log.info("Retrieving transactions for card number: {}", cardNumber);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByCardNumber(cardNumber);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Create a new transaction", description = "Create a new transaction record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or Tran ID already exist..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody CreateTransactionRequestDto request) {
        log.info("Creating new transaction with transaction ID: {}", request.getTranId());
        TransactionResponseDto response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing transaction", description = "Update transaction details by internal ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @RequestBody UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {}", id);
        TransactionResponseDto response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a transaction", description = "Delete a transaction by internal ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.info("Deleting transaction with ID: {}", id);
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Generate next transaction ID", description = "Generate unique transaction ID by incrementing the highest existing ID (BR005)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction ID generated successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/generate-id")
    public ResponseEntity<Map<String, Object>> generateTransactionId() {
        log.info("Generating next transaction ID");
        Long nextId = transactionService.generateNextTransactionId();
        
        Map<String, Object> response = new HashMap<>();
        response.put("nextTransactionId", nextId);
        response.put("message", "Transaction ID generated successfully");
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create bill payment transaction", description = "Create standardized transaction record for bill payment with full balance payment (BR004, BR006)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Bill payment transaction created successfully and account balance updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or account balance is not positive"),
        @ApiResponse(responseCode = "404", description = "Account or card not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/bill-payment")
    public ResponseEntity<TransactionResponseDto> createBillPaymentTransaction(@RequestBody Map<String, Object> request) {
        String accountId = (String) request.get("accountId");
        String cardNumber = (String) request.get("cardNumber");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        
        log.info("Creating bill payment transaction for account ID: {}", accountId);
        TransactionResponseDto response = transactionService.createBillPaymentTransaction(accountId, cardNumber, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all bill payment transactions", description = "Retrieve all transactions with type code '02' (bill payments)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of bill payment transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/bill-payments")
    public ResponseEntity<Page<TransactionResponseDto>> getBillPaymentTransactions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all bill payment transactions with pagination: {}", pageable);
        Page<TransactionResponseDto> transactions = transactionService.getBillPaymentTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get bill payment transactions by account", description = "Retrieve all bill payment transactions for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of bill payment transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid account ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/bill-payments/by-account/{accountId}")
    public ResponseEntity<Page<TransactionResponseDto>> getBillPaymentTransactionsByAccountId(
            @PathVariable String accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving bill payment transactions for account ID: {}", accountId);
        Page<TransactionResponseDto> transactions = transactionService.getBillPaymentTransactionsByAccountId(accountId, pageable);
        return ResponseEntity.ok(transactions);
    }
}
