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
import java.time.temporal.ChronoUnit;
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
    public void resetCycleAmounts(Long accountId) {
        log.info("Resetting cycle amounts for account: {} (BR009)", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        account.resetCycleAmounts();
        accountRepository.save(account);
        
        log.info("Cycle amounts reset to zero for account: {}", accountId);
    }

    @Transactional
    public void updateBalanceWithInterest(Long accountId, BigDecimal interestAmount) {
        log.info("Updating balance with interest for account: {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        account.updateBalanceWithInterest(interestAmount);
        account.resetCycleAmounts();
        
        accountRepository.save(account);
        
        log.info("Balance updated with interest. New balance: {} for account: {}", 
                account.getCurrentBalance(), accountId);
    }

    @Transactional(readOnly = true)
    public void validatePaymentBalance(Long accountId) {
        log.info("Validating payment balance for account: {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (!account.hasBalanceToPay()) {
            throw new IllegalArgumentException("You have nothing to pay...");
        }
        
        log.info("Payment balance validation passed for account: {}", accountId);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> findAccountsByStatus(String status, Pageable pageable) {
        log.info("Finding accounts by status: {}", status);
        return accountRepository.findByActiveStatus(status, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> findAccountsByGroupId(String groupId, Pageable pageable) {
        log.info("Finding accounts by group ID: {}", groupId);
        return accountRepository.findByGroupId(groupId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> findAccountsWithBalance(Pageable pageable) {
        log.info("Finding accounts with positive balance");
        return accountRepository.findAccountsWithBalanceGreaterThanZero(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> findExpiredAccounts(Pageable pageable) {
        log.info("Finding expired accounts");
        return accountRepository.findExpiredAccounts(LocalDate.now(), pageable).map(this::convertToResponse);
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setActiveStatus(account.getActiveStatus());
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
        response.setAvailableCredit(account.getAvailableCredit());
        response.setAvailableCashCredit(account.getAvailableCashCredit());
        response.setNetCycleAmount(account.getCurrentCycleCredit().subtract(account.getCurrentCycleDebit()));
        response.setActiveStatusDisplay(account.isActive() ? "Active" : "Inactive");
        response.setIsExpired(account.isExpired());
        response.setCreditUtilizationPercentage(calculateCreditUtilization(account));
        response.setHasOutstandingBalance(account.hasBalanceToPay());
        response.setFormattedAccountId(account.getFormattedAccountId());
        response.setAccountSummary(account.getAccountSummary());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }

    private BigDecimal calculateCreditUtilization(Account account) {
        if (account.getCreditLimit().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return account.getCurrentBalance()
                .divide(account.getCreditLimit(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
