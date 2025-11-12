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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
        account.setXrefAcctId(accountId);
        account.setActiveStatus(request.getActiveStatus());
        account.setAcctActiveStatus(request.getActiveStatus());
        account.setAccountStatus(request.getActiveStatus());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setAcctCurrBal(request.getCurrentBalance());
        account.setCreditLimit(request.getCreditLimit());
        account.setAcctCreditLimit(request.getCreditLimit());
        account.setCashCreditLimit(request.getCashCreditLimit());
        account.setAcctCashCreditLimit(request.getCashCreditLimit());
        account.setOpenDate(request.getOpenDate());
        account.setAcctOpenDate(request.getOpenDate());
        account.setExpirationDate(request.getExpirationDate());
        account.setAcctExpiraionDate(request.getExpirationDate());
        account.setReissueDate(request.getReissueDate());
        account.setAcctReissueDate(request.getReissueDate());
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
            account.setAcctActiveStatus(request.getActiveStatus());
            account.setAccountStatus(request.getActiveStatus());
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
        
        if (request.getExpirationDate() != null) {
            account.setExpirationDate(request.getExpirationDate());
            account.setAcctExpiraionDate(request.getExpirationDate());
        }
        
        if (request.getReissueDate() != null) {
            account.setReissueDate(request.getReissueDate());
            account.setAcctReissueDate(request.getReissueDate());
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

    @Transactional
    public AccountResponseDto calculateAndApplyInterest(Long accountId, BigDecimal annualRate) {
        log.info("Calculating interest for account: {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        BigDecimal monthlyInterest = account.calculateMonthlyInterest(annualRate);
        account.updateBalanceWithInterest(monthlyInterest);
        account.resetCycleAmounts();
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Interest applied successfully for account: {}", accountId);
        
        return convertToResponse(updatedAccount);
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(String.valueOf(account.getAccountId()));
        response.setActiveStatus(account.getActiveStatus());
        response.setActiveStatusDisplay(account.isActive() ? "Active" : "Inactive");
        response.setCurrentBalance(account.getCurrentBalance());
        response.setCreditLimit(account.getCreditLimit());
        response.setCashCreditLimit(account.getCashCreditLimit());
        response.setOpenDate(account.getOpenDate());
        response.setOpenDateFormatted(formatDate(account.getOpenDate()));
        response.setExpirationDate(account.getExpirationDate());
        response.setExpirationDateFormatted(formatDate(account.getExpirationDate()));
        response.setReissueDate(account.getReissueDate());
        response.setReissueDateFormatted(formatDate(account.getReissueDate()));
        response.setCurrentCycleCredit(account.getCurrentCycleCredit());
        response.setCurrentCycleDebit(account.getCurrentCycleDebit());
        response.setGroupId(account.getGroupId());
        response.setAccountData(account.getAccountData());
        
        // Calculated fields
        response.setAvailableCredit(account.getCreditLimit().subtract(account.getCurrentBalance()));
        response.setAvailableCashCredit(account.getCashCreditLimit().subtract(account.getCurrentBalance()));
        response.setNetCycleAmount(account.getCurrentCycleCredit().subtract(account.getCurrentCycleDebit()));
        response.setIsActive(account.isActive());
        response.setIsExpired(isExpired(account.getExpirationDate()));
        response.setCreditUtilizationPercentage(calculateUtilization(account.getCurrentBalance(), account.getCreditLimit()));
        response.setDaysUntilExpiration(calculateDaysUntil(account.getExpirationDate()));
        response.setAccountAgeInDays(calculateDaysSince(account.getOpenDate()));
        response.setFormattedBalance(account.getFormattedBalance());
        response.setFormattedCreditLimit(account.getFormattedCreditLimit());
        response.setFormattedCashCreditLimit(account.getFormattedCashCreditLimit());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        
        return response;
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return dateStr;
        }
    }

    private Boolean isExpired(String expirationDate) {
        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate expDate = LocalDate.parse(expirationDate, DATE_FORMATTER);
            return expDate.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    private BigDecimal calculateUtilization(BigDecimal balance, BigDecimal limit) {
        if (limit.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return balance.divide(limit, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    private Long calculateDaysUntil(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            return ChronoUnit.DAYS.between(LocalDate.now(), date);
        } catch (Exception e) {
            return null;
        }
    }

    private Long calculateDaysSince(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            return ChronoUnit.DAYS.between(date, LocalDate.now());
        } catch (Exception e) {
            return null;
        }
    }
}
