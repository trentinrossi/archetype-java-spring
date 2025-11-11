package com.example.demo.controller;

import com.example.demo.dto.*;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing credit card accounts and bill payments")
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
        log.info("Retrieving all accounts with pagination: {}", pageable);
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by ID", description = "BR001: Account Validation - Retrieve an account by their account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "400", description = "Account ID can NOT be empty..."),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable String accountId) {
        log.info("Retrieving account by ID: {}", accountId);
        
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get account balance", 
               description = "BR001: Account Validation & BR002: Balance Check - Retrieve the current balance for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account balance"),
        @ApiResponse(responseCode = "400", description = "Account ID can NOT be empty..."),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<AccountBalanceDto> getAccountBalance(@PathVariable String accountId) {
        log.info("Retrieving balance for account ID: {}", accountId);
        
        AccountBalanceDto balance = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Create a new account", description = "Create a new credit card account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - Account ID can NOT be empty... or You have nothing to pay..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Process bill payment", 
               description = "BR001-BR007: Process a full balance bill payment for an account. " +
                           "Validates account, checks balance, requires confirmation, processes full payment, " +
                           "records transaction, and updates balance.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data - Account ID can NOT be empty... or Invalid value. Valid values are (Y/N)..."),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "422", description = "You have nothing to pay..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/process-payment")
    public ResponseEntity<ProcessPaymentResponseDto> processPayment(@Valid @RequestBody ProcessPaymentRequestDto request) {
        log.info("Processing payment for account ID: {}", request.getAccountId());
        
        try {
            ProcessPaymentResponseDto response = accountService.processPayment(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Payment processing failed: {}", e.getMessage());
            
            // Return appropriate HTTP status based on error message
            if (e.getMessage().contains("nothing to pay")) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            } else if (e.getMessage().contains("NOT found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else if (e.getMessage().contains("cancelled")) {
                ProcessPaymentResponseDto cancelResponse = new ProcessPaymentResponseDto();
                cancelResponse.setAccountId(request.getAccountId());
                cancelResponse.setMessage("Payment cancelled by user");
                return ResponseEntity.ok(cancelResponse);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @Operation(summary = "Update an existing account", description = "Update account details by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or Account ID can NOT be empty..."),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable String accountId,
            @Valid @RequestBody UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);
        
        AccountResponseDto response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an account", description = "Delete an account by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Account ID can NOT be empty..."),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        log.info("Deleting account with ID: {}", accountId);
        
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
