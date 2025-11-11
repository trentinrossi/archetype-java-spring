package com.carddemo.billpayment.controller;

import com.carddemo.billpayment.dto.AccountResponseDto;
import com.carddemo.billpayment.dto.CreateAccountRequestDto;
import com.carddemo.billpayment.dto.UpdateAccountRequestDto;
import com.carddemo.billpayment.service.AccountService;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    @Operation(summary = "Get account by ID", description = "Retrieve an account by their account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{acctId}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable String acctId) {
        log.info("Retrieving account by ID: {}", acctId);
        return accountService.getAccountById(acctId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new account", description = "Create a new credit card account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        log.info("Creating new account");
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing account", description = "Update account details by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{acctId}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable String acctId, 
            @Valid @RequestBody UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", acctId);
        AccountResponseDto response = accountService.updateAccount(acctId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete an account", description = "Delete an account by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{acctId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String acctId) {
        log.info("Deleting account with ID: {}", acctId);
        accountService.deleteAccount(acctId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Validate account for payment", 
               description = "BR001: Validates that the account exists and BR002: has a positive balance for bill payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account validation successful"),
        @ApiResponse(responseCode = "400", description = "Acct ID can NOT be empty... or You have nothing to pay..."),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{acctId}/validate-payment")
    public ResponseEntity<Map<String, Object>> validateAccountForPayment(@PathVariable String acctId) {
        log.info("Validating account for payment: {}", acctId);
        
        try {
            accountService.validateAccountExists(acctId);
            accountService.validatePositiveBalance(acctId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("message", "Account is valid for payment");
            response.put("accountId", acctId);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", e.getMessage());
            response.put("accountId", acctId);
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @Operation(summary = "Get payment amount", 
               description = "BR004: Retrieves the full current account balance that needs to be paid")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment amount retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Acct ID can NOT be empty... or You have nothing to pay..."),
        @ApiResponse(responseCode = "404", description = "Account ID NOT found..."),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{acctId}/payment-amount")
    public ResponseEntity<Map<String, Object>> getPaymentAmount(@PathVariable String acctId) {
        log.info("Retrieving payment amount for account: {}", acctId);
        
        try {
            BigDecimal paymentAmount = accountService.getFullBalancePaymentAmount(acctId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("accountId", acctId);
            response.put("paymentAmount", paymentAmount);
            response.put("formattedAmount", String.format("$%.2f", paymentAmount));
            response.put("message", "Full balance payment amount");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("accountId", acctId);
            response.put("paymentAmount", BigDecimal.ZERO);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @Operation(summary = "Get accounts with positive balance", 
               description = "Retrieve accounts that are eligible for bill payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/positive-balance")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsWithPositiveBalance(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving accounts with positive balance");
        Page<AccountResponseDto> accounts = accountService.getAccountsWithPositiveBalance(pageable);
        return ResponseEntity.ok(accounts);
    }
}
