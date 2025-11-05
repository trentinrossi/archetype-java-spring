package com.example.demo.service;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    @Transactional(readOnly = true)
    public AccountDTO getAccountById(String accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        return mapToDTO(account);
    }
    
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByCustomerId(String customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountRepository.existsByAccountId(accountDTO.getAccountId())) {
            throw new IllegalArgumentException("Account already exists: " + accountDTO.getAccountId());
        }
        
        Account account = mapToEntity(accountDTO);
        account = accountRepository.save(account);
        
        log.info("Account created: {}", account.getAccountId());
        
        return mapToDTO(account);
    }
    
    @Transactional
    public AccountDTO updateAccount(String accountId, AccountDTO accountDTO) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        
        account.setCurrentBalance(accountDTO.getCurrentBalance());
        account.setCreditLimit(accountDTO.getCreditLimit());
        account.setCurrentCycleCredit(accountDTO.getCurrentCycleCredit());
        account.setCurrentCycleDebit(accountDTO.getCurrentCycleDebit());
        account.setExpirationDate(accountDTO.getExpirationDate());
        account.setAccountStatus(accountDTO.getAccountStatus());
        
        account = accountRepository.save(account);
        
        log.info("Account updated: {}", accountId);
        
        return mapToDTO(account);
    }
    
    private Account mapToEntity(AccountDTO dto) {
        Account entity = new Account();
        entity.setAccountId(dto.getAccountId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setCurrentBalance(dto.getCurrentBalance());
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setCurrentCycleCredit(dto.getCurrentCycleCredit());
        entity.setCurrentCycleDebit(dto.getCurrentCycleDebit());
        entity.setExpirationDate(dto.getExpirationDate());
        entity.setAccountStatus(dto.getAccountStatus());
        return entity;
    }
    
    private AccountDTO mapToDTO(Account entity) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(entity.getAccountId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setCurrentBalance(entity.getCurrentBalance());
        dto.setCreditLimit(entity.getCreditLimit());
        dto.setCurrentCycleCredit(entity.getCurrentCycleCredit());
        dto.setCurrentCycleDebit(entity.getCurrentCycleDebit());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setAccountStatus(entity.getAccountStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
