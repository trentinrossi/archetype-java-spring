package com.example.demo.controller;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * AccountController
 * 
 * REST API controller for Account management.
 * Provides endpoints for CRUD operations and user access management.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR004: Account Filter Validation
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing customer accounts")
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
        log.info("Fetching all accounts with pagination");
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by ID", description = "Retrieve a specific account by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccountById(
            @Parameter(description = "Account ID (11 digit numeric value)", required = true)
            @PathVariable String accountId) {
        log.info("Fetching account with ID: {}", accountId);
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new account", description = "Create a new customer account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - account ID must be 11 digits"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody CreateAccountRequestDto request) {
        log.info("Creating new account");
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
            @Parameter(description = "Account ID (11 digit numeric value)", required = true)
            @PathVariable String accountId,
            @Valid @RequestBody UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);
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
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account ID (11 digit numeric value)", required = true)
            @PathVariable String accountId) {
        log.info("Deleting account with ID: {}", accountId);
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get accounts accessible by user", 
               description = "Retrieve accounts accessible by a specific user. Implements BR001 - admin users see all accounts, regular users see only their accessible accounts.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsAccessibleByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching accounts accessible by user: {}", userId);
        Page<AccountResponseDto> accounts = accountService.getAccountsAccessibleByUser(userId, pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Grant user access to account", 
               description = "Grant a specific user access to an account. Implements BR001.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access granted successfully"),
        @ApiResponse(responseCode = "404", description = "Account or user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{accountId}/grant-access/{userId}")
    public ResponseEntity<Void> grantUserAccessToAccount(
            @Parameter(description = "Account ID (11 digit numeric value)", required = true)
            @PathVariable String accountId,
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.info("Granting user {} access to account {}", userId, accountId);
        accountService.grantUserAccessToAccount(userId, accountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Revoke user access from account", 
               description = "Revoke a specific user's access to an account. Implements BR001.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access revoked successfully"),
        @ApiResponse(responseCode = "404", description = "Account or user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{accountId}/revoke-access/{userId}")
    public ResponseEntity<Void> revokeUserAccessFromAccount(
            @Parameter(description = "Account ID (11 digit numeric value)", required = true)
            @PathVariable String accountId,
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.info("Revoking user {} access from account {}", userId, accountId);
        accountService.revokeUserAccessFromAccount(userId, accountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Check user access to account", 
               description = "Check if a user has access to a specific account. Implements BR001.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Access check completed"),
        @ApiResponse(responseCode = "404", description = "Account or user not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}/has-access/{userId}")
    public ResponseEntity<Boolean> userHasAccessToAccount(
            @Parameter(description = "Account ID (11 digit numeric value)", required = true)
            @PathVariable String accountId,
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.info("Checking if user {} has access to account {}", userId, accountId);
        boolean hasAccess = accountService.userHasAccessToAccount(userId, accountId);
        return ResponseEntity.ok(hasAccess);
    }
}
