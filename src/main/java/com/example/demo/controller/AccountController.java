package com.example.demo.controller;

import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.dto.AccountResponseDto;
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

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing account data in CardDemo Application")
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Get all accounts", description = "Retrieve a paginated list of all account records")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<AccountResponseDto>> getAllAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by ID", description = "Retrieve a single account by its 11-digit account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new account", description = "Create a new account record with all required fields")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
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
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequestDto request) {
        AccountResponseDto response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an account", description = "Delete an account by its account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reset cycle amounts", description = "Reset current cycle credit and debit amounts to zero (BR009)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cycle amounts reset successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/reset-cycle")
    public ResponseEntity<Void> resetCycleAmounts(@PathVariable Long id) {
        accountService.resetCycleAmounts(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update balance with interest", description = "Update account balance with calculated interest and reset cycle amounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance updated successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/update-interest")
    public ResponseEntity<Void> updateBalanceWithInterest(
            @PathVariable Long id,
            @RequestParam BigDecimal interestAmount) {
        accountService.updateBalanceWithInterest(id, interestAmount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validate payment balance", description = "Validate if account has sufficient balance for payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment validation successful"),
        @ApiResponse(responseCode = "400", description = "Insufficient balance"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/validate-payment")
    public ResponseEntity<Void> validatePaymentBalance(@PathVariable Long id) {
        accountService.validatePaymentBalance(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get active accounts", description = "Retrieve all accounts with active status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of active accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<Page<AccountResponseDto>> getActiveAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AccountResponseDto> accounts = accountService.findAccountsByStatus("Y", pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get expired accounts", description = "Retrieve all accounts with expiration date in the past")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of expired accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/expired")
    public ResponseEntity<Page<AccountResponseDto>> getExpiredAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AccountResponseDto> accounts = accountService.findExpiredAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts with positive balance", description = "Retrieve all accounts with current balance greater than zero")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/with-balance")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsWithBalance(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AccountResponseDto> accounts = accountService.findAccountsWithBalance(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts by group ID", description = "Retrieve all accounts belonging to a specific group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-group/{groupId}")
    public ResponseEntity<Page<AccountResponseDto>> getAccountsByGroup(
            @PathVariable String groupId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AccountResponseDto> accounts = accountService.findAccountsByGroupId(groupId, pageable);
        return ResponseEntity.ok(accounts);
    }
}
