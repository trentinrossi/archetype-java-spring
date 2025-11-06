package com.example.demo.service;

import com.example.demo.dto.AccountResponseDto;
import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with account ID: {}", request.getAccountId());

        validateAccountId(request.getAccountId());

        Long accountIdLong = Long.parseLong(request.getAccountId());
        
        if (accountRepository.existsByAccountId(accountIdLong)) {
            throw new IllegalArgumentException("Account with ID already exists");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId()));

        Account account = new Account();
        account.setAccountId(accountIdLong);
        account.setAccountData(request.getAccountData());
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long accountId) {
        log.debug("Retrieving account by account ID: {}", accountId);
        return accountRepository.findByAccountId(accountId).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        log.debug("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(Long accountId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", accountId);

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        if (request.getAccountData() != null) {
            account.setAccountData(request.getAccountData());
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully with ID: {}", updatedAccount.getAccountId());
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        log.info("Deleting account with ID: {}", accountId);

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        accountRepository.delete(account);
        log.info("Account deleted successfully with ID: {}", accountId);
    }

    @Transactional(readOnly = true)
    public boolean existsByAccountId(Long accountId) {
        validateAccountIdLong(accountId);
        return accountRepository.existsByAccountId(accountId);
    }

    @Transactional(readOnly = true)
    public long countAccounts() {
        return accountRepository.count();
    }

    private void validateAccountId(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid account ID format: Account ID cannot be null or empty");
        }

        if (!accountId.matches("\\d{11}")) {
            throw new IllegalArgumentException("Invalid account ID format: Must be 11 numeric digits");
        }
    }

    private void validateAccountIdLong(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Invalid account ID format: Account ID cannot be null");
        }

        String accountIdStr = String.valueOf(accountId);

        if (accountIdStr.startsWith("-")) {
            throw new IllegalArgumentException("Invalid account ID format: Account ID must be positive");
        }

        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("Invalid account ID format: Must be 11 numeric digits");
        }
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAccountId(String.valueOf(account.getAccountId()));
        response.setAccountData(account.getAccountData());
        response.setCustomerId(account.getCustomer() != null ? account.getCustomer().getCustomerId() : null);
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
