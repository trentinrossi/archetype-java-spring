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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Account business logic.
 * Implements all business rules for account management including:
 * - BR-001: Account File Sequential Processing
 * - BR-002: Account Record Display
 * - BR-004: End-of-File Detection
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    /**
     * Create a new account.
     * Validates that the account ID is unique and meets the 11-digit requirement.
     * 
     * @param request the account creation request
     * @return the created account response
     * @throws IllegalArgumentException if account ID already exists or is invalid
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAcctId());
        
        // Validate account ID is 11 digits
        if (!isValidAccountId(request.getAcctId())) {
            log.error("Invalid account ID: {}. Must be 11 digits.", request.getAcctId());
            throw new IllegalArgumentException("Account ID must be an 11-digit numeric value");
        }
        
        // Check if account already exists
        if (accountRepository.existsByAcctId(request.getAcctId())) {
            log.error("Account with ID {} already exists", request.getAcctId());
            throw new IllegalArgumentException("Account with ID " + request.getAcctId() + " already exists");
        }
        
        // Validate business rules
        validateAccountBusinessRules(request);
        
        // Create account entity
        Account account = new Account();
        account.setAcctId(request.getAcctId());
        account.setAcctActiveStatus(request.getAcctActiveStatus());
        account.setAcctCurrBal(request.getAcctCurrBal());
        account.setAcctCreditLimit(request.getAcctCreditLimit());
        account.setAcctCashCreditLimit(request.getAcctCashCreditLimit());
        account.setAcctOpenDate(request.getAcctOpenDate());
        account.setAcctExpirationDate(request.getAcctExpirationDate());
        account.setAcctReissueDate(request.getAcctReissueDate());
        account.setAcctCurrCycCredit(request.getAcctCurrCycCredit());
        account.setAcctCurrCycDebit(request.getAcctCurrCycDebit());
        account.setAcctGroupId(request.getAcctGroupId());
        
        Account savedAccount = accountRepository.save(account);
        log.info("Successfully created account with ID: {}", savedAccount.getAcctId());
        
        return convertToResponse(savedAccount);
    }
    
    /**
     * Get account by ID.
     * Supports BR-002: Account Record Display
     * 
     * @param acctId the account identifier
     * @return Optional containing the account response if found
     */
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long acctId) {
        log.info("Retrieving account with ID: {}", acctId);
        return accountRepository.findByAcctId(acctId).map(this::convertToResponse);
    }
    
    /**
     * Update an existing account.
     * Only updates fields that are provided (non-null) in the request.
     * 
     * @param acctId the account identifier
     * @param request the update request
     * @return the updated account response
     * @throws IllegalArgumentException if account not found
     */
    @Transactional
    public AccountResponseDto updateAccount(Long acctId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> {
                    log.error("Account with ID {} not found", acctId);
                    return new IllegalArgumentException("Account with ID " + acctId + " not found");
                });
        
        // Update only provided fields
        if (request.getAcctActiveStatus() != null) {
            account.setAcctActiveStatus(request.getAcctActiveStatus());
        }
        if (request.getAcctCurrBal() != null) {
            account.setAcctCurrBal(request.getAcctCurrBal());
        }
        if (request.getAcctCreditLimit() != null) {
            account.setAcctCreditLimit(request.getAcctCreditLimit());
        }
        if (request.getAcctCashCreditLimit() != null) {
            account.setAcctCashCreditLimit(request.getAcctCashCreditLimit());
        }
        if (request.getAcctOpenDate() != null) {
            account.setAcctOpenDate(request.getAcctOpenDate());
        }
        if (request.getAcctExpirationDate() != null) {
            account.setAcctExpirationDate(request.getAcctExpirationDate());
        }
        if (request.getAcctReissueDate() != null) {
            account.setAcctReissueDate(request.getAcctReissueDate());
        }
        if (request.getAcctCurrCycCredit() != null) {
            account.setAcctCurrCycCredit(request.getAcctCurrCycCredit());
        }
        if (request.getAcctCurrCycDebit() != null) {
            account.setAcctCurrCycDebit(request.getAcctCurrCycDebit());
        }
        if (request.getAcctGroupId() != null) {
            account.setAcctGroupId(request.getAcctGroupId());
        }
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Successfully updated account with ID: {}", acctId);
        
        return convertToResponse(updatedAccount);
    }
    
    /**
     * Delete an account.
     * 
     * @param acctId the account identifier
     * @throws IllegalArgumentException if account not found
     */
    @Transactional
    public void deleteAccount(Long acctId) {
        log.info("Deleting account with ID: {}", acctId);
        
        if (!accountRepository.existsByAcctId(acctId)) {
            log.error("Account with ID {} not found", acctId);
            throw new IllegalArgumentException("Account with ID " + acctId + " not found");
        }
        
        accountRepository.deleteById(acctId);
        log.info("Successfully deleted account with ID: {}", acctId);
    }
    
    /**
     * Get all accounts with pagination.
     * Supports BR-002: Account Record Display
     * 
     * @param pageable pagination information
     * @return page of account responses
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination: {}", pageable);
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    /**
     * Process all accounts sequentially.
     * Implements BR-001: Account File Sequential Processing
     * Implements BR-004: End-of-File Detection
     * 
     * @return list of all account responses in sequential order
     */
    @Transactional(readOnly = true)
    public List<AccountResponseDto> processAccountsSequentially() {
        log.info("Starting sequential processing of all accounts");
        
        List<Account> accounts = accountRepository.findAllAccountsSequentially();
        
        if (accounts.isEmpty()) {
            log.info("No accounts found. End-of-file reached.");
            return List.of();
        }
        
        log.info("Processing {} accounts sequentially", accounts.size());
        
        List<AccountResponseDto> responses = accounts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        log.info("Successfully processed {} accounts. End-of-file reached.", responses.size());
        
        return responses;
    }
    
    /**
     * Get all active accounts.
     * 
     * @param pageable pagination information
     * @return page of active account responses
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getActiveAccounts(Pageable pageable) {
        log.info("Retrieving active accounts");
        return accountRepository.findAllActiveAccounts(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get all inactive accounts.
     * 
     * @param pageable pagination information
     * @return page of inactive account responses
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getInactiveAccounts(Pageable pageable) {
        log.info("Retrieving inactive accounts");
        return accountRepository.findAllInactiveAccounts(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get accounts by group ID.
     * 
     * @param groupId the group identifier
     * @param pageable pagination information
     * @return page of account responses in the group
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsByGroupId(String groupId, Pageable pageable) {
        log.info("Retrieving accounts for group ID: {}", groupId);
        return accountRepository.findByAcctGroupId(groupId, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get accounts expiring before a specific date.
     * 
     * @param expirationDate the cutoff date
     * @param pageable pagination information
     * @return page of expiring account responses
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsExpiringBefore(LocalDate expirationDate, Pageable pageable) {
        log.info("Retrieving accounts expiring before: {}", expirationDate);
        return accountRepository.findAccountsExpiringBefore(expirationDate, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get accounts over their credit limit.
     * 
     * @param pageable pagination information
     * @return page of account responses over credit limit
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsOverCreditLimit(Pageable pageable) {
        log.info("Retrieving accounts over credit limit");
        return accountRepository.findAccountsOverCreditLimit(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get total balance across all accounts.
     * 
     * @return total balance
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalBalance() {
        log.info("Calculating total balance across all accounts");
        BigDecimal total = accountRepository.calculateTotalBalance();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * Get total balance for active accounts.
     * 
     * @return total active balance
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalActiveBalance() {
        log.info("Calculating total balance for active accounts");
        BigDecimal total = accountRepository.calculateTotalActiveBalance();
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * Get count of active accounts.
     * 
     * @return count of active accounts
     */
    @Transactional(readOnly = true)
    public long getActiveAccountCount() {
        log.info("Counting active accounts");
        return accountRepository.countActiveAccounts();
    }
    
    /**
     * Get count of inactive accounts.
     * 
     * @return count of inactive accounts
     */
    @Transactional(readOnly = true)
    public long getInactiveAccountCount() {
        log.info("Counting inactive accounts");
        return accountRepository.countInactiveAccounts();
    }
    
    /**
     * Convert Account entity to AccountResponseDto.
     * Implements BR-002: Account Record Display - displays all fields
     * 
     * @param account the account entity
     * @return the account response DTO
     */
    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAcctId(account.getAcctId());
        response.setAcctActiveStatus(account.getAcctActiveStatus());
        response.setAcctActiveStatusDisplay(account.isActive() ? "Active" : "Inactive");
        response.setIsActive(account.isActive());
        response.setAcctCurrBal(account.getAcctCurrBal());
        response.setAcctCreditLimit(account.getAcctCreditLimit());
        response.setAcctCashCreditLimit(account.getAcctCashCreditLimit());
        response.setAvailableCredit(account.getAvailableCredit());
        response.setAvailableCashCredit(account.getAvailableCashCredit());
        response.setAcctOpenDate(account.getAcctOpenDate());
        response.setAcctExpirationDate(account.getAcctExpirationDate());
        response.setIsExpired(account.isExpired());
        response.setAcctReissueDate(account.getAcctReissueDate());
        response.setAcctCurrCycCredit(account.getAcctCurrCycCredit());
        response.setAcctCurrCycDebit(account.getAcctCurrCycDebit());
        response.setNetCycleAmount(account.getNetCycleAmount());
        response.setAcctGroupId(account.getAcctGroupId());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
    
    /**
     * Validate that account ID is an 11-digit numeric value.
     * 
     * @param acctId the account identifier
     * @return true if valid, false otherwise
     */
    private boolean isValidAccountId(Long acctId) {
        if (acctId == null || acctId <= 0) {
            return false;
        }
        String idStr = String.valueOf(acctId);
        return idStr.length() == 11;
    }
    
    /**
     * Validate business rules for account creation.
     * 
     * @param request the account creation request
     * @throws IllegalArgumentException if validation fails
     */
    private void validateAccountBusinessRules(CreateAccountRequestDto request) {
        // Validate expiration date is after open date
        if (request.getAcctExpirationDate().isBefore(request.getAcctOpenDate())) {
            throw new IllegalArgumentException("Account expiration date must be after open date");
        }
        
        // Validate reissue date if provided
        if (request.getAcctReissueDate() != null) {
            if (request.getAcctReissueDate().isBefore(request.getAcctOpenDate())) {
                throw new IllegalArgumentException("Account reissue date must be after open date");
            }
        }
        
        // Validate cash credit limit does not exceed credit limit
        if (request.getAcctCashCreditLimit().compareTo(request.getAcctCreditLimit()) > 0) {
            throw new IllegalArgumentException("Cash credit limit cannot exceed credit limit");
        }
        
        // Validate current balance does not exceed credit limit for new accounts
        if (request.getAcctCurrBal().compareTo(request.getAcctCreditLimit()) > 0) {
            log.warn("Account {} has balance exceeding credit limit", request.getAcctId());
        }
    }
}
