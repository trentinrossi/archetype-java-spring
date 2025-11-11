package com.example.demo.controller;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.service.TransactionService;
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
import java.util.List;

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
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long transactionId) {
        log.info("Retrieving transaction by ID: {}", transactionId);
        
        return transactionService.getTransactionById(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get transactions by account ID", 
               description = "BR006: Retrieve all transactions for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByAccountId(@PathVariable String accountId) {
        log.info("Retrieving transactions for account ID: {}", accountId);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by account ID (paginated)", 
               description = "BR006: Retrieve transactions for a specific account with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/account/{accountId}/paginated")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByAccountIdPaginated(
            @PathVariable String accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving transactions for account ID: {} with pagination", accountId);
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByAccountId(accountId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by card number", 
               description = "BR006: Retrieve all transactions for a specific card number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCardNumber(@PathVariable String cardNumber) {
        log.info("Retrieving transactions for card number: {}", cardNumber);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByCardNumber(cardNumber);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get bill payment transactions", 
               description = "BR006: Retrieve all bill payment transactions (type code 02, category 2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of bill payment transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/bill-payments")
    public ResponseEntity<List<TransactionResponseDto>> getBillPaymentTransactions() {
        log.info("Retrieving all bill payment transactions");
        List<TransactionResponseDto> transactions = transactionService.getBillPaymentTransactions();
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get bill payment transactions by account", 
               description = "BR006: Retrieve bill payment transactions for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of bill payment transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/bill-payments/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getBillPaymentTransactionsByAccount(
            @PathVariable String accountId) {
        log.info("Retrieving bill payment transactions for account ID: {}", accountId);
        List<TransactionResponseDto> transactions = transactionService.getBillPaymentTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by type code", description = "Retrieve transactions by transaction type code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/type/{transactionTypeCode}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByTypeCode(
            @PathVariable String transactionTypeCode) {
        log.info("Retrieving transactions by type code: {}", transactionTypeCode);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByTypeCode(transactionTypeCode);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by category code", 
               description = "Retrieve transactions by transaction category code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/category/{transactionCategoryCode}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByCategoryCode(
            @PathVariable Integer transactionCategoryCode) {
        log.info("Retrieving transactions by category code: {}", transactionCategoryCode);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByCategoryCode(transactionCategoryCode);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by date range", 
               description = "Retrieve transactions within a specific date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Retrieving transactions between {} and {}", startDate, endDate);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by amount range", 
               description = "Retrieve transactions within a specific amount range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid amount format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/amount-range")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByAmountRange(
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {
        log.info("Retrieving transactions with amount between {} and {}", minAmount, maxAmount);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByAmountRange(minAmount, maxAmount);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get recent transactions", 
               description = "Retrieve transactions from the last N days")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "400", description = "Invalid days parameter"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/recent")
    public ResponseEntity<List<TransactionResponseDto>> getRecentTransactions(
            @RequestParam(defaultValue = "30") int days) {
        log.info("Retrieving transactions from last {} days", days);
        List<TransactionResponseDto> transactions = transactionService.getRecentTransactions(days);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by merchant ID", 
               description = "Retrieve all transactions for a specific merchant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByMerchantId(@PathVariable Long merchantId) {
        log.info("Retrieving transactions for merchant ID: {}", merchantId);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByMerchantId(merchantId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transactions by merchant name", 
               description = "Retrieve all transactions for a specific merchant name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transactions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/merchant-name/{merchantName}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByMerchantName(@PathVariable String merchantName) {
        log.info("Retrieving transactions for merchant name: {}", merchantName);
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByMerchantName(merchantName);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Create a new transaction", 
               description = "BR006: Create a new transaction record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody CreateTransactionRequestDto request) {
        log.info("Creating transaction for account ID: {}", request.getAccountId());
        
        try {
            TransactionResponseDto response = transactionService.createTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a transaction", description = "Delete a transaction by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        log.info("Deleting transaction with ID: {}", transactionId);
        
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Count transactions by account", 
               description = "Count the number of transactions for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count/account/{accountId}")
    public ResponseEntity<Long> countTransactionsByAccountId(@PathVariable String accountId) {
        log.info("Counting transactions for account ID: {}", accountId);
        
        long count = transactionService.countTransactionsByAccountId(accountId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Sum transaction amounts by account", 
               description = "Calculate the total sum of transaction amounts for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sum retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/sum/account/{accountId}")
    public ResponseEntity<BigDecimal> sumTransactionAmountsByAccount(@PathVariable String accountId) {
        log.info("Calculating sum of transaction amounts for account ID: {}", accountId);
        
        BigDecimal sum = transactionService.sumTransactionAmountsByAccount(accountId);
        return ResponseEntity.ok(sum);
    }

    @Operation(summary = "Get next transaction ID", 
               description = "BR005: Get the next available transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Next transaction ID retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/next-id")
    public ResponseEntity<Long> getNextTransactionId() {
        log.info("Retrieving next transaction ID");
        
        Long nextId = transactionService.getNextTransactionId();
        return ResponseEntity.ok(nextId);
    }
}
