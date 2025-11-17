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

import jakarta.validation.Valid;

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
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by ID", description = "Retrieve an account by their ID")
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

    @Operation(summary = "Create a new account", description = "Create a new account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(
            @Valid @RequestBody CreateAccountRequestDto request) {
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
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequestDto request) {
        AccountResponseDto response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an account", description = "Delete an account by ID")
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

    @Operation(summary = "Validate account exists", description = "BR002: Validate that account ID exists in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account exists"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/validate/{id}")
    public ResponseEntity<Void> validateAccountExists(@PathVariable Long id) {
        accountService.validateAccountExists(id);
        return ResponseEntity.ok().build();
    }
}
