package com.example.demo.service;

import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.dto.AccountResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    
    private static final String ACTIVE_STATUS = "A";
    private static final String INACTIVE_STATUS = "I";
    private static final int FILE_STATUS_SUCCESS = 0;
    private static final int FILE_STATUS_ERROR = 12;
    private static final int FILE_STATUS_EOF = 16;
    private static final String EOF_FLAG = "Y";

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAcctId());
        
        validateAccountId(request.getAcctId());
        
        if (accountRepository.existsByAcctId(request.getAcctId())) {
            throw new IllegalArgumentException("Account with ID already exists");
        }
        
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
        log.info("Account created successfully with ID: {}", savedAccount.getAcctId());
        
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String acctId) {
        log.info("Retrieving account with ID: {}", acctId);
        
        validateAccountId(acctId);
        
        return accountRepository.findByAcctId(acctId).map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(String acctId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", acctId);
        
        validateAccountId(acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
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
        log.info("Account updated successfully with ID: {}", updatedAccount.getAcctId());
        
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(String acctId) {
        log.info("Deleting account with ID: {}", acctId);
        
        validateAccountId(acctId);
        
        if (!accountRepository.existsByAcctId(acctId)) {
            throw new IllegalArgumentException("Account not found");
        }
        
        accountRepository.deleteById(acctId);
        log.info("Account deleted successfully with ID: {}", acctId);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDto> getAllAccountsSequentially() {
        log.info("BR-001: Starting sequential account record processing");
        
        int fileStatus = openAccountFileForInput();
        
        if (fileStatus != FILE_STATUS_SUCCESS) {
            log.error("BR-003: Account file access failed with status: {}", fileStatus);
            throw new IllegalStateException("Account file could not be opened. File status: " + fileStatus);
        }
        
        log.info("BR-003: Account file opened successfully with status: {}", FILE_STATUS_SUCCESS);
        
        List<AccountResponseDto> accountList = new ArrayList<>();
        List<Account> accounts = accountRepository.findAllOrderedByAcctId();
        
        boolean endOfFile = false;
        int recordCount = 0;
        
        for (Account account : accounts) {
            if (endOfFile) {
                break;
            }
            
            recordCount++;
            log.info("BR-001: Processing account record #{}: {}", recordCount, account.getAcctId());
            
            AccountResponseDto accountResponse = convertToResponse(account);
            displayAllAccountInformation(accountResponse);
            
            accountList.add(accountResponse);
        }
        
        if (accounts.isEmpty() || recordCount == accounts.size()) {
            endOfFile = detectEndOfFile();
            log.info("BR-004: End of file detected. Total records processed: {}", recordCount);
        }
        
        log.info("BR-001: Sequential processing completed. Total records: {}", accountList.size());
        
        return accountList;
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDto> getActiveAccounts() {
        log.info("Retrieving active accounts");
        
        List<Account> activeAccounts = accountRepository.findByAcctActiveStatus(ACTIVE_STATUS);
        
        return activeAccounts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDto> getExpiredAccounts() {
        log.info("Retrieving expired accounts");
        
        LocalDate currentDate = LocalDate.now();
        List<Account> expiredAccounts = accountRepository.findByAcctExpirationDateBefore(currentDate);
        
        return expiredAccounts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void validateAccountId(String acctId) {
        log.debug("Validating account ID: {}", acctId);
        
        if (acctId == null || acctId.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Account ID format");
        }
        
        String cleanedAcctId = acctId.trim();
        
        if (!cleanedAcctId.matches("\\d{11}")) {
            throw new IllegalArgumentException("Invalid Account ID format");
        }
        
        log.debug("Account ID validation successful: {}", acctId);
    }

    public boolean checkAccountExists(String acctId) {
        log.debug("Checking if account exists: {}", acctId);
        
        validateAccountId(acctId);
        
        boolean exists = accountRepository.existsByAcctId(acctId);
        log.debug("Account exists check result for {}: {}", acctId, exists);
        
        return exists;
    }

    public void processAccountSequentially(String acctId) {
        log.info("BR-001: Processing account sequentially: {}", acctId);
        
        validateAccountId(acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        AccountResponseDto accountResponse = convertToResponse(account);
        displayAllAccountInformation(accountResponse);
        
        log.info("BR-001: Account processed successfully: {}", acctId);
    }

    public void displayAllAccountInformation(AccountResponseDto accountResponse) {
        log.info("BR-002: Displaying all account information");
        log.info("========================================");
        log.info("ACCT-ID: {}", accountResponse.getAcctId());
        log.info("ACCT-ACTIVE-STATUS: {}", accountResponse.getAcctActiveStatus());
        log.info("ACCT-CURR-BAL: {}", accountResponse.getAcctCurrBal());
        log.info("ACCT-CREDIT-LIMIT: {}", accountResponse.getAcctCreditLimit());
        log.info("ACCT-CASH-CREDIT-LIMIT: {}", accountResponse.getAcctCashCreditLimit());
        log.info("ACCT-OPEN-DATE: {}", accountResponse.getAcctOpenDate());
        log.info("ACCT-EXPIRATION-DATE: {}", accountResponse.getAcctExpirationDate());
        log.info("ACCT-REISSUE-DATE: {}", accountResponse.getAcctReissueDate());
        log.info("ACCT-CURR-CYC-CREDIT: {}", accountResponse.getAcctCurrCycCredit());
        log.info("ACCT-CURR-CYC-DEBIT: {}", accountResponse.getAcctCurrCycDebit());
        log.info("ACCT-GROUP-ID: {}", accountResponse.getAcctGroupId());
        log.info("========================================");
        log.info("BR-002: Account information display completed");
    }

    private int openAccountFileForInput() {
        log.info("BR-003: Opening account file for input");
        
        try {
            long accountCount = accountRepository.count();
            
            if (accountCount >= 0) {
                log.info("BR-003: Account file opened successfully. File status: 00");
                return FILE_STATUS_SUCCESS;
            } else {
                log.error("BR-003: Account file open failed. File status: 12");
                return FILE_STATUS_ERROR;
            }
        } catch (Exception e) {
            log.error("BR-003: Account file open failed with exception. File status: 12", e);
            return FILE_STATUS_ERROR;
        }
    }

    private boolean detectEndOfFile() {
        log.info("BR-004: Detecting end of file condition");
        log.info("BR-004: Setting application result to: {}", FILE_STATUS_EOF);
        log.info("BR-004: Setting END-OF-FILE flag to: {}", EOF_FLAG);
        log.info("BR-004: Stopping record processing");
        
        return true;
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAcctId(account.getAcctId());
        response.setAcctActiveStatus(account.getAcctActiveStatus());
        response.setAcctCurrBal(account.getAcctCurrBal());
        response.setAcctCreditLimit(account.getAcctCreditLimit());
        response.setAcctCashCreditLimit(account.getAcctCashCreditLimit());
        response.setAcctOpenDate(account.getAcctOpenDate());
        response.setAcctExpirationDate(account.getAcctExpirationDate());
        response.setAcctReissueDate(account.getAcctReissueDate());
        response.setAcctCurrCycCredit(account.getAcctCurrCycCredit());
        response.setAcctCurrCycDebit(account.getAcctCurrCycDebit());
        response.setAcctGroupId(account.getAcctGroupId());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        
        // Compute derived fields
        response.computeDerivedFields();
        
        return response;
    }
}
