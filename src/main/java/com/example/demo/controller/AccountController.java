package com.example.demo.controller;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Account management.
 * Provides endpoints for account CRUD operations.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing customer accounts")
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    /**
     * Get all accounts with pagination
     * @param pageable pagination parameters
     * @return page of accounts
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
        log.info("GET /api/accounts - Fetching all accounts");
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Get account by ID
     * @param accountId the account identifier (11 digits)
     * @return account details
     */
    @Operation(summary = "Get account by ID", description = "Retrieve an account by its 11-digit identifier")
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
    
    /**
     * Create a new account
     * BR003: Account ID must be a valid 11-digit numeric value
     * @param request the create account request
     * @return created account details
     */
    @Operation(summary = "Create a new account", description = "Create a new customer account with 11-digit ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or account already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        log.info("POST /api/accounts - Creating new account");
        try {
            AccountResponseDto response = accountService.createAccount(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error creating account: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete an account
     * @param accountId the account identifier
     * @return no content
     */
    @Operation(summary = "Delete an account", description = "Delete an account by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        log.info("DELETE /api/accounts/{} - Deleting account", accountId);
        try {
            accountService.deleteAccount(accountId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting account: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Check if account exists
     * @param accountId the account identifier
     * @return true if account exists
     */
    @Operation(summary = "Check if account exists", description = "Validate if an account exists by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}/exists")
    public ResponseEntity<Boolean> accountExists(@PathVariable String accountId) {
        log.info("GET /api/accounts/{}/exists - Checking account existence", accountId);
        boolean exists = accountService.accountExists(accountId);
        return ResponseEntity.ok(exists);
    }
}
