package com.example.demo.controller;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<Page<AccountResponseDto>> getAllAccounts(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Retrieving all accounts with pagination: {}", pageable);
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable String id) {
        log.info("Retrieving account by ID: {}", id);
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        AccountResponseDto response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> updateAccount(
            @PathVariable String id,
            @Valid @RequestBody UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", id);
        AccountResponseDto response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        log.info("Deleting account with ID: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process-sequential")
    public ResponseEntity<List<AccountResponseDto>> processAccountsSequentially() {
        log.info("Starting sequential account record processing (BR-001)");
        List<AccountResponseDto> processedAccounts = accountService.processAllAccountsSequentially();
        return ResponseEntity.ok(processedAccounts);
    }

    @PostMapping("/{id}/calculate-interest")
    public ResponseEntity<BigDecimal> calculateInterest(
            @PathVariable String id,
            @RequestParam String categoryCode,
            @RequestParam String transactionTypeCode,
            @RequestParam BigDecimal categoryBalance) {
        log.info("Calculating interest for account ID: {}", id);
        BigDecimal interest = accountService.calculateInterestByTransactionCategory(
                id, categoryCode, transactionTypeCode, categoryBalance);
        return ResponseEntity.ok(interest);
    }

    @PostMapping("/{id}/update-balance")
    public ResponseEntity<String> updateAccountBalance(
            @PathVariable String id,
            @RequestParam BigDecimal totalInterest) {
        log.info("Updating balance for account ID: {} with interest: {}", id, totalInterest);
        accountService.updateAccountBalanceWithInterest(id, totalInterest);
        return ResponseEntity.ok("Account balance updated successfully");
    }

    @GetMapping("/{id}/validate-payment")
    public ResponseEntity<String> validateAccountForPayment(@PathVariable String id) {
        log.info("Validating account for payment: {}", id);
        try {
            AccountResponseDto account = accountService.getAccountById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));
            accountService.validateBalanceForPayment(account.getCurrentBalance());
            return ResponseEntity.ok("Account validated successfully for payment");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
