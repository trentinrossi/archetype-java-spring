package com.example.demo.controller;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.enums.AccountStatus;
import com.example.demo.service.AccountService;
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
import java.util.List;

/**
 * REST Controller for Account operations
 * Provides endpoints for account management
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing accounts")
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    @Operation(summary = "Get all accounts", description = "Retrieve a paginated list of all accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<AccountResponseDto>> getAllAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts - Fetching all accounts");
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    @Operation(summary = "Get account by ID", description = "Retrieve an account by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable String accountId) {
        log.info("GET /api/accounts/{} - Fetching account", accountId);
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Create a new account", description = "Create a new account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody CreateAccountRequestDto request) {
        log.info("POST /api/accounts - Creating new account");
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing account", description = "Update account details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable String accountId,
            @RequestBody UpdateAccountRequestDto request) {
        log.info("PUT /api/accounts/{} - Updating account", accountId);
        AccountResponseDto response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete an account", description = "Delete an account by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        log.info("DELETE /api/accounts/{} - Deleting account", accountId);
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Get accounts by customer ID", description = "Retrieve all accounts for a specific customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByCustomerId(@PathVariable String customerId) {
        log.info("GET /api/accounts/customer/{} - Fetching accounts for customer", customerId);
        List<AccountResponseDto> accounts = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }
    
    @Operation(summary = "Get accounts by status", description = "Retrieve accounts by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsByStatus(
            @PathVariable AccountStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/status/{} - Fetching accounts by status", status);
        Page<AccountResponseDto> accounts = accountService.getAccountsByStatus(status, pageable);
        return ResponseEntity.ok(accounts);
    }
    
    @Operation(summary = "Get delinquent accounts", description = "Retrieve all delinquent accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/delinquent")
    public ResponseEntity<Page<AccountResponseDto>> getDelinquentAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/delinquent - Fetching delinquent accounts");
        Page<AccountResponseDto> accounts = accountService.getDelinquentAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    @Operation(summary = "Get over-limit accounts", description = "Retrieve all over-limit accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/over-limit")
    public ResponseEntity<Page<AccountResponseDto>> getOverLimitAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/accounts/over-limit - Fetching over-limit accounts");
        Page<AccountResponseDto> accounts = accountService.getOverLimitAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    @Operation(summary = "Apply payment to account", description = "Apply a payment to an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment applied successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid payment amount"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{accountId}/payment")
    public ResponseEntity<AccountResponseDto> applyPayment(
            @PathVariable String accountId,
            @RequestParam BigDecimal amount) {
        log.info("POST /api/accounts/{}/payment - Applying payment of {}", accountId, amount);
        AccountResponseDto response = accountService.applyPayment(accountId, amount);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Apply charge to account", description = "Apply a charge to an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Charge applied successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid charge amount or exceeds credit limit"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{accountId}/charge")
    public ResponseEntity<AccountResponseDto> applyCharge(
            @PathVariable String accountId,
            @RequestParam BigDecimal amount) {
        log.info("POST /api/accounts/{}/charge - Applying charge of {}", accountId, amount);
        AccountResponseDto response = accountService.applyCharge(accountId, amount);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Calculate and apply interest", description = "Calculate and apply interest to an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Interest applied successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{accountId}/interest")
    public ResponseEntity<AccountResponseDto> calculateAndApplyInterest(@PathVariable String accountId) {
        log.info("POST /api/accounts/{}/interest - Calculating and applying interest", accountId);
        AccountResponseDto response = accountService.calculateAndApplyInterest(accountId);
        return ResponseEntity.ok(response);
    }
}
