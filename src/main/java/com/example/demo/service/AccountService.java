package com.example.demo.service;

import com.example.demo.dto.CreateAccountRequestDto;
import com.example.demo.dto.UpdateAccountRequestDto;
import com.example.demo.dto.AccountResponseDto;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAccountId());
        
        if (!isValidAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Invalid account ID format");
        }
        
        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new IllegalArgumentException("Account with ID already exists");
        }
        
        Account account = new Account();
        account.setAccountId(request.getAccountId());
        account.setAccountData(request.getAccountData());
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAccountId());
        return convertToResponse(savedAccount);
    }
    
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long id) {
        log.debug("Retrieving account by ID: {}", id);
        return accountRepository.findById(id).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountByAccountId(Long accountId) {
        log.debug("Retrieving account by account ID: {}", accountId);
        
        if (!isValidAccountId(accountId)) {
            throw new IllegalArgumentException("Invalid account ID format");
        }
        
        return accountRepository.findByAccountId(accountId).map(this::convertToResponse);
    }
    
    @Transactional
    public AccountResponseDto updateAccount(Long id, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (request.getAccountData() != null) {
            account.setAccountData(request.getAccountData());
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
        log.debug("Retrieving all accounts with pagination");
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public AccountResponseDto readAccountByKey(String accountIdKey, int keyLength) {
        log.info("Reading account by key with length: {}", keyLength);
        
        if (keyLength <= 0 || keyLength > 25) {
            throw new IllegalArgumentException("Key length must be positive and not exceed 25 characters");
        }
        
        String extractedKey = extractKey(accountIdKey, keyLength);
        
        if (!isValidAccountIdString(extractedKey)) {
            throw new IllegalArgumentException("Invalid account ID format");
        }
        
        Long accountId = Long.parseLong(extractedKey);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        log.info("Account retrieved successfully with account ID: {}", accountId);
        return convertToResponse(account);
    }
    
    private String extractKey(String keyField, int keyLength) {
        if (keyField == null || keyField.length() < keyLength) {
            throw new IllegalArgumentException("Key field length is insufficient");
        }
        return keyField.substring(0, keyLength);
    }
    
    private boolean isValidAccountId(Long accountId) {
        if (accountId == null) {
            return false;
        }
        String accountIdStr = String.valueOf(accountId);
        return accountIdStr.matches("\\d{11}");
    }
    
    private boolean isValidAccountIdString(String accountId) {
        if (accountId == null) {
            return false;
        }
        return accountId.matches("\\d{11}");
    }
    
    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setId(account.getId());
        response.setAccountId(account.getAccountId());
        response.setAccountData(account.getAccountData());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}