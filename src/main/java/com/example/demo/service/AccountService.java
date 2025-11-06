package com.example.demo.service;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.enums.AccountStatus;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
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
 * Service class for Account operations
 * Implements business logic for account management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    
    /**
     * Create a new account
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        
        // Validate account ID format (must be 11 numeric digits)
        if (!request.getAccountId().matches("\\d{11}")) {
            throw new IllegalArgumentException("Account ID must be 11 numeric digits");
        }
        
        // Check if account already exists
        if (accountRepository.existsById(request.getAccountId())) {
            throw new IllegalArgumentException("Account with ID " + request.getAccountId() + " already exists");
        }
        
        // Check if account number already exists
        if (request.getAccountNumber() != null && accountRepository.existsByAccountNumber(request.getAccountNumber())) {
            throw new IllegalArgumentException("Account with number " + request.getAccountNumber() + " already exists");
        }
        
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId()));
        
        // Validate credit limit
        if (request.getCreditLimit().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit limit must be greater than zero");
        }
        
        Account account = new Account();
        account.setAccountId(request.getAccountId());
        account.setCustomer(customer);
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setCurrentBalance(BigDecimal.ZERO);
        account.setAvailableBalance(request.getCreditLimit());
        account.setCreditLimit(request.getCreditLimit());
        account.setCashAdvanceLimit(request.getCashAdvanceLimit() != null ? 
                request.getCashAdvanceLimit() : request.getCreditLimit().multiply(new BigDecimal("0.2")));
        account.setMinimumPaymentDue(BigDecimal.ZERO);
        account.setInterestRate(request.getInterestRate() != null ? request.getInterestRate() : new BigDecimal("18.99"));
        account.setAnnualFee(request.getAnnualFee() != null ? request.getAnnualFee() : BigDecimal.ZERO);
        account.setLateFee(new BigDecimal("35.00"));
        account.setOverlimitFee(new BigDecimal("25.00"));
        account.setAccountOpenDate(request.getAccountOpenDate() != null ? request.getAccountOpenDate() : LocalDate.now());
        account.setStatus(AccountStatus.ACTIVE);
        account.setAutopayEnabled(request.getAutopayEnabled() != null ? request.getAutopayEnabled() : false);
        account.setPaperlessStatements(request.getPaperlessStatements() != null ? request.getPaperlessStatements() : false);
        account.setFraudAlert(false);
        account.setTemporaryHold(false);
        account.setDaysDelinquent(0);
        account.setRewardPoints(0);
        account.setRewardPointsBalance(0);
        
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
        return accountRepository.findById(accountId).map(this::convertToResponse);
    }
    
    /**
     * Update account
     */
    @Transactional
    public AccountResponseDto updateAccount(String accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        
        // Update only non-null fields
        if (request.getCreditLimit() != null) {
            if (request.getCreditLimit().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Credit limit must be greater than zero");
            }
            account.setCreditLimit(request.getCreditLimit());
            // Recalculate available balance
            account.setAvailableBalance(request.getCreditLimit().subtract(account.getCurrentBalance()));
        }
        if (request.getCashAdvanceLimit() != null) account.setCashAdvanceLimit(request.getCashAdvanceLimit());
        if (request.getInterestRate() != null) account.setInterestRate(request.getInterestRate());
        if (request.getAnnualFee() != null) account.setAnnualFee(request.getAnnualFee());
        if (request.getLateFee() != null) account.setLateFee(request.getLateFee());
        if (request.getOverlimitFee() != null) account.setOverlimitFee(request.getOverlimitFee());
        if (request.getStatus() != null) account.setStatus(request.getStatus());
        if (request.getAutopayEnabled() != null) account.setAutopayEnabled(request.getAutopayEnabled());
        if (request.getPaperlessStatements() != null) account.setPaperlessStatements(request.getPaperlessStatements());
        if (request.getFraudAlert() != null) account.setFraudAlert(request.getFraudAlert());
        if (request.getTemporaryHold() != null) account.setTemporaryHold(request.getTemporaryHold());
        if (request.getPaymentDueDate() != null) account.setPaymentDueDate(request.getPaymentDueDate());
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        
        return convertToResponse(updatedAccount);
    }
    
    /**
     * Delete account
     */
    @Transactional
    public void deleteAccount(String accountId) {
        log.info("Deleting account with ID: {}", accountId);
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        
        // Check if account has balance
        if (account.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Cannot delete account with non-zero balance");
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
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get accounts by customer ID
     */
    @Transactional(readOnly = true)
    public List<AccountResponseDto> getAccountsByCustomerId(String customerId) {
        log.debug("Fetching accounts for customer ID: {}", customerId);
        return accountRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get accounts by status
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsByStatus(AccountStatus status, Pageable pageable) {
        log.debug("Fetching accounts with status: {}", status);
        return accountRepository.findByStatus(status, pageable).map(this::convertToResponse);
    }
    
    /**
     * Get delinquent accounts
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getDelinquentAccounts(Pageable pageable) {
        log.debug("Fetching delinquent accounts");
        return accountRepository.findDelinquentAccounts(pageable).map(this::convertToResponse);
    }
    
    /**
     * Get over-limit accounts
     */
    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getOverLimitAccounts(Pageable pageable) {
        log.debug("Fetching over-limit accounts");
        return accountRepository.findOverLimitAccounts(pageable).map(this::convertToResponse);
    }
    
    /**
     * Apply payment to account
     */
    @Transactional
    public AccountResponseDto applyPayment(String accountId, BigDecimal amount) {
        log.info("Applying payment of {} to account {}", amount, accountId);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        
        account.applyPayment(amount);
        
        // Reset delinquency if payment covers minimum due
        if (account.getMinimumPaymentDue().compareTo(BigDecimal.ZERO) == 0) {
            account.setDaysDelinquent(0);
        }
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Payment applied successfully to account {}", accountId);
        
        return convertToResponse(updatedAccount);
    }
    
    /**
     * Apply charge to account
     */
    @Transactional
    public AccountResponseDto applyCharge(String accountId, BigDecimal amount) {
        log.info("Applying charge of {} to account {}", amount, accountId);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Charge amount must be greater than zero");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        
        // Check if account can transact
        if (!account.canTransact()) {
            throw new IllegalArgumentException("Account cannot process transactions");
        }
        
        // Check if charge would exceed credit limit (with some tolerance for fees)
        BigDecimal newBalance = account.getCurrentBalance().add(amount);
        BigDecimal maxAllowedBalance = account.getCreditLimit().multiply(new BigDecimal("1.1")); // 10% over-limit tolerance
        
        if (newBalance.compareTo(maxAllowedBalance) > 0) {
            throw new IllegalArgumentException("Charge would exceed credit limit");
        }
        
        account.applyCharge(amount);
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Charge applied successfully to account {}", accountId);
        
        return convertToResponse(updatedAccount);
    }
    
    /**
     * Calculate and apply interest to account
     */
    @Transactional
    public AccountResponseDto calculateAndApplyInterest(String accountId) {
        log.info("Calculating and applying interest to account {}", accountId);
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        
        // Calculate daily interest
        BigDecimal dailyRate = account.getInterestRate().divide(new BigDecimal("365"), 6, java.math.RoundingMode.HALF_UP);
        BigDecimal interestAmount = account.getCurrentBalance().multiply(dailyRate).divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        
        if (interestAmount.compareTo(BigDecimal.ZERO) > 0) {
            account.applyInterest(interestAmount);
            Account updatedAccount = accountRepository.save(account);
            log.info("Interest of {} applied to account {}", interestAmount, accountId);
            return convertToResponse(updatedAccount);
        }
        
        return convertToResponse(account);
    }
    
    /**
     * Convert Account entity to AccountResponseDto
     */
    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setCustomerId(account.getCustomer().getCustomerId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setCurrentBalance(account.getCurrentBalance());
        response.setAvailableBalance(account.getAvailableBalance());
        response.setCreditLimit(account.getCreditLimit());
        response.setAvailableCredit(account.getAvailableCredit());
        response.setCashAdvanceLimit(account.getCashAdvanceLimit());
        response.setMinimumPaymentDue(account.getMinimumPaymentDue());
        response.setPaymentDueDate(account.getPaymentDueDate());
        response.setLastPaymentAmount(account.getLastPaymentAmount());
        response.setLastPaymentDate(account.getLastPaymentDate());
        response.setLastStatementBalance(account.getLastStatementBalance());
        response.setLastStatementDate(account.getLastStatementDate());
        response.setNextStatementDate(account.getNextStatementDate());
        response.setAccountOpenDate(account.getAccountOpenDate());
        response.setAccountCloseDate(account.getAccountCloseDate());
        response.setInterestRate(account.getInterestRate());
        response.setAnnualFee(account.getAnnualFee());
        response.setLateFee(account.getLateFee());
        response.setOverlimitFee(account.getOverlimitFee());
        response.setDaysDelinquent(account.getDaysDelinquent());
        response.setCycleToDatePurchases(account.getCycleToDatePurchases());
        response.setCycleToDateCashAdvances(account.getCycleToDateCashAdvances());
        response.setCycleToDatePayments(account.getCycleToDatePayments());
        response.setYearToDatePurchases(account.getYearToDatePurchases());
        response.setYearToDateCashAdvances(account.getYearToDateCashAdvances());
        response.setYearToDateInterest(account.getYearToDateInterest());
        response.setYearToDateFees(account.getYearToDateFees());
        response.setStatus(account.getStatus());
        response.setStatusDisplayName(account.getStatus().getDisplayName());
        response.setRewardPoints(account.getRewardPoints());
        response.setRewardPointsBalance(account.getRewardPointsBalance());
        response.setAutopayEnabled(account.getAutopayEnabled());
        response.setPaperlessStatements(account.getPaperlessStatements());
        response.setFraudAlert(account.getFraudAlert());
        response.setTemporaryHold(account.getTemporaryHold());
        response.setCreditUtilization(account.getCreditUtilization());
        response.setIsOverLimit(account.isOverLimit());
        response.setIsDelinquent(account.isDelinquent());
        response.setIsPastDue(account.isPastDue());
        response.setCanTransact(account.canTransact());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
