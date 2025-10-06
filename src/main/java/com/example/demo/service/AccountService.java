package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.dto.*;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating new account with ID: {}", request.getAcctId());
        
        // Validate account doesn't already exist
        if (accountRepository.existsByAcctId(request.getAcctId())) {
            throw new IllegalArgumentException("Account with ID " + request.getAcctId() + " already exists");
        }
        
        // Create account entity
        Account account = new Account();
        account.setAcctId(request.getAcctId());
        account.setAcctActiveStatus(request.getAcctActiveStatus());
        account.setAcctCurrBal(request.getAcctCurrBal() != null ? request.getAcctCurrBal() : BigDecimal.ZERO);
        account.setAcctCreditLimit(request.getAcctCreditLimit() != null ? request.getAcctCreditLimit() : BigDecimal.ZERO);
        account.setAcctCashCreditLimit(request.getAcctCashCreditLimit() != null ? request.getAcctCashCreditLimit() : BigDecimal.ZERO);
        account.setAcctOpenDate(parseDate(request.getAcctOpenDate()));
        account.setAcctExpirationDate(parseDate(request.getAcctExpirationDate()));
        account.setAcctReissueDate(request.getAcctReissueDate() != null ? parseDate(request.getAcctReissueDate()) : null);
        account.setAcctCurrCycCredit(request.getAcctCurrCycCredit() != null ? request.getAcctCurrCycCredit() : BigDecimal.ZERO);
        account.setAcctCurrCycDebit(request.getAcctCurrCycDebit() != null ? request.getAcctCurrCycDebit() : BigDecimal.ZERO);
        account.setAcctGroupId(request.getAcctGroupId());
        
        Account savedAccount = accountRepository.save(account);
        
        log.info("Account created successfully: {}", savedAccount.getAcctId());
        return convertToResponse(savedAccount);
    }
    
    @Transactional(readOnly = true)
    public Optional<AccountResponse> getAccountById(String acctId) {
        log.info("Retrieving account by ID: {}", acctId);
        
        return accountRepository.findByAcctId(acctId)
                .map(this::convertToResponse);
    }
    
    @Transactional
    public AccountResponse updateAccount(String acctId, UpdateAccountRequest request) {
        log.info("Updating account with ID: {}", acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + acctId));
        
        // Update fields if provided
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
        if (request.getAcctExpirationDate() != null) {
            account.setAcctExpirationDate(parseDate(request.getAcctExpirationDate()));
        }
        if (request.getAcctReissueDate() != null) {
            account.setAcctReissueDate(parseDate(request.getAcctReissueDate()));
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
        
        log.info("Account updated successfully: {}", updatedAccount.getAcctId());
        return convertToResponse(updatedAccount);
    }
    
    @Transactional
    public void deleteAccount(String acctId) {
        log.info("Deleting account with ID: {}", acctId);
        
        if (!accountRepository.existsByAcctId(acctId)) {
            throw new IllegalArgumentException("Account not found: " + acctId);
        }
        
        accountRepository.deleteById(acctId);
        log.info("Account deleted successfully: {}", acctId);
    }
    
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        
        return accountRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccountsByStatus(String status, Pageable pageable) {
        log.info("Retrieving accounts by status: {}", status);
        
        return accountRepository.findByAcctActiveStatus(status, pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAccountsByGroup(String groupId, Pageable pageable) {
        log.info("Retrieving accounts by group: {}", groupId);
        
        return accountRepository.findByAcctGroupId(groupId, pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public AccountSequentialReadResponse sequentialReadAccounts(AccountSequentialReadRequest request) {
        log.info("Sequential read starting from account: {} with {} records", 
                request.getStartingAcctId(), 
                request.getRecordCount() != null ? request.getRecordCount() : 10);
        
        String startingAcctId = request.getStartingAcctId() != null ? request.getStartingAcctId() : "00000000000";
        int recordCount = request.getRecordCount() != null ? request.getRecordCount() : 10;
        
        Pageable pageable = PageRequest.of(0, recordCount + 1, Sort.by("acctId"));
        List<Account> accounts;
        
        // Apply filters based on request
        if (request.getStatusFilter() != null && request.getGroupIdFilter() != null) {
            accounts = accountRepository.findAccountsSequentiallyByStatusAndGroup(
                    startingAcctId, request.getStatusFilter(), request.getGroupIdFilter(), pageable);
        } else if (request.getStatusFilter() != null) {
            accounts = accountRepository.findAccountsSequentiallyByStatus(
                    startingAcctId, request.getStatusFilter(), pageable);
        } else if (request.getGroupIdFilter() != null) {
            accounts = accountRepository.findAccountsSequentiallyByGroup(
                    startingAcctId, request.getGroupIdFilter(), pageable);
        } else {
            accounts = accountRepository.findAccountsSequentiallyFromStarting(startingAcctId, pageable);
        }
        
        // Apply additional filters
        if (Boolean.FALSE.equals(request.getIncludeInactive())) {
            accounts = accounts.stream()
                    .filter(Account::isActive)
                    .collect(Collectors.toList());
        }
        
        if (Boolean.FALSE.equals(request.getIncludeExpired())) {
            accounts = accounts.stream()
                    .filter(account -> !account.isExpired())
                    .collect(Collectors.toList());
        }
        
        // Determine if there are more records
        boolean hasMoreRecords = accounts.size() > recordCount;
        if (hasMoreRecords) {
            accounts = accounts.subList(0, recordCount);
        }
        
        // Convert to response DTOs
        List<AccountResponse> accountResponses = accounts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        // Build response
        AccountSequentialReadResponse response = new AccountSequentialReadResponse();
        response.setStartingAcctId(startingAcctId);
        response.setRecordsRequested(recordCount);
        response.setRecordsReturned(accountResponses.size());
        response.setHasMoreRecords(hasMoreRecords);
        response.setNextAcctId(hasMoreRecords && !accounts.isEmpty() ? 
                getNextAccountId(accounts.get(accounts.size() - 1).getAcctId()) : null);
        response.setAccounts(accountResponses);
        response.setTotalMatchingAccounts(getTotalMatchingAccounts(request, startingAcctId));
        response.setAppliedFilters(buildAppliedFiltersString(request));
        
        return response;
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getActiveAccounts() {
        log.info("Retrieving all active accounts");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("acctId"));
        return accountRepository.findActiveAccounts(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getInactiveAccounts() {
        log.info("Retrieving all inactive accounts");
        
        Pageable pageable = PageRequest.of(0, 1000, Sort.by("acctId"));
        return accountRepository.findInactiveAccounts(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getOverLimitAccounts() {
        log.info("Retrieving accounts over credit limit");
        
        return accountRepository.findOverLimitAccounts()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getOverCashLimitAccounts() {
        log.info("Retrieving accounts over cash credit limit");
        
        return accountRepository.findOverCashLimitAccounts()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getExpiringAccounts(LocalDate beforeDate) {
        log.info("Retrieving accounts expiring before: {}", beforeDate);
        
        return accountRepository.findExpiringAccounts(beforeDate)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getRecentlyReissuedAccounts(LocalDate since) {
        log.info("Retrieving accounts reissued since: {}", since);
        
        return accountRepository.findRecentlyReissuedAccounts(since)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public void openAccountFile() {
        log.info("Opening account file for sequential processing - CBACT01C functionality");
        // Simulated file opening operation based on CBACT01C program
        // In the original COBOL program, this would open the VSAM file
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> readAccountsSequentially(int recordCount) {
        log.info("Reading {} accounts sequentially - CBACT01C functionality", recordCount);
        
        Pageable pageable = PageRequest.of(0, recordCount, Sort.by("acctId"));
        return accountRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public void closeAccountFile() {
        log.info("Closing account file after sequential processing - CBACT01C functionality");
        // Simulated file closing operation based on CBACT01C program
        // In the original COBOL program, this would close the VSAM file
    }
    
    @Transactional(readOnly = true)
    public void displayAccountRecord(AccountResponse account) {
        log.info("Displaying account record - CBACT01C functionality");
        log.info("ACCT-ID                 : {}", account.getAcctId());
        log.info("ACCT-ACTIVE-STATUS      : {}", account.getAcctActiveStatus());
        log.info("ACCT-CURR-BAL           : {}", account.getAcctCurrBal());
        log.info("ACCT-CREDIT-LIMIT       : {}", account.getAcctCreditLimit());
        log.info("ACCT-CASH-CREDIT-LIMIT  : {}", account.getAcctCashCreditLimit());
        log.info("ACCT-OPEN-DATE          : {}", account.getAcctOpenDate());
        log.info("ACCT-EXPIRATION-DATE    : {}", account.getAcctExpirationDate());
        log.info("ACCT-REISSUE-DATE       : {}", account.getAcctReissueDate());
        log.info("ACCT-CURR-CYC-CREDIT    : {}", account.getAcctCurrCycCredit());
        log.info("ACCT-CURR-CYC-DEBIT     : {}", account.getAcctCurrCycDebit());
        log.info("ACCT-GROUP-ID           : {}", account.getAcctGroupId());
        log.info("-------------------------------------------------");
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctStatuses() {
        return accountRepository.findDistinctActiveStatuses();
    }
    
    @Transactional(readOnly = true)
    public List<String> getDistinctGroupIds() {
        return accountRepository.findDistinctGroupIds();
    }
    
    @Transactional(readOnly = true)
    public long countAccountsByStatus(String status) {
        return accountRepository.countByAcctActiveStatus(status);
    }
    
    @Transactional(readOnly = true)
    public long countAccountsByGroup(String groupId) {
        return accountRepository.countByAcctGroupId(groupId);
    }
    
    private AccountResponse convertToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAcctId(account.getAcctId());
        response.setAcctActiveStatus(account.getAcctActiveStatus());
        response.setAccountStatusDescription(account.getAccountStatusDescription());
        response.setAcctCurrBal(account.getAcctCurrBal());
        response.setAcctCreditLimit(account.getAcctCreditLimit());
        response.setAcctCashCreditLimit(account.getAcctCashCreditLimit());
        response.setAvailableCredit(account.getAvailableCredit());
        response.setAvailableCashCredit(account.getAvailableCashCredit());
        response.setAcctOpenDate(formatDate(account.getAcctOpenDate()));
        response.setAcctExpirationDate(formatDate(account.getAcctExpirationDate()));
        response.setAcctReissueDate(account.getAcctReissueDate() != null ? formatDate(account.getAcctReissueDate()) : null);
        response.setAcctCurrCycCredit(account.getAcctCurrCycCredit());
        response.setAcctCurrCycDebit(account.getAcctCurrCycDebit());
        response.setCycleNetAmount(account.getCycleNetAmount());
        response.setAcctGroupId(account.getAcctGroupId());
        response.setIsActive(account.isActive());
        response.setIsExpired(account.isExpired());
        response.setIsOverLimit(account.isOverLimit());
        response.setIsOverCashLimit(account.isOverCashLimit());
        response.setHasRecentReissue(account.hasRecentReissue());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        
        return response;
    }
    
    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("Failed to parse date: {}", dateString);
            return null;
        }
    }
    
    private String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        
        return date.format(DATE_FORMATTER);
    }
    
    private String getNextAccountId(String currentAcctId) {
        // Simple increment logic for account ID
        try {
            long id = Long.parseLong(currentAcctId);
            return String.format("%011d", id + 1);
        } catch (NumberFormatException e) {
            return currentAcctId;
        }
    }
    
    private Long getTotalMatchingAccounts(AccountSequentialReadRequest request, String startingAcctId) {
        if (request.getStatusFilter() != null && request.getGroupIdFilter() != null) {
            return accountRepository.countByAcctActiveStatusAndAcctGroupId(
                    request.getStatusFilter(), request.getGroupIdFilter());
        } else if (request.getStatusFilter() != null) {
            return accountRepository.countAccountsFromStartingByStatus(startingAcctId, request.getStatusFilter());
        } else if (request.getGroupIdFilter() != null) {
            return accountRepository.countAccountsFromStartingByGroup(startingAcctId, request.getGroupIdFilter());
        } else {
            return accountRepository.countAccountsFromStarting(startingAcctId);
        }
    }
    
    private String buildAppliedFiltersString(AccountSequentialReadRequest request) {
        List<String> filters = new ArrayList<>();
        
        if (request.getStatusFilter() != null) {
            filters.add("Status: " + request.getStatusFilter());
        }
        if (request.getGroupIdFilter() != null) {
            filters.add("Group: " + request.getGroupIdFilter());
        }
        if (Boolean.FALSE.equals(request.getIncludeInactive())) {
            filters.add("Active Only");
        }
        if (Boolean.FALSE.equals(request.getIncludeExpired())) {
            filters.add("Non-Expired Only");
        }
        
        return filters.isEmpty() ? "No filters applied" : String.join(", ", filters);
    }
}