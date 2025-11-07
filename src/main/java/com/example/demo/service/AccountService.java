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

/**
 * Service for Account entity operations
 * Business Rule: Account serves as the primary container for credit card management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    /**
     * Create a new account
     * Validates that account ID is exactly 11 numeric digits
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        
        // Validate account ID format
        if (!request.getAccountId().matches("\\d{11}")) {
            throw new IllegalArgumentException("Account ID must be exactly 11 numeric digits");
        }
        
        // Check if account already exists
        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Account with ID " + request.getAccountId() + " already exists");
        }
        
        Account account = new Account(request.getAccountId());
        
        // Validate using entity method
        if (!account.isValidAccountId()) {
            throw new IllegalArgumentException("Invalid account ID format");
        }
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        
        return convertToResponse(savedAccount);
    }
    
    /**
     * Get account by ID
     */
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String accountId) {
        log.debug("Fetching account with ID: {}", accountId);
        return accountRepository.findByAccountId(accountId)
                .map(this::convertToResponse);
    }
    
    /**
     * Update an existing account
     */
    @Transactional
    public AccountResponseDto updateAccount(String accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        
        // Note: Account ID cannot be changed after creation
        // Additional fields can be updated here if needed
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        
        return convertToResponse(updatedAccount);
    }
    
    /**
     * Delete an account
     */
    @Transactional
    public void deleteAccount(String accountId) {
        log.info("Deleting account with ID: {}", accountId);
        
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new IllegalArgumentException("Account not found with ID: " + accountId);
        }
        
        accountRepository.deleteById(accountId);
        log.info("Account deleted successfully with ID: {}", accountId);
    }
    
    /**
     * Get all accounts with pagination
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.debug("Fetching all accounts with pagination");
        return accountRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Get accounts accessible by a specific user
     * Business Rule BR001: Users have access to specific accounts
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsByUserId(String userId, Pageable pageable) {
        log.debug("Fetching accounts for user: {}", userId);
        // This would need to be implemented with proper pagination
        // For now, returning all accounts
        return accountRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    /**
     * Check if account exists
     */
    @Transactional(readOnly = true)
    public boolean accountExists(String accountId) {
        return accountRepository.existsByAccountId(accountId);
    }
    
    /**
     * Convert Account entity to AccountResponseDto
     */
    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setCreditCardCount(account.getCreditCards() != null ? account.getCreditCards().size() : 0);
        response.setActiveCreditCardCount(account.getActiveCreditCardCount());
        response.setUserCount(account.getUsers() != null ? account.getUsers().size() : 0);
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
