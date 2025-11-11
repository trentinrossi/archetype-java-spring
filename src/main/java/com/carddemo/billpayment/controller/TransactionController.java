package com.carddemo.billpayment.controller;

import com.carddemo.billpayment.dto.CreateTransactionRequestDto;
import com.carddemo.billpayment.dto.TransactionResponseDto;
import com.carddemo.billpayment.dto.UpdateTransactionRequestDto;
import com.carddemo.billpayment.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for managing bill payment transactions")
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

    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by its ID")
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

    @Operation(summary = "Create a new transaction", description = "Create a new transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody CreateTransactionRequestDto request) {
        log.info("Creating new transaction");
        TransactionResponseDto response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing transaction", description = "Update transaction details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {}", id);
        TransactionResponseDto response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete a transaction", description = "Delete a transaction by ID")
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
    
    @Operation(summary = "Get transactions by card number", description = "Retrieve all transactions for a specific card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNum}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByCardNum(
            @PathVariable String cardNum,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving transactions by card number");
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByCardNum(cardNum, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get transactions by account ID", description = "Retrieve all transactions for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByAccountId(
            @PathVariable String accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving transactions by account ID: {}", accountId);
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByAccountId(accountId, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get bill payment transactions", 
               description = "BR006: Retrieve all bill payment transactions (type=02, category=2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of bill payment transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/bill-payments")
    public ResponseEntity<Page<TransactionResponseDto>> getBillPaymentTransactions(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving bill payment transactions");
        Page<TransactionResponseDto> transactions = transactionService.getBillPaymentTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get bill payment transactions by card", 
               description = "Retrieve bill payment transactions for a specific card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of bill payment transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/bill-payments/card/{cardNum}")
    public ResponseEntity<Page<TransactionResponseDto>> getBillPaymentTransactionsByCardNum(
            @PathVariable String cardNum,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving bill payment transactions by card number");
        Page<TransactionResponseDto> transactions = 
                transactionService.getBillPaymentTransactionsByCardNum(cardNum, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get transactions by date range", description = "Retrieve transactions within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid date parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/date-range")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving transactions by date range: {} to {}", startDate, endDate);
        Page<TransactionResponseDto> transactions = 
                transactionService.getTransactionsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get recent transactions", description = "Retrieve recent transactions since a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid date parameter"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/recent")
    public ResponseEntity<Page<TransactionResponseDto>> getRecentTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving recent transactions since: {}", since);
        Page<TransactionResponseDto> transactions = transactionService.getRecentTransactions(since, pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Get transaction statistics by card", 
               description = "Get count and sum of transactions for a specific card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of statistics"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/statistics/card/{cardNum}")
    public ResponseEntity<Map<String, Object>> getTransactionStatisticsByCard(@PathVariable String cardNum) {
        log.info("Retrieving transaction statistics by card number");
        
        long count = transactionService.countTransactionsByCardNum(cardNum);
        BigDecimal sum = transactionService.sumTransactionAmountsByCardNum(cardNum);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("cardNumber", "************" + cardNum.substring(12));
        statistics.put("transactionCount", count);
        statistics.put("totalAmount", sum);
        statistics.put("formattedTotalAmount", String.format("$%.2f", sum));
        
        return ResponseEntity.ok(statistics);
    }
    
    @Operation(summary = "Get bill payment statistics", 
               description = "Get count and sum of all bill payment transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of statistics"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/statistics/bill-payments")
    public ResponseEntity<Map<String, Object>> getBillPaymentStatistics() {
        log.info("Retrieving bill payment statistics");
        
        long count = transactionService.countBillPaymentTransactions();
        BigDecimal sum = transactionService.sumBillPaymentTransactionAmounts();
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("billPaymentCount", count);
        statistics.put("totalBillPaymentAmount", sum);
        statistics.put("formattedTotalAmount", String.format("$%.2f", sum));
        
        return ResponseEntity.ok(statistics);
    }
}
