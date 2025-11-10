package com.example.demo.service;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for Account entity.
 * Implements business logic for account operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    /**
     * Create a new account
     * BR003: Account ID must be a valid 11-digit numeric value
     * @param request the create account request
     * @return the created account response
     * @throws IllegalArgumentException if account already exists
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        
        // Validate account ID format (11 digits)
        if (!request.getAccountId().matches("^\\d{11}$")) {
            log.error("Invalid account ID format: {}", request.getAccountId());
            throw new IllegalArgumentException("Account ID must be exactly 11 digits");
        }
        
        // Check if account already exists
        if (accountRepository.existsByAccountId(request.getAccountId())) {
            log.error("Account already exists with ID: {}", request.getAccountId());
            throw new IllegalArgumentException("Account with ID " + request.getAccountId() + " already exists");
        }
        
        Account account = new Account();
        account.setAccountId(request.getAccountId());
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        
        return convertToResponse(savedAccount);
    }
    
    /**
     * Get account by ID
     * @param accountId the account identifier
     * @return Optional containing the account response if found
     */
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String accountId) {
        log.debug("Fetching account with ID: {}", accountId);
        return accountRepository.findByAccountId(accountId)
                .map(this::convertToResponse);
    }
    
    /**
     * Get all accounts with pagination
     * @param pageable pagination information
     * @return page of account responses
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.debug("Fetching all accounts with pagination");
        return accountRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Delete an account
     * @param accountId the account identifier
     * @throws IllegalArgumentException if account not found
     */
    @Transactional
    public void deleteAccount(String accountId) {
        log.info("Deleting account with ID: {}", accountId);
        
        if (!accountRepository.existsByAccountId(accountId)) {
            log.error("Account not found with ID: {}", accountId);
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }
        
        accountRepository.deleteById(accountId);
        log.info("Account deleted successfully with ID: {}", accountId);
    }
    
    /**
     * Check if account exists
     * BR003: Validate account ID when supplied
     * @param accountId the account identifier
     * @return true if account exists
     */
    @Transactional(readOnly = true)
    public boolean accountExists(String accountId) {
        // Validate account ID format
        if (accountId == null || accountId.isBlank()) {
            return false;
        }
        
        if (!accountId.matches("^\\d{11}$")) {
            log.warn("Invalid account ID format: {}", accountId);
            return false;
        }
        
        return accountRepository.existsByAccountId(accountId);
    }
    
    /**
     * Get accounts with credit cards
     * @return list of accounts that have at least one credit card
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsWithCreditCards(Pageable pageable) {
        log.debug("Fetching accounts with credit cards");
        return accountRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Convert Account entity to AccountResponseDto
     * @param account the account entity
     * @return the account response DTO
     */
    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setCreditCardCount(account.getCreditCardCount());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
