package com.example.demo.service;

import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.dto.AccountResponseDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());

        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Account ID must be 11 digits numeric and exist in account file");
        }

        Customer customer = customerRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer ID must be 9 digits numeric and exist in customer file"));

        Account account = new Account();
        account.setAccountId(request.getAccountId());
        account.setCurrentBalance(request.getCurrentBalance());
        account.setCreditLimit(request.getCreditLimit());
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long id) {
        log.info("Retrieving account by ID: {}", id);
        return accountRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountByAccountId(Long accountId) {
        log.info("Retrieving account by account ID: {}", accountId);
        return accountRepository.findByAccountId(accountId).map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(Long id, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", id);

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (request.getCurrentBalance() != null) {
            account.setCurrentBalance(request.getCurrentBalance());
        }

        if (request.getCreditLimit() != null) {
            account.setCreditLimit(request.getCreditLimit());
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long id) {
        log.info("Deleting account with ID: {}", id);
        if (!accountRepository.existsById(id)) {
            throw new IllegalArgumentException("Account not found");
        }
        accountRepository.deleteById(id);
        log.info("Account deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.info("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAccountsByCustomerId(Long customerId, Pageable pageable) {
        log.info("Retrieving accounts for customer ID: {}", customerId);
        return accountRepository.findByCustomer_CustomerId(customerId, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public boolean accountExists(Long accountId) {
        return accountRepository.existsByAccountId(accountId);
    }

    /**
     * BR003: Transaction Amount Summation
     * Calculate total transaction amount for an account
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalTransactionAmount(Long accountId) {
        log.info("Calculating total transaction amount for account ID: {}", accountId);
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return account.calculateTotalTransactionAmount();
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(account.getAccountId());
        response.setCurrentBalance(account.getCurrentBalance());
        response.setCreditLimit(account.getCreditLimit());
        response.setAvailableCredit(account.getAvailableCredit());
        response.setCustomerId(account.getCustomer().getCustomerId());
        response.setCustomerName(account.getCustomer().getFullName());
        response.setIsOverLimit(account.isOverLimit());
        response.setTotalTransactionAmount(account.calculateTotalTransactionAmount());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
