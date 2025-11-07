package com.example.demo.controller;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.service.AccountService;
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
import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Account management.
 * Provides endpoints for CRUD operations and business-specific queries.
 * Implements BR-001: Account File Sequential Processing
 * Implements BR-002: Account Record Display
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing customer accounts")
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    /**
     * Get all accounts with pagination.
     * Supports BR-002: Account Record Display
     */
    @Operation(summary = "Get all accounts", description = "Retrieve a paginated list of all accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<AccountResponseDto>> getAllAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts - Retrieving all accounts");
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get account by ID.
     * Supports BR-002: Account Record Display
     */
    @Operation(summary = "Get account by ID", description = "Retrieve an account by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{acctId}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long acctId) {
        log.info("GET /api/accounts/{} - Retrieving account", acctId);
        return accountService.getAccountById(acctId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create a new account.
     */
    @Operation(summary = "Create a new account", description = "Create a new customer account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody CreateAccountRequestDto request) {
        log.info("POST /api/accounts - Creating new account");
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Update an existing account.
     */
    @Operation(summary = "Update an existing account", description = "Update account details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{acctId}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable Long acctId,
            @Valid @RequestBody UpdateAccountRequestDto request) {
        log.info("PUT /api/accounts/{} - Updating account", acctId);
        AccountResponseDto response = accountService.updateAccount(acctId, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Delete an account.
     */
    @Operation(summary = "Delete an account", description = "Delete an account by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{acctId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long acctId) {
        log.info("DELETE /api/accounts/{} - Deleting account", acctId);
        accountService.deleteAccount(acctId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Process all accounts sequentially.
     * Implements BR-001: Account File Sequential Processing
     * Implements BR-004: End-of-File Detection
     */
    @Operation(summary = "Process accounts sequentially", 
               description = "Process all accounts in sequential order (BR-001)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts processed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/process-sequential")
    public ResponseEntity<List<AccountResponseDto>> processAccountsSequentially() {
        log.info("GET /api/accounts/process-sequential - Processing accounts sequentially");
        List<AccountResponseDto> accounts = accountService.processAccountsSequentially();
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get all active accounts.
     */
    @Operation(summary = "Get active accounts", description = "Retrieve all active accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of active accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<Page<AccountResponseDto>> getActiveAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/active - Retrieving active accounts");
        Page<AccountResponseDto> accounts = accountService.getActiveAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get all inactive accounts.
     */
    @Operation(summary = "Get inactive accounts", description = "Retrieve all inactive accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of inactive accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/inactive")
    public ResponseEntity<Page<AccountResponseDto>> getInactiveAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/inactive - Retrieving inactive accounts");
        Page<AccountResponseDto> accounts = accountService.getInactiveAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get accounts by group ID.
     */
    @Operation(summary = "Get accounts by group", description = "Retrieve accounts by group ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/group/{groupId}")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsByGroupId(
            @PathVariable String groupId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/group/{} - Retrieving accounts by group", groupId);
        Page<AccountResponseDto> accounts = accountService.getAccountsByGroupId(groupId, pageable);
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get accounts expiring before a specific date.
     */
    @Operation(summary = "Get expiring accounts", 
               description = "Retrieve accounts expiring before a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of expiring accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/expiring-before")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsExpiringBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/expiring-before?date={} - Retrieving expiring accounts", date);
        Page<AccountResponseDto> accounts = accountService.getAccountsExpiringBefore(date, pageable);
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get accounts over their credit limit.
     */
    @Operation(summary = "Get accounts over credit limit", 
               description = "Retrieve accounts with balance exceeding credit limit")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/over-credit-limit")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsOverCreditLimit(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/over-credit-limit - Retrieving accounts over credit limit");
        Page<AccountResponseDto> accounts = accountService.getAccountsOverCreditLimit(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get total balance across all accounts.
     */
    @Operation(summary = "Get total balance", 
               description = "Calculate total balance across all accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful calculation of total balance"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/total-balance")
    public ResponseEntity<BigDecimal> getTotalBalance() {
        log.info("GET /api/accounts/total-balance - Calculating total balance");
        BigDecimal totalBalance = accountService.getTotalBalance();
        return ResponseEntity.ok(totalBalance);
    }
    
    /**
     * Get total balance for active accounts.
     */
    @Operation(summary = "Get total active balance", 
               description = "Calculate total balance for active accounts only")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful calculation of total active balance"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/total-active-balance")
    public ResponseEntity<BigDecimal> getTotalActiveBalance() {
        log.info("GET /api/accounts/total-active-balance - Calculating total active balance");
        BigDecimal totalBalance = accountService.getTotalActiveBalance();
        return ResponseEntity.ok(totalBalance);
    }
    
    /**
     * Get count of active accounts.
     */
    @Operation(summary = "Get active account count", 
               description = "Get the count of active accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of count"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count/active")
    public ResponseEntity<Long> getActiveAccountCount() {
        log.info("GET /api/accounts/count/active - Counting active accounts");
        long count = accountService.getActiveAccountCount();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Get count of inactive accounts.
     */
    @Operation(summary = "Get inactive account count", 
               description = "Get the count of inactive accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of count"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count/inactive")
    public ResponseEntity<Long> getInactiveAccountCount() {
        log.info("GET /api/accounts/count/inactive - Counting inactive accounts");
        long count = accountService.getInactiveAccountCount();
        return ResponseEntity.ok(count);
    }
}
