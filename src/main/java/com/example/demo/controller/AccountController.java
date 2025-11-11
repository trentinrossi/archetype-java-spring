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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing account data - CBACT01C Account Data File Reader and Printer")
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Get all accounts", description = "Retrieve a paginated list of all accounts from the VSAM KSDS file")
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

    @Operation(summary = "Get account by ID", description = "Retrieve a single account by its unique 11-digit account identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "400", description = "Invalid Account ID format"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable String id) {
        log.info("Retrieving account by ID: {}", id);
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new account", description = "Create a new account record in the VSAM KSDS file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or validation failure"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAcctId());
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing account", description = "Update account details by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data or validation failure"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable String id,
            @Valid @RequestBody UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", id);
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
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        log.info("Deleting account with ID: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get all accounts sequentially",
        description = "BR-001: Process account records sequentially from the account file until end-of-file is reached. Reads records one by one in sequential order using account ID as the key."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful sequential retrieval of all accounts"),
        @ApiResponse(responseCode = "400", description = "Account file access error"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/sequential")
    public ResponseEntity<List<AccountResponseDto>> getAllAccountsSequentially() {
        log.info("Processing accounts sequentially - BR-001");
        List<AccountResponseDto> accounts = accountService.getAllAccountsSequentially();
        return ResponseEntity.ok(accounts);
    }

    @Operation(
        summary = "Get active accounts",
        description = "Retrieve all accounts with active status ('A')"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of active accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<List<AccountResponseDto>> getActiveAccounts() {
        log.info("Retrieving active accounts");
        List<AccountResponseDto> accounts = accountService.getActiveAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(
        summary = "Get expired accounts",
        description = "Retrieve all accounts that have passed their expiration date"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of expired accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/expired")
    public ResponseEntity<List<AccountResponseDto>> getExpiredAccounts() {
        log.info("Retrieving expired accounts");
        List<AccountResponseDto> accounts = accountService.getExpiredAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(
        summary = "Display account information",
        description = "BR-002: Display all account information fields for a specific account record. Shows Account ID, Active Status, Current Balance, Credit Limit, Cash Credit Limit, Open Date, Expiration Date, Reissue Date, Current Cycle Credit, Current Cycle Debit, and Group ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful display of account information"),
        @ApiResponse(responseCode = "400", description = "Invalid Account ID format"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/display")
    public ResponseEntity<AccountResponseDto> displayAccountInformation(@PathVariable String id) {
        log.info("Displaying complete account information for ID: {} - BR-002", id);
        return accountService.getAccountById(id)
                .map(account -> {
                    accountService.displayAllAccountInformation(account);
                    return ResponseEntity.ok(account);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Process account sequentially",
        description = "BR-001 & BR-003 & BR-004: Process a specific account record sequentially. Opens account file for input operations (BR-003), reads the account record, displays complete information (BR-002), and handles end-of-file detection (BR-004). File status must be '00' for successful operation."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account processed successfully - File status 00"),
        @ApiResponse(responseCode = "400", description = "Invalid Account ID format or file access error - File status not 00"),
        @ApiResponse(responseCode = "404", description = "Account not found or end-of-file reached - File status 10"),
        @ApiResponse(responseCode = "500", description = "Internal server error - File status 12")
    })
    @PostMapping("/{id}/process")
    public ResponseEntity<Void> processAccountSequentially(@PathVariable String id) {
        log.info("Processing account sequentially for ID: {} - BR-001, BR-003, BR-004", id);
        accountService.processAccountSequentially(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Check if account exists",
        description = "Validate if an account with the given ID exists in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account existence check completed"),
        @ApiResponse(responseCode = "400", description = "Invalid Account ID format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkAccountExists(@PathVariable String id) {
        log.info("Checking if account exists: {}", id);
        boolean exists = accountService.checkAccountExists(id);
        return ResponseEntity.ok(exists);
    }
}
