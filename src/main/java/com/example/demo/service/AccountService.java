package com.example.demo.service;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * AccountService
 * 
 * Business logic layer for Account entity.
 * Implements account management operations with user access control.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR004: Account Filter Validation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new account
     * Implements BR004: Account Filter Validation
     * 
     * @param request The create account request
     * @return The created account response
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());

        // Validate account ID format (BR004)
        validateAccountId(request.getAccountId());

        // Check if account already exists
        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Account with ID already exists: " + request.getAccountId());
        }

        Account account = new Account();
        account.setAccountId(request.getAccountId());

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        
        return convertToResponse(savedAccount);
    }

    /**
     * Retrieves an account by ID
     * 
     * @param accountId The account ID
     * @return Optional containing the account response if found
     */
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String accountId) {
        log.info("Retrieving account by ID: {}", accountId);
        return accountRepository.findByAccountId(accountId).map(this::convertToResponse);
    }

    /**
     * Retrieves all accounts with pagination
     * 
     * @param pageable Pagination information
     * @return Page of account responses
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * Updates an existing account
     * Implements BR004: Account Filter Validation
     * 
     * @param accountId The account ID to update
     * @param request The update account request
     * @return The updated account response
     */
    @Transactional
    public AccountResponseDto updateAccount(String accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        if (request.getAccountId() != null) {
            validateAccountId(request.getAccountId());
            account.setAccountId(request.getAccountId());
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        
        return convertToResponse(updatedAccount);
    }

    /**
     * Deletes an account
     * 
     * @param accountId The account ID to delete
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
     * Retrieves accounts accessible by a specific user
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @param pageable Pagination information
     * @return Page of accounts accessible by the user
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsAccessibleByUser(String userId, Pageable pageable) {
        log.info("Retrieving accounts accessible by user: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // BR001: Admin users can view all accounts
        if (user.isAdmin()) {
            log.info("Admin user - returning all accounts");
            return accountRepository.findAll(pageable).map(this::convertToResponse);
        }

        // BR001: Regular users can only view their accessible accounts
        log.info("Regular user - returning accessible accounts only");
        return accountRepository.findAccountsAccessibleByUser(userId, pageable).map(this::convertToResponse);
    }

    /**
     * Grants a user access to an account
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @param accountId The account ID
     */
    @Transactional
    public void grantUserAccessToAccount(String userId, String accountId) {
        log.info("Granting user {} access to account {}", userId, accountId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        account.grantAccessToUser(user);
        accountRepository.save(account);
        
        log.info("Access granted successfully");
    }

    /**
     * Revokes a user's access to an account
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @param accountId The account ID
     */
    @Transactional
    public void revokeUserAccessFromAccount(String userId, String accountId) {
        log.info("Revoking user {} access from account {}", userId, accountId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        account.revokeAccessFromUser(user);
        accountRepository.save(account);
        
        log.info("Access revoked successfully");
    }

    /**
     * Checks if a user has access to an account
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @param accountId The account ID
     * @return true if user has access, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean userHasAccessToAccount(String userId, String accountId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // BR001: Admin users have access to all accounts
        if (user.isAdmin()) {
            return true;
        }

        // BR001: Check if regular user has explicit access
        return accountRepository.userHasAccessToAccount(userId, accountId);
    }

    /**
     * Validates account ID format
     * Implements BR004: Account Filter Validation
     * 
     * @param accountId The account ID to validate
     */
    private void validateAccountId(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        String trimmedAccountId = accountId.trim();

        // BR004: Cannot be blank, spaces, or zeros
        if (trimmedAccountId.matches("^0+$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        // BR004: Must be numeric and exactly 11 digits
        if (!trimmedAccountId.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }
    }

    /**
     * Converts Account entity to AccountResponseDto
     * 
     * @param account The account entity
     * @return The account response DTO
     */
    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setCreditCardCount(account.getCreditCards() != null ? account.getCreditCards().size() : 0);
        response.setUserAccessCount(account.getUsers() != null ? account.getUsers().size() : 0);
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
