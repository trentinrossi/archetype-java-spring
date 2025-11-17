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
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for managing credit card transactions")
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
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TransactionResponseDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transaction"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable String id) {
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
            @PathVariable String id,
            @Valid @RequestBody UpdateTransactionRequestDto request) {
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
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get paginated transaction list", description = "BR001: Display transactions in pages with maximum 10 per page")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of transaction page"),
        @ApiResponse(responseCode = "400", description = "Invalid page number"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionPage(
            @PathVariable int pageNumber) {
        Page<TransactionResponseDto> transactions = transactionService.getTransactionListPaginated(pageNumber);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Select transaction for detail view", description = "BR002: Select a transaction for detailed view")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction selected successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/select/{transactionId}")
    public ResponseEntity<TransactionResponseDto> selectTransaction(
            @PathVariable String transactionId) {
        TransactionResponseDto response = transactionService.selectTransactionForDetail(transactionId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search transactions by ID", description = "BR003: Search transactions starting from specific ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful search"),
        @ApiResponse(responseCode = "404", description = "Transaction not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search/{startTransactionId}")
    public ResponseEntity<Page<TransactionResponseDto>> searchTransactionById(
            @PathVariable String startTransactionId,
            @RequestParam(defaultValue = "1") int pageNumber) {
        Page<TransactionResponseDto> transactions = transactionService.searchTransactionById(startTransactionId, pageNumber);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Navigate forward", description = "BR004: Navigate to next page of transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful navigation"),
        @ApiResponse(responseCode = "404", description = "End of file reached"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/navigate/forward/{lastTransactionId}")
    public ResponseEntity<Page<TransactionResponseDto>> navigateForward(
            @PathVariable String lastTransactionId) {
        Page<TransactionResponseDto> transactions = transactionService.navigateForward(lastTransactionId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Navigate backward", description = "BR005: Navigate to previous page of transactions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful navigation"),
        @ApiResponse(responseCode = "404", description = "Beginning of file reached"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/navigate/backward/{firstTransactionId}")
    public ResponseEntity<Page<TransactionResponseDto>> navigateBackward(
            @PathVariable String firstTransactionId,
            @RequestParam int currentPage) {
        Page<TransactionResponseDto> transactions = transactionService.navigateBackward(firstTransactionId, currentPage);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Copy last transaction", description = "BR013: Get last transaction for copy feature")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Last transaction retrieved"),
        @ApiResponse(responseCode = "404", description = "No transactions found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/copy-last")
    public ResponseEntity<TransactionResponseDto> copyLastTransaction() {
        TransactionResponseDto response = transactionService.getLastTransaction();
        return ResponseEntity.ok(response);
    }
}
