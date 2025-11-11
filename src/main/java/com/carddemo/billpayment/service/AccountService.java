package com.carddemo.billpayment.service;

import com.carddemo.billpayment.dto.AccountResponseDto;
import com.carddemo.billpayment.dto.CreateAccountRequestDto;
import com.carddemo.billpayment.dto.UpdateAccountRequestDto;
import com.carddemo.billpayment.entity.Account;
import com.carddemo.billpayment.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAcctId());

        // BR001: Account Validation
        if (request.getAcctId() == null || request.getAcctId().trim().isEmpty()) {
            throw new IllegalArgumentException("Acct ID can NOT be empty...");
        }

        if (accountRepository.existsByAcctId(request.getAcctId())) {
            throw new IllegalArgumentException("Account ID already exists");
        }

        Account account = new Account();
        account.setAcctId(request.getAcctId());
        account.setAcctCurrBal(request.getAcctCurrBal());

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAcctId());
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String acctId) {
        log.info("Retrieving account by account ID: {}", acctId);

        // BR001: Account Validation
        if (acctId == null || acctId.trim().isEmpty()) {
            throw new IllegalArgumentException("Acct ID can NOT be empty...");
        }

        return accountRepository.findByAcctId(acctId).map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(String acctId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", acctId);

        // BR001: Account Validation
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        if (request.getAcctCurrBal() != null) {
            account.setAcctCurrBal(request.getAcctCurrBal());
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAcctId());
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(String acctId) {
        log.info("Deleting account with ID: {}", acctId);

        // BR001: Account Validation
        if (!accountRepository.existsByAcctId(acctId)) {
            throw new IllegalArgumentException("Account ID NOT found...");
        }

        accountRepository.deleteById(acctId);
        log.info("Account deleted successfully with ID: {}", acctId);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * BR001: Account Validation - Validates that the entered account ID exists in the system
     */
    @Transactional(readOnly = true)
    public void validateAccountExists(String acctId) {
        log.info("Validating account exists: {}", acctId);

        if (acctId == null || acctId.trim().isEmpty()) {
            throw new IllegalArgumentException("Acct ID can NOT be empty...");
        }

        if (!accountRepository.existsByAcctId(acctId)) {
            throw new IllegalArgumentException("Account ID NOT found...");
        }

        log.info("Account validation successful: {}", acctId);
    }

    /**
     * BR002: Balance Check - Verifies that the account has a positive balance to pay
     */
    @Transactional(readOnly = true)
    public void validatePositiveBalance(String acctId) {
        log.info("Validating positive balance for account: {}", acctId);

        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        // BR002: Must be greater than zero to process payment
        if (account.getAcctCurrBal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You have nothing to pay...");
        }

        log.info("Balance validation successful for account: {}", acctId);
    }

    /**
     * BR004: Full Balance Payment - Payment processes the full current account balance
     */
    @Transactional(readOnly = true)
    public BigDecimal getFullBalancePaymentAmount(String acctId) {
        log.info("Getting full balance payment amount for account: {}", acctId);

        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        if (account.getAcctCurrBal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You have nothing to pay...");
        }

        log.info("Full balance payment amount for account {}: {}", acctId, account.getAcctCurrBal());
        return account.getAcctCurrBal();
    }

    /**
     * BR007: Account Balance Update - Updates account balance after successful payment
     */
    @Transactional
    public AccountResponseDto updateBalanceAfterPayment(String acctId, BigDecimal paymentAmount) {
        log.info("Updating account balance after payment for account: {}", acctId);

        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));

        BigDecimal previousBalance = account.getAcctCurrBal();
        account.subtractAmount(paymentAmount);
        
        Account updatedAccount = accountRepository.save(account);
        log.info("Account balance updated for account: {} from {} to {}", 
                 acctId, previousBalance, updatedAccount.getAcctCurrBal());
        
        return convertToResponse(updatedAccount);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsWithPositiveBalance(Pageable pageable) {
        log.info("Retrieving accounts with positive balance");
        return accountRepository.findAccountsWithPositiveBalancePaged(pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public long countTransactionsByAccount(String acctId) {
        log.info("Counting transactions for account: {}", acctId);
        return accountRepository.countTransactionsByAcctId(acctId);
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAcctId(account.getAcctId());
        response.setAcctCurrBal(account.getAcctCurrBal());
        response.setHasPositiveBalance(account.hasPositiveBalance());
        response.setPaymentAmount(account.getPaymentAmount());
        response.setEligibleForPayment(account.hasPositiveBalance());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        
        if (account.hasPositiveBalance()) {
            response.setPaymentStatusMessage("Account ready for payment");
        } else {
            response.setPaymentStatusMessage("You have nothing to pay...");
        }
        
        return response;
    }
}
