package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Account;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());

        // BR001: Account Validation
        validateAccountIdNotEmpty(request.getAccountId());

        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Account with ID already exists");
        }

        Account account = new Account();
        account.setAccountId(request.getAccountId());
        account.setCurrentBalance(request.getCurrentBalance() != null ? request.getCurrentBalance() : BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(String accountId) {
        log.info("Retrieving account by account ID: {}", accountId);
        validateAccountIdNotEmpty(accountId);
        return accountRepository.findByAccountId(accountId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public AccountBalanceDto getAccountBalance(String accountId) {
        log.info("Retrieving account balance for account ID: {}", accountId);
        
        // BR001: Account Validation
        validateAccountIdNotEmpty(accountId);
        Account account = validateAccountExists(accountId);
        
        AccountBalanceDto balanceDto = new AccountBalanceDto();
        balanceDto.setAccountId(account.getAccountId());
        balanceDto.setCurrentBalance(account.getCurrentBalance());
        balanceDto.setHasPositiveBalance(account.hasPositiveBalance());
        
        return balanceDto;
    }

    @Transactional
    public AccountResponseDto updateAccount(String accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);

        // BR001: Account Validation
        validateAccountIdNotEmpty(accountId);
        Account account = validateAccountExists(accountId);

        if (request.getCurrentBalance() != null) {
            account.setCurrentBalance(request.getCurrentBalance());
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(String accountId) {
        log.info("Deleting account with ID: {}", accountId);

        // BR001: Account Validation
        validateAccountIdNotEmpty(accountId);
        
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

    /**
     * BR003: Payment Confirmation - Requires user confirmation before processing payment
     * BR004: Full Balance Payment - Payment processes the full current account balance
     * BR006: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes
     * BR007: Account Balance Update - Updates account balance after successful payment
     */
    @Transactional
    public ProcessPaymentResponseDto processPayment(ProcessPaymentRequestDto request) {
        log.info("Processing payment for account ID: {}", request.getAccountId());

        // BR001: Account Validation
        validateAccountIdNotEmpty(request.getAccountId());
        
        // BR003: Payment Confirmation
        validatePaymentConfirmation(request.getConfirmPayment());

        // BR001: Account Validation - Validates that the entered account ID exists in the system
        Account account = validateAccountExists(request.getAccountId());
        
        // BR002: Balance Check - Verifies that the account has a positive balance to pay
        validatePositiveBalance(account);

        // Store previous balance for response
        BigDecimal previousBalance = account.getCurrentBalance();

        // BR004: Full Balance Payment - Payment processes the full current account balance
        BigDecimal paymentAmount = processFullBalancePayment(account);
        
        // BR006: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes
        Transaction transaction = createBillPaymentTransaction(account, paymentAmount, request.getCardNumber());
        
        // BR007: Account Balance Update - Updates account balance after successful payment
        updateAccountBalance(account, paymentAmount);

        ProcessPaymentResponseDto response = new ProcessPaymentResponseDto();
        response.setTransactionId(transaction.getTransactionId());
        response.setAccountId(account.getAccountId());
        response.setPreviousBalance(previousBalance);
        response.setNewBalance(account.getCurrentBalance());
        response.setPaymentAmount(paymentAmount);
        response.setTimestamp(transaction.getProcessingTimestamp());
        response.setMessage("Payment processed successfully. Transaction ID: " + transaction.getTransactionId());
        response.setTransactionTypeCode(transaction.getTransactionTypeCode());
        response.setTransactionCategoryCode(transaction.getTransactionCategoryCode());
        response.setTransactionSource(transaction.getTransactionSource());
        response.setTransactionDescription(transaction.getDescription());
        response.setMerchantId(transaction.getMerchantId());
        response.setMerchantName(transaction.getMerchantName());
        response.setMerchantCity(transaction.getMerchantCity());
        response.setMerchantZip(transaction.getMerchantZip());

        log.info("Payment processed successfully for account ID: {} with transaction ID: {}", 
                account.getAccountId(), transaction.getTransactionId());

        return response;
    }

    /**
     * BR001: Account Validation - Validates that account ID is not empty
     */
    private void validateAccountIdNotEmpty(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            log.error("Account ID validation failed: Account ID is empty");
            throw new IllegalArgumentException("Acct ID can NOT be empty...");
        }
    }

    /**
     * BR001: Account Validation - Validates that the entered account ID exists in the system
     */
    private Account validateAccountExists(String accountId) {
        log.debug("Validating account exists: {}", accountId);
        return accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> {
                    log.error("Account validation failed: Account ID NOT found: {}", accountId);
                    return new IllegalArgumentException("Account ID NOT found...");
                });
    }

    /**
     * BR002: Balance Check - Verifies that the account has a positive balance to pay
     */
    private void validatePositiveBalance(Account account) {
        log.debug("Validating positive balance for account: {}", account.getAccountId());
        if (!account.hasPositiveBalance()) {
            log.warn("Balance check failed: Account {} has zero or negative balance", account.getAccountId());
            throw new IllegalArgumentException("You have nothing to pay...");
        }
    }

    /**
     * BR003: Payment Confirmation - Requires user confirmation before processing payment
     */
    private void validatePaymentConfirmation(String confirmation) {
        log.debug("Validating payment confirmation: {}", confirmation);

        if (confirmation == null || confirmation.trim().isEmpty()) {
            log.info("Payment confirmation not provided - display information only");
            throw new IllegalArgumentException("Payment confirmation required. Please confirm with Y to proceed.");
        }

        String normalizedConfirmation = confirmation.trim().toUpperCase();

        if (normalizedConfirmation.equals("N")) {
            log.info("Payment cancelled by user");
            throw new IllegalArgumentException("Payment cancelled by user");
        }

        if (!normalizedConfirmation.equals("Y")) {
            log.error("Payment confirmation validation failed: Invalid value: {}", confirmation);
            throw new IllegalArgumentException("Invalid value. Valid values are (Y/N)...");
        }

        log.debug("Payment confirmation validated successfully");
    }

    /**
     * BR004: Full Balance Payment - Payment processes the full current account balance
     */
    private BigDecimal processFullBalancePayment(Account account) {
        log.debug("Processing full balance payment for account: {}", account.getAccountId());
        BigDecimal paymentAmount = account.getCurrentBalance();
        log.info("Full balance payment amount: {} for account: {}", paymentAmount, account.getAccountId());
        return paymentAmount;
    }

    /**
     * BR006: Bill Payment Transaction Recording - Records bill payment with specific transaction attributes
     * BR005: Transaction ID Generation - Generates unique sequential transaction ID (handled by database)
     */
    private Transaction createBillPaymentTransaction(Account account, BigDecimal amount, String cardNumber) {
        log.debug("Creating bill payment transaction for account: {}", account.getAccountId());

        Transaction transaction = Transaction.createBillPaymentTransaction(amount, cardNumber, account);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Bill payment transaction created with ID: {} for account: {}", 
                savedTransaction.getTransactionId(), account.getAccountId());

        return savedTransaction;
    }

    /**
     * BR007: Account Balance Update - Updates account balance after successful payment
     */
    private void updateAccountBalance(Account account, BigDecimal paymentAmount) {
        log.debug("Updating account balance for account: {}", account.getAccountId());

        BigDecimal oldBalance = account.getCurrentBalance();
        account.subtractAmount(paymentAmount);
        BigDecimal newBalance = account.getCurrentBalance();

        accountRepository.save(account);

        log.info("Account balance updated for account: {}. Old balance: {}, Payment: {}, New balance: {}", 
                account.getAccountId(), oldBalance, paymentAmount, newBalance);
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setCurrentBalance(account.getCurrentBalance());
        response.setHasPositiveBalance(account.hasPositiveBalance());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
