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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAcctId());

        validateAccountIdNotEmpty(request.getAcctId());

        if (accountRepository.existsByAcctId(request.getAcctId())) {
            throw new IllegalArgumentException("Account with ID already exists");
        }

        Account account = new Account();
        account.setAcctId(request.getAcctId());
        account.setAcctCurrBal(request.getAcctCurrBal() != null ? request.getAcctCurrBal() : BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAcctId());
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String acctId) {
        log.info("Retrieving account with ID: {}", acctId);
        validateAccountIdNotEmpty(acctId);
        return accountRepository.findByAcctId(acctId).map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(String acctId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", acctId);
        
        validateAccountIdNotEmpty(acctId);
        
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
        
        validateAccountIdNotEmpty(acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));
        
        accountRepository.delete(account);
        log.info("Account deleted successfully with ID: {}", acctId);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public void validateAccount(String acctId) {
        log.info("Validating account with ID: {}", acctId);
        
        validateAccountIdNotEmpty(acctId);
        
        if (!accountRepository.existsByAcctId(acctId)) {
            log.error("Account ID NOT found: {}", acctId);
            throw new IllegalArgumentException("Account ID NOT found...");
        }
        
        log.info("Account validation successful for ID: {}", acctId);
    }

    @Transactional(readOnly = true)
    public void checkBalance(String acctId) {
        log.info("Checking balance for account ID: {}", acctId);
        
        validateAccount(acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));
        
        if (account.getAcctCurrBal().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Account has zero or negative balance: {}", acctId);
            throw new IllegalArgumentException("You have nothing to pay...");
        }
        
        log.info("Balance check successful for account ID: {}. Balance: {}", acctId, account.getAcctCurrBal());
    }

    @Transactional
    public BigDecimal processFullBalancePayment(String acctId) {
        log.info("Processing full balance payment for account ID: {}", acctId);
        
        validateAccount(acctId);
        checkBalance(acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));
        
        BigDecimal paymentAmount = account.getAcctCurrBal();
        log.info("Payment amount set to current balance: {}", paymentAmount);
        
        account.processFullBalancePayment();
        accountRepository.save(account);
        
        log.info("Full balance payment processed successfully for account ID: {}. Amount: {}", acctId, paymentAmount);
        
        return paymentAmount;
    }

    @Transactional
    public void updateAccountBalance(String acctId, BigDecimal paymentAmount) {
        log.info("Updating account balance for account ID: {}. Payment amount: {}", acctId, paymentAmount);
        
        validateAccount(acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account ID NOT found..."));
        
        BigDecimal currentBalance = account.getAcctCurrBal();
        BigDecimal newBalance = currentBalance.subtract(paymentAmount);
        
        log.info("Calculating new balance: {} - {} = {}", currentBalance, paymentAmount, newBalance);
        
        account.updateBalanceAfterPayment(paymentAmount);
        
        try {
            accountRepository.save(account);
            log.info("Account balance updated successfully for account ID: {}. New balance: {}", acctId, newBalance);
        } catch (Exception e) {
            log.error("Failed to update account balance for account ID: {}", acctId, e);
            throw new RuntimeException("Failed to update account balance. Please try again.");
        }
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsWithPositiveBalance(Pageable pageable) {
        log.info("Retrieving accounts with positive balance");
        return accountRepository.findAllAccountsWithPositiveBalance(pageable).map(this::convertToResponse);
    }

    private void validateAccountIdNotEmpty(String acctId) {
        if (acctId == null || acctId.trim().isEmpty() || acctId.equals("\u0000") || acctId.matches("^\\x00+$")) {
            log.error("Account ID is empty or contains low-values");
            throw new IllegalArgumentException("Acct ID can NOT be empty...");
        }
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAcctId(account.getAcctId());
        response.setAcctCurrBal(account.getAcctCurrBal());
        response.setFormattedAcctCurrBal(account.getFormattedBalance());
        response.setCardCount(account.getCards() != null ? account.getCards().size() : 0);
        response.setTransactionCount(account.getTransactions() != null ? account.getTransactions().size() : 0);
        response.setHasBalanceToPay(account.hasPositiveBalance());
        response.setPaymentAmount(account.getFullPaymentAmount());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
