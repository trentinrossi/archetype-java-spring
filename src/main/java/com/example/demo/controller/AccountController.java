package com.example.demo.controller;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing accounts and bill payment processing")
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
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{acctId}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable String acctId) {
        log.info("Retrieving account by ID: {}", acctId);
        return accountService.getAccountById(acctId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new account", description = "Create a new account in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAcctId());
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing account", description = "Update account details by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{acctId}")
    public ResponseEntity<AccountResponseDto> updateAccount(@PathVariable String acctId,
                                                           @RequestBody UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", acctId);
        AccountResponseDto response = accountService.updateAccount(acctId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an account", description = "Delete an account by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{acctId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String acctId) {
        log.info("Deleting account with ID: {}", acctId);
        accountService.deleteAccount(acctId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validate account", description = "Validates that the entered account ID is valid and exists in the system (BR001)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account validation successful"),
        @ApiResponse(responseCode = "400", description = "Account ID is empty or invalid"),
        @ApiResponse(responseCode = "404", description = "Account ID not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{acctId}/validate")
    public ResponseEntity<Map<String, Object>> validateAccount(@PathVariable String acctId) {
        log.info("Validating account with ID: {}", acctId);
        try {
            accountService.validateAccount(acctId);
            Map<String, Object> response = new HashMap<>();
            response.put("acctId", acctId);
            response.put("valid", true);
            response.put("message", "Account validation successful");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Account validation failed: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("acctId", acctId);
            response.put("valid", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Check account balance", description = "Verifies that the account has a positive balance before allowing payment (BR002)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance check successful"),
        @ApiResponse(responseCode = "400", description = "Account has zero or negative balance"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{acctId}/check-balance")
    public ResponseEntity<Map<String, Object>> checkBalance(@PathVariable String acctId) {
        log.info("Checking balance for account ID: {}", acctId);
        try {
            accountService.checkBalance(acctId);
            AccountResponseDto account = accountService.getAccountById(acctId).orElseThrow();
            Map<String, Object> response = new HashMap<>();
            response.put("acctId", acctId);
            response.put("hasBalance", true);
            response.put("currentBalance", account.getAcctCurrBal());
            response.put("message", "Account has sufficient balance for payment");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Balance check failed: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("acctId", acctId);
            response.put("hasBalance", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Process full balance payment", description = "Process bill payment for the full current account balance (BR004, BR006, BR007)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid payment request or insufficient balance"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{acctId}/process-payment")
    public ResponseEntity<Map<String, Object>> processPayment(@PathVariable String acctId) {
        log.info("Processing full balance payment for account ID: {}", acctId);
        try {
            BigDecimal paymentAmount = accountService.processFullBalancePayment(acctId);
            AccountResponseDto account = accountService.getAccountById(acctId).orElseThrow();
            
            Map<String, Object> response = new HashMap<>();
            response.put("acctId", acctId);
            response.put("paymentAmount", paymentAmount);
            response.put("newBalance", account.getAcctCurrBal());
            response.put("status", "SUCCESS");
            response.put("message", "Full balance payment processed successfully");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Payment processing failed: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("acctId", acctId);
            response.put("status", "FAILED");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get accounts with positive balance", description = "Retrieve all accounts that have a positive balance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts with positive balance"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/positive-balance")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsWithPositiveBalance(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving accounts with positive balance with pagination: {}", pageable);
        Page<AccountResponseDto> accounts = accountService.getAccountsWithPositiveBalance(pageable);
        return ResponseEntity.ok(accounts);
    }
}
