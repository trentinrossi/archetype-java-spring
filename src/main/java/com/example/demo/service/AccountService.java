package com.example.demo.service;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        
        Long accountId = Long.parseLong(request.getAccountId());
        
        // BR001: Account Number Validation
        validateAccountId(accountId);
        
        if (accountRepository.existsByAccountId(accountId)) {
            throw new IllegalArgumentException("Account with ID already exists");
        }
        
        Account account = new Account();
        account.setAccountId(accountId);
        
        Account savedAccount = accountRepository.save(account);
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long accountId) {
        log.info("Retrieving account with ID: {}", accountId);
        
        // BR001: Account Number Validation
        validateAccountId(accountId);
        
        return accountRepository.findById(accountId)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(Long id, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (request.getAccountId() != null) {
            Long newAccountId = Long.parseLong(request.getAccountId());
            validateAccountId(newAccountId);
            account.setAccountId(newAccountId);
        }
        
        Account updatedAccount = accountRepository.save(account);
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long id) {
        log.info("Deleting account with ID: {}", id);
        
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("Account not found");
        }
        
        accountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> searchAccounts(String searchTerm, Pageable pageable) {
        log.info("Searching accounts with term: {}", searchTerm);
        
        // BR003: Search Criteria Requirement - at least one criterion must be provided
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("At least one search criterion must be provided");
        }
        
        return accountRepository.searchByAccountId(searchTerm, pageable)
                .map(this::convertToResponse);
    }

    /**
     * BR001: Account Number Validation
     * Account number must be a valid 11-digit numeric value when provided
     */
    private void validateAccountId(Long accountId) {
        log.debug("Validating account ID: {}", accountId);

        if (accountId == null) {
            log.error("Account ID must be provided");
            throw new IllegalArgumentException("Account ID must be provided and must be 11 digits");
        }

        String accountIdStr = String.valueOf(accountId);

        // Must be numeric
        if (!accountIdStr.matches("\\d+")) {
            log.error("Account ID must be numeric: {}", accountId);
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }

        // Must be exactly 11 digits
        if (accountIdStr.length() != 11) {
            log.error("Account ID must be exactly 11 digits: {}", accountId);
            throw new IllegalArgumentException("ACCOUNT NUMBER MUST BE 11 DIGITS");
        }

        // Cannot be zero or all zeros
        if (accountId == 0L || accountIdStr.matches("^0+$")) {
            log.error("Account ID cannot be zero: {}", accountId);
            throw new IllegalArgumentException("Please enter a valid account number");
        }
    }

    /**
     * BR004: Account Number Filter Validation
     * Validates account number format when used as a filter
     */
    public void validateAccountNumberFilter(Long accountId) {
        log.debug("Validating account number filter: {}", accountId);

        if (accountId == null) {
            return; // Filter is optional
        }

        String accountIdStr = String.valueOf(accountId);

        // If provided, must be numeric
        if (!accountIdStr.matches("\\d+")) {
            log.error("Account filter must be numeric: {}", accountId);
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }

        // If provided, must be exactly 11 digits
        if (accountIdStr.length() != 11) {
            log.error("Account filter must be exactly 11 digits: {}", accountId);
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }

        // Cannot be zero
        if (accountId == 0L) {
            log.error("Account filter cannot be zero: {}", accountId);
            throw new IllegalArgumentException("Please enter a valid account number");
        }
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setFormattedAccountId(account.getFormattedAccountId());
        response.setCreditCardCount(account.getCreditCardCount());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
