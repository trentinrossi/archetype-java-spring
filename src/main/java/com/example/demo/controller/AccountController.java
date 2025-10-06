package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import com.example.demo.service.AccountService;
import com.example.demo.dto.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing accounts based on CBACT01C program functionality")
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
    public ResponseEntity<Page<AccountResponse>> getAllAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all accounts with pagination: {}", pageable);
        Page<AccountResponse> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by ID", description = "Retrieve an account by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of account"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{acctId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable String acctId) {
        log.info("Retrieving account by ID: {}", acctId);
        return accountService.getAccountById(acctId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new account", description = "Create a new account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Account already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        log.info("Creating new account with ID: {}", request.getAcctId());
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Update an existing account", description = "Update account details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{acctId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable String acctId,
            @Valid @RequestBody UpdateAccountRequest request) {
        log.info("Updating account with ID: {}", acctId);
        AccountResponse response = accountService.updateAccount(acctId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete an account", description = "Delete an account by ID")
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

    @Operation(summary = "Sequential read of accounts", description = "Read accounts sequentially based on CBACT01C program functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful sequential read"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/sequential-read")
    public ResponseEntity<AccountSequentialReadResponse> sequentialRead(
            @Valid @RequestBody AccountSequentialReadRequest request) {
        log.info("Sequential read starting from account: {}", request.getStartingAcctId());
        AccountSequentialReadResponse response = accountService.sequentialReadAccounts(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get accounts by status", description = "Retrieve accounts filtered by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts by status"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AccountResponse>> getAccountsByStatus(
            @PathVariable String status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving accounts with status: {}", status);
        Page<AccountResponse> accounts = accountService.getAccountsByStatus(status, pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get accounts by group", description = "Retrieve accounts filtered by group ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of accounts by group"),
        @ApiResponse(responseCode = "400", description = "Invalid group ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/group/{groupId}")
    public ResponseEntity<Page<AccountResponse>> getAccountsByGroup(
            @PathVariable String groupId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving accounts with group ID: {}", groupId);
        Page<AccountResponse> accounts = accountService.getAccountsByGroup(groupId, pageable);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get active accounts", description = "Retrieve all active accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of active accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/active")
    public ResponseEntity<List<AccountResponse>> getActiveAccounts() {
        log.info("Retrieving all active accounts");
        List<AccountResponse> accounts = accountService.getActiveAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get inactive accounts", description = "Retrieve all inactive accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of inactive accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/inactive")
    public ResponseEntity<List<AccountResponse>> getInactiveAccounts() {
        log.info("Retrieving all inactive accounts");
        List<AccountResponse> accounts = accountService.getInactiveAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get over-limit accounts", description = "Retrieve accounts that are over their credit limit")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of over-limit accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/over-limit")
    public ResponseEntity<List<AccountResponse>> getOverLimitAccounts() {
        log.info("Retrieving accounts over credit limit");
        List<AccountResponse> accounts = accountService.getOverLimitAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get over cash limit accounts", description = "Retrieve accounts that are over their cash credit limit")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of over cash limit accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/over-cash-limit")
    public ResponseEntity<List<AccountResponse>> getOverCashLimitAccounts() {
        log.info("Retrieving accounts over cash credit limit");
        List<AccountResponse> accounts = accountService.getOverCashLimitAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get expiring accounts", description = "Retrieve accounts expiring before a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of expiring accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/expiring")
    public ResponseEntity<List<AccountResponse>> getExpiringAccounts(
            @RequestParam(required = false) String beforeDate) {
        log.info("Retrieving accounts expiring before: {}", beforeDate);
        LocalDate date = beforeDate != null ? LocalDate.parse(beforeDate) : LocalDate.now().plusMonths(3);
        List<AccountResponse> accounts = accountService.getExpiringAccounts(date);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get recently reissued accounts", description = "Retrieve accounts reissued since a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of recently reissued accounts"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/recently-reissued")
    public ResponseEntity<List<AccountResponse>> getRecentlyReissuedAccounts(
            @RequestParam(required = false) String sinceDate) {
        log.info("Retrieving accounts reissued since: {}", sinceDate);
        LocalDate date = sinceDate != null ? LocalDate.parse(sinceDate) : LocalDate.now().minusMonths(6);
        List<AccountResponse> accounts = accountService.getRecentlyReissuedAccounts(date);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Open account file", description = "Open account file for sequential processing - CBACT01C functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account file opened successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/file/open")
    public ResponseEntity<String> openAccountFile() {
        log.info("Opening account file for sequential processing");
        accountService.openAccountFile();
        return ResponseEntity.ok("Account file opened successfully");
    }

    @Operation(summary = "Read accounts sequentially", description = "Read accounts sequentially - CBACT01C functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts read successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid record count"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/file/read/{recordCount}")
    public ResponseEntity<List<AccountResponse>> readAccountsSequentially(@PathVariable int recordCount) {
        log.info("Reading {} accounts sequentially", recordCount);
        List<AccountResponse> accounts = accountService.readAccountsSequentially(recordCount);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Close account file", description = "Close account file after sequential processing - CBACT01C functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account file closed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/file/close")
    public ResponseEntity<String> closeAccountFile() {
        log.info("Closing account file after sequential processing");
        accountService.closeAccountFile();
        return ResponseEntity.ok("Account file closed successfully");
    }

    @Operation(summary = "Display account record", description = "Display account record details - CBACT01C functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account record displayed successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{acctId}/display")
    public ResponseEntity<String> displayAccountRecord(@PathVariable String acctId) {
        log.info("Displaying account record for ID: {}", acctId);
        return accountService.getAccountById(acctId)
                .map(account -> {
                    accountService.displayAccountRecord(account);
                    return ResponseEntity.ok("Account record displayed in logs");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get distinct statuses", description = "Get all distinct account statuses")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of distinct statuses"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/distinct/statuses")
    public ResponseEntity<List<String>> getDistinctStatuses() {
        log.info("Retrieving distinct account statuses");
        List<String> statuses = accountService.getDistinctStatuses();
        return ResponseEntity.ok(statuses);
    }

    @Operation(summary = "Get distinct group IDs", description = "Get all distinct account group IDs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful retrieval of distinct group IDs"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/distinct/groups")
    public ResponseEntity<List<String>> getDistinctGroupIds() {
        log.info("Retrieving distinct account group IDs");
        List<String> groupIds = accountService.getDistinctGroupIds();
        return ResponseEntity.ok(groupIds);
    }

    @Operation(summary = "Count accounts by status", description = "Get count of accounts by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful count retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countAccountsByStatus(@PathVariable String status) {
        log.info("Counting accounts with status: {}", status);
        Long count = accountService.countAccountsByStatus(status);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Count accounts by group", description = "Get count of accounts by group ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful count retrieval"),
        @ApiResponse(responseCode = "400", description = "Invalid group ID"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/count/group/{groupId}")
    public ResponseEntity<Long> countAccountsByGroup(@PathVariable String groupId) {
        log.info("Counting accounts with group ID: {}", groupId);
        Long count = accountService.countAccountsByGroup(groupId);
        return ResponseEntity.ok(count);
    }
}