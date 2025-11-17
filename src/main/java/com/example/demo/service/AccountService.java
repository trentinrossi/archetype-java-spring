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

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * BR002: Account ID Validation - Account ID must be numeric and exist in the system
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto request) {
        log.info("Creating new account with ID: {}", request.getAcctId());
        
        if (accountRepository.existsByAcctId(request.getAcctId())) {
            throw new IllegalArgumentException("Account ID already exists");
        }
        
        Account account = new Account();
        account.setAcctId(request.getAcctId());
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getAcctId());
        
        return convertToResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> getAccountById(Long acctId) {
        log.info("Retrieving account with ID: {}", acctId);
        
        validateAccountId(acctId);
        
        return accountRepository.findByAcctId(acctId)
                .map(this::convertToResponse);
    }

    @Transactional
    public AccountResponseDto updateAccount(Long acctId, UpdateAccountRequestDto request) {
        log.info("Updating account with ID: {}", acctId);
        
        Account account = accountRepository.findByAcctId(acctId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        
        if (request.getAcctId() != null) {
            account.setAcctId(request.getAcctId());
        }
        
        Account updatedAccount = accountRepository.save(account);
        return convertToResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long acctId) {
        log.info("Deleting account with ID: {}", acctId);
        
        if (!accountRepository.existsByAcctId(acctId)) {
            throw new IllegalArgumentException("Account not found");
        }
        
        accountRepository.deleteById(acctId);
    }

    @Transactional(readOnly = true)
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * BR014: Account-Card Cross-Reference - Validate account exists in CXACAIX file
     */
    public void validateAccountExists(Long acctId) {
        log.info("BR002: Validating account ID: {}", acctId);
        
        validateAccountId(acctId);
        
        if (!accountRepository.existsByAcctId(acctId)) {
            throw new IllegalArgumentException("Account ID must exist in CXACAIX file");
        }
    }

    private void validateAccountId(Long acctId) {
        if (acctId == null) {
            throw new IllegalArgumentException("Account ID must be entered");
        }
        if (acctId <= 0) {
            throw new IllegalArgumentException("Account ID must be a positive number");
        }
    }

    private AccountResponseDto convertToResponse(Account account) {
        AccountResponseDto response = new AccountResponseDto();
        response.setAcctId(account.getAcctId());
        
        if (account.getCards() != null) {
            response.setCardNumbers(account.getCards().stream()
                    .map(card -> card.getCardNum())
                    .collect(Collectors.toList()));
        }
        
        response.setTransactionCount(account.getTransactions() != null ? account.getTransactions().size() : 0);
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        
        return response;
    }
}
