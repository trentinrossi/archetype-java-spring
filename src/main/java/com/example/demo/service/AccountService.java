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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        
        Long accountId = Long.parseLong(request.getAccountId());
        
        if (accountRepository.existsByAccountId(accountId)) {
            throw new IllegalArgumentException("Account with ID already exists");
        }
        
        Account account = new Account();
        account.setAccountId(accountId);
        account.setAcctId(accountId);
        account.setActiveStatus(request.getActiveStatus());
        account.setAccountStatus(request.getAccountStatus());
        account.setAcctActiveStatus(request.getActiveStatus());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setAcctCurrBal(request.getCurrentBalance());
        account.setCreditLimit(request.getCreditLimit());
        account.setAcctCreditLimit(request.getCreditLimit());
        account.setCashCreditLimit(request.getCashCreditLimit());
        account.setAcctCashCreditLimit(request.getCashCreditLimit());
        account.setOpenDate(LocalDate.parse(request.getOpenDate(), DATE_FORMATTER));
        account.setAcctOpenDate(LocalDate.parse(request.getOpenDate(), DATE_FORMATTER));
        account.setExpirationDate(LocalDate.parse(request.getExpirationDate(), DATE_FORMATTER));
        account.setAcctExpiraionDate(LocalDate.parse(request.getExpirationDate(), DATE_FORMATTER));
        account.setReissueDate(LocalDate.parse(request.getReissueDate(), DATE_FORMATTER));
        account.setAcctReissueDate(LocalDate.parse(request.getReissueDate(), DATE_FORMATTER));
        account.setCurrentCycleCredit(request.getCurrentCycleCredit());
        account.setAcctCurrCycCredit(request.getCurrentCycleCredit());
        account.setCurrentCycleDebit(request.getCurrentCycleDebit());
        account.setAcctCurrCycDebit(request.getCurrentCycleDebit());
        account.setGroupId(request.getGroupId());
        account.setAcctGroupId(request.getGroupId());
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long accountId) {
        log.info("Retrieving account with ID: {}", accountId);
        return accountRepository.findByAccountId(accountId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(Long accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (request.getActiveStatus() != null) {
            account.setActiveStatus(request.getActiveStatus());
            account.setAccountStatus(request.getActiveStatus());
            account.setAcctActiveStatus(request.getActiveStatus());
        }
        if (request.getCurrentBalance() != null) {
            account.setCurrentBalance(request.getCurrentBalance());
            account.setAcctCurrBal(request.getCurrentBalance());
        }
        if (request.getCreditLimit() != null) {
            account.setCreditLimit(request.getCreditLimit());
            account.setAcctCreditLimit(request.getCreditLimit());
        }
        if (request.getCashCreditLimit() != null) {
            account.setCashCreditLimit(request.getCashCreditLimit());
            account.setAcctCashCreditLimit(request.getCashCreditLimit());
        }
        if (request.getOpenDate() != null) {
            LocalDate openDate = LocalDate.parse(request.getOpenDate(), DATE_FORMATTER);
            account.setOpenDate(openDate);
            account.setAcctOpenDate(openDate);
        }
        if (request.getExpirationDate() != null) {
            LocalDate expirationDate = LocalDate.parse(request.getExpirationDate(), DATE_FORMATTER);
            account.setExpirationDate(expirationDate);
            account.setAcctExpiraionDate(expirationDate);
        }
        if (request.getReissueDate() != null) {
            LocalDate reissueDate = LocalDate.parse(request.getReissueDate(), DATE_FORMATTER);
            account.setReissueDate(reissueDate);
            account.setAcctReissueDate(reissueDate);
        }
        if (request.getCurrentCycleCredit() != null) {
            account.setCurrentCycleCredit(request.getCurrentCycleCredit());
            account.setAcctCurrCycCredit(request.getCurrentCycleCredit());
        }
        if (request.getCurrentCycleDebit() != null) {
            account.setCurrentCycleDebit(request.getCurrentCycleDebit());
            account.setAcctCurrCycDebit(request.getCurrentCycleDebit());
        }
        if (request.getGroupId() != null) {
            account.setGroupId(request.getGroupId());
            account.setAcctGroupId(request.getGroupId());
        }
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        log.info("Deleting account with ID: {}", accountId);
        
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new IllegalArgumentException("Account not found");
        }
        
        accountRepository.deleteById(accountId);
        log.info("Account deleted successfully with ID: {}", accountId);
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDto> processAccountRecordsSequentially() {
        log.info("BR-001: Starting sequential account record processing");
        
        List<Account> accounts = accountRepository.findAllAccountsOrderedById();
        
        List<AccountResponseDto> processedAccounts = accounts.stream()
                .map(account -> {
                    displayAccountRecord(account);
                    return convertToResponse(account);
                })
                .collect(Collectors.toList());
        
        log.info("BR-001: Sequential processing completed. Total records: {}", processedAccounts.size());
        return processedAccounts;
    }

    private void displayAccountRecord(Account account) {
        log.info("BR-002: Account ID: {}, Status: {}, Balance: {}, Credit Limit: {}, Open Date: {}", 
                 account.getAccountId(), account.getActiveStatus(), account.getCurrentBalance(), 
                 account.getCreditLimit(), account.getOpenDate());
    }

    @Transactional
    public BigDecimal calculateInterestByCategory(Long accountId, String categoryCode, BigDecimal categoryBalance) {
        log.info("BR001: Calculating interest for account {} category {}", accountId, categoryCode);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!"Y".equals(account.getActiveStatus())) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal annualRate = BigDecimal.valueOf(15.99);
        BigDecimal monthlyInterest = categoryBalance
                .multiply(annualRate)
                .divide(BigDecimal.valueOf(1200), 2, RoundingMode.HALF_UP);
        
        log.info("Monthly interest calculated: {}", monthlyInterest);
        return monthlyInterest;
    }

    @Transactional
    public void updateAccountBalanceWithInterest(Long accountId, BigDecimal totalInterest) {
        log.info("BR003: Updating account balance with interest for account {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        account.addInterestToBalance(totalInterest);
        account.resetCycleAmounts();
        
        accountRepository.save(account);
        log.info("Account balance updated with interest: {}", totalInterest);
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(String.valueOf(account.getAccountId()));
        response.setActiveStatus(account.getActiveStatus());
        response.setCurrentBalance(account.getCurrentBalance());
        response.setCreditLimit(account.getCreditLimit());
        response.setCashCreditLimit(account.getCashCreditLimit());
        response.setOpenDate(account.getOpenDate() != null ? account.getOpenDate().format(DATE_FORMATTER) : null);
        response.setExpirationDate(account.getExpirationDate() != null ? account.getExpirationDate().format(DATE_FORMATTER) : null);
        response.setReissueDate(account.getReissueDate() != null ? account.getReissueDate().format(DATE_FORMATTER) : null);
        response.setCurrentCycleCredit(account.getCurrentCycleCredit());
        response.setCurrentCycleDebit(account.getCurrentCycleDebit());
        response.setGroupId(account.getGroupId());
        response.setAccountStatus(account.getAccountStatus());
        
        // Computed fields
        response.setAvailableCredit(account.getCreditLimit().subtract(account.getCurrentBalance()));
        response.setAvailableCashCredit(account.getCashCreditLimit().subtract(account.getCurrentBalance()));
        response.setNetCycleAmount(account.getCurrentCycleCredit().subtract(account.getCurrentCycleDebit()));
        
        if (account.getCreditLimit().compareTo(BigDecimal.ZERO) > 0) {
            response.setCreditUtilizationPercentage(
                account.getCurrentBalance()
                    .divide(account.getCreditLimit(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
            );
        }
        
        response.setActiveStatusDisplay("Y".equals(account.getActiveStatus()) ? "Active" : "Inactive");
        response.setAccountStatusDisplay("Y".equals(account.getAccountStatus()) ? "Active" : "Inactive");
        response.setIsExpired(account.getExpirationDate() != null && account.getExpirationDate().isBefore(LocalDate.now()));
        response.setIsOverLimit(account.getCurrentBalance().compareTo(account.getCreditLimit()) > 0);
        
        if (account.getOpenDate() != null) {
            response.setDaysSinceOpened(ChronoUnit.DAYS.between(account.getOpenDate(), LocalDate.now()));
        }
        if (account.getExpirationDate() != null) {
            response.setDaysUntilExpiration(ChronoUnit.DAYS.between(LocalDate.now(), account.getExpirationDate()));
        }
        
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        
        return response;
    }
}
