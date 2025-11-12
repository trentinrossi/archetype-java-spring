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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private static final String DEFAULT_GROUP_ID = "DEFAULT";
    private static final int MONTHS_IN_YEAR = 1200;
    
    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        
        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Account with ID already exists");
        }
        
        Account account = new Account();
        account.setAcctId(request.getAccountId());
        account.setAccountId(request.getAccountId());
        account.setXrefAcctId(request.getAccountId());
        account.setAcctActiveStatus(request.getActiveStatus());
        account.setActiveStatus(request.getActiveStatus());
        account.setAccountStatus(request.getActiveStatus());
        account.setAcctCurrBal(request.getCurrentBalance());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setAcctCreditLimit(request.getCreditLimit());
        account.setCreditLimit(request.getCreditLimit());
        account.setAcctCashCreditLimit(request.getCashCreditLimit());
        account.setCashCreditLimit(request.getCashCreditLimit());
        account.setAcctOpenDate(request.getOpenDate());
        account.setOpenDate(request.getOpenDate());
        account.setAcctExpirationDate(request.getExpirationDate());
        account.setExpirationDate(request.getExpirationDate());
        account.setAcctReissueDate(request.getReissueDate());
        account.setReissueDate(request.getReissueDate());
        account.setAcctCurrCycCredit(request.getCurrentCycleCredit());
        account.setCurrentCycleCredit(request.getCurrentCycleCredit());
        account.setAcctCurrCycDebit(request.getCurrentCycleDebit());
        account.setCurrentCycleDebit(request.getCurrentCycleDebit());
        account.setAcctGroupId(request.getGroupId());
        account.setGroupId(request.getGroupId());
        account.setAccountData(request.getAccountData());
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String accountId) {
        log.info("Retrieving account with ID: {}", accountId);
        return accountRepository.findByAccountId(accountId).map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(String accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (request.getActiveStatus() != null) {
            account.setAcctActiveStatus(request.getActiveStatus());
            account.setActiveStatus(request.getActiveStatus());
            account.setAccountStatus(request.getActiveStatus());
        }
        
        if (request.getCurrentBalance() != null) {
            account.setAcctCurrBal(request.getCurrentBalance());
            account.setCurrentBalance(request.getCurrentBalance());
        }
        
        if (request.getCreditLimit() != null) {
            account.setAcctCreditLimit(request.getCreditLimit());
            account.setCreditLimit(request.getCreditLimit());
        }
        
        if (request.getCashCreditLimit() != null) {
            account.setAcctCashCreditLimit(request.getCashCreditLimit());
            account.setCashCreditLimit(request.getCashCreditLimit());
        }
        
        if (request.getExpirationDate() != null) {
            account.setAcctExpirationDate(request.getExpirationDate());
            account.setExpirationDate(request.getExpirationDate());
        }
        
        if (request.getReissueDate() != null) {
            account.setAcctReissueDate(request.getReissueDate());
            account.setReissueDate(request.getReissueDate());
        }
        
        if (request.getCurrentCycleCredit() != null) {
            account.setAcctCurrCycCredit(request.getCurrentCycleCredit());
            account.setCurrentCycleCredit(request.getCurrentCycleCredit());
        }
        
        if (request.getCurrentCycleDebit() != null) {
            account.setAcctCurrCycDebit(request.getCurrentCycleDebit());
            account.setCurrentCycleDebit(request.getCurrentCycleDebit());
        }
        
        if (request.getGroupId() != null) {
            account.setAcctGroupId(request.getGroupId());
            account.setGroupId(request.getGroupId());
        }
        
        if (request.getAccountData() != null) {
            account.setAccountData(request.getAccountData());
        }
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(String accountId) {
        log.info("Deleting account with ID: {}", accountId);
        
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new IllegalArgumentException("Account not found");
        }
        
        accountRepository.deleteById(accountId);
        log.info("Account deleted successfully with ID: {}", accountId);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDto> processAllAccountsSequentially() {
        log.info("Starting sequential account record processing (BR-001)");
        
        List<AccountResponseDto> processedAccounts = new ArrayList<>();
        List<Account> allAccounts = accountRepository.findAllOrderedByAccountIdAsc();
        
        for (Account account : allAccounts) {
            AccountResponseDto response = convertToResponse(account);
            processedAccounts.add(response);
            displayAccountData(account);
        }
        
        log.info("Sequential account record processing completed. Total records processed: {}", processedAccounts.size());
        return processedAccounts;
    }

    @Transactional(readOnly = true)
    public void displayAccountData(Account account) {
        log.info("========================================");
        log.info("ACCT-ID: {}", account.getAcctId());
        log.info("ACCT-ACTIVE-STATUS: {}", account.getAcctActiveStatus());
        log.info("ACCT-CURR-BAL: {}", account.getAcctCurrBal());
        log.info("ACCT-CREDIT-LIMIT: {}", account.getAcctCreditLimit());
        log.info("ACCT-CASH-CREDIT-LIMIT: {}", account.getAcctCashCreditLimit());
        log.info("ACCT-OPEN-DATE: {}", account.getAcctOpenDate());
        log.info("ACCT-EXPIRATION-DATE: {}", account.getAcctExpirationDate());
        log.info("ACCT-REISSUE-DATE: {}", account.getAcctReissueDate());
        log.info("ACCT-CURR-CYC-CREDIT: {}", account.getAcctCurrCycCredit());
        log.info("ACCT-CURR-CYC-DEBIT: {}", account.getAcctCurrCycDebit());
        log.info("ACCT-GROUP-ID: {}", account.getAcctGroupId());
        log.info("========================================");
    }

    @Transactional
    public BigDecimal calculateInterestByTransactionCategory(String accountId, String categoryCode, 
                                                             String transactionTypeCode, BigDecimal categoryBalance) {
        log.info("Calculating interest for account: {}, category: {}, type: {}", accountId, categoryCode, transactionTypeCode);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!"Y".equals(account.getActiveStatus())) {
            log.warn("Account {} is not active. Skipping interest calculation.", accountId);
            return BigDecimal.ZERO;
        }
        
        BigDecimal interestRate = determineInterestRate(account.getGroupId(), categoryCode, transactionTypeCode);
        
        if (interestRate.compareTo(BigDecimal.ZERO) <= 0) {
            log.debug("Interest rate is zero or negative. No interest calculated.");
            return BigDecimal.ZERO;
        }
        
        BigDecimal monthlyInterest = calculateMonthlyInterest(categoryBalance, interestRate);
        log.info("Monthly interest calculated: {} for category balance: {}", monthlyInterest, categoryBalance);
        
        return monthlyInterest;
    }

    private BigDecimal calculateMonthlyInterest(BigDecimal balance, BigDecimal annualRate) {
        if (balance == null || annualRate == null) {
            return BigDecimal.ZERO;
        }
        
        return balance.multiply(annualRate)
                .divide(BigDecimal.valueOf(MONTHS_IN_YEAR), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal determineInterestRate(String groupId, String categoryCode, String transactionTypeCode) {
        log.debug("Determining interest rate for group: {}, category: {}, type: {}", groupId, categoryCode, transactionTypeCode);
        
        BigDecimal interestRate = queryDisclosureGroupFile(groupId, categoryCode, transactionTypeCode);
        
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) == 0) {
            log.debug("No specific rate found. Trying with DEFAULT group ID.");
            interestRate = queryDisclosureGroupFile(DEFAULT_GROUP_ID, categoryCode, transactionTypeCode);
        }
        
        return interestRate != null ? interestRate : BigDecimal.ZERO;
    }

    private BigDecimal queryDisclosureGroupFile(String groupId, String categoryCode, String transactionTypeCode) {
        log.debug("Querying disclosure group file for group: {}, category: {}, type: {}", groupId, categoryCode, transactionTypeCode);
        return BigDecimal.valueOf(15.99);
    }

    @Transactional
    public void updateAccountBalanceWithInterest(String accountId, BigDecimal totalInterest) {
        log.info("Updating account balance with interest for account: {}, interest: {}", accountId, totalInterest);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        account.updateBalanceWithInterest(totalInterest);
        account.resetCycleAmounts();
        
        accountRepository.save(account);
        log.info("Account balance updated successfully. New balance: {}", account.getCurrentBalance());
    }

    public void validateBalanceForPayment(BigDecimal currentBalance) {
        if (currentBalance == null || currentBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You have nothing to pay...");
        }
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        
        response.setAccountId(account.getAccountId());
        response.setActiveStatus(account.getActiveStatus());
        response.setActiveStatusDisplay("Y".equals(account.getActiveStatus()) ? "Active" : "Inactive");
        response.setCurrentBalance(account.getCurrentBalance());
        response.setCreditLimit(account.getCreditLimit());
        response.setCashCreditLimit(account.getCashCreditLimit());
        response.setOpenDate(account.getOpenDate());
        response.setExpirationDate(account.getExpirationDate());
        response.setReissueDate(account.getReissueDate());
        response.setCurrentCycleCredit(account.getCurrentCycleCredit());
        response.setCurrentCycleDebit(account.getCurrentCycleDebit());
        response.setGroupId(account.getGroupId());
        response.setAccountData(account.getAccountData());
        
        // Calculate derived fields
        BigDecimal availableCredit = account.getCreditLimit().subtract(account.getCurrentBalance());
        response.setAvailableCredit(availableCredit.max(BigDecimal.ZERO));
        
        BigDecimal availableCashCredit = account.getCashCreditLimit().subtract(account.getCurrentBalance());
        response.setAvailableCashCredit(availableCashCredit.max(BigDecimal.ZERO));
        
        if (account.getCreditLimit().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal utilization = account.getCurrentBalance()
                    .divide(account.getCreditLimit(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            response.setCreditUtilizationPercentage(utilization);
        } else {
            response.setCreditUtilizationPercentage(BigDecimal.ZERO);
        }
        
        BigDecimal netCycle = account.getCurrentCycleCredit().subtract(account.getCurrentCycleDebit());
        response.setNetCycleAmount(netCycle);
        
        LocalDate expDate = parseDate(account.getExpirationDate());
        if (expDate != null) {
            response.setIsExpired(expDate.isBefore(LocalDate.now()));
            response.setDaysUntilExpiration(ChronoUnit.DAYS.between(LocalDate.now(), expDate));
        }
        
        response.setHasOutstandingBalance(account.getCurrentBalance().compareTo(BigDecimal.ZERO) > 0);
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        
        return response;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 8) {
            return null;
        }
        try {
            int year = Integer.parseInt(dateStr.substring(0, 4));
            int month = Integer.parseInt(dateStr.substring(4, 6));
            int day = Integer.parseInt(dateStr.substring(6, 8));
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            return null;
        }
    }
}
