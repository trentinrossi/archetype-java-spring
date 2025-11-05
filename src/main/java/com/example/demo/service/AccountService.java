package com.example.demo.service;

import com.example.demo.dto.AccountCreateDTO;
import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountUpdateDTO;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    @Transactional(readOnly = true)
    public AccountDTO getAccountById(String accountId) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        return mapToDTO(account);
    }
    
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByStatus(String status) {
        return accountRepository.findByActiveStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public AccountDTO createAccount(AccountCreateDTO createDTO) {
        if (accountRepository.existsByAccountId(createDTO.getAccountId())) {
            throw new RuntimeException("Account already exists: " + createDTO.getAccountId());
        }
        
        Account account = new Account();
        account.setAccountId(createDTO.getAccountId());
        account.setActiveStatus(createDTO.getActiveStatus());
        account.setCurrentBalance(BigDecimal.ZERO);
        account.setCreditLimit(createDTO.getCreditLimit());
        account.setCashCreditLimit(createDTO.getCashCreditLimit());
        account.setOpenDate(createDTO.getOpenDate());
        account.setExpirationDate(createDTO.getExpirationDate());
        account.setReissueDate(createDTO.getReissueDate());
        account.setCurrentCycleCredit(BigDecimal.ZERO);
        account.setCurrentCycleDebit(BigDecimal.ZERO);
        account.setGroupId(createDTO.getGroupId());
        
        Account saved = accountRepository.save(account);
        return mapToDTO(saved);
    }
    
    @Transactional
    public AccountDTO updateAccount(String accountId, AccountUpdateDTO updateDTO) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        
        account.setActiveStatus(updateDTO.getActiveStatus());
        account.setCurrentBalance(updateDTO.getCurrentBalance());
        account.setCreditLimit(updateDTO.getCreditLimit());
        account.setCashCreditLimit(updateDTO.getCashCreditLimit());
        account.setOpenDate(updateDTO.getOpenDate());
        account.setExpirationDate(updateDTO.getExpirationDate());
        account.setReissueDate(updateDTO.getReissueDate());
        account.setCurrentCycleCredit(updateDTO.getCurrentCycleCredit());
        account.setCurrentCycleDebit(updateDTO.getCurrentCycleDebit());
        account.setGroupId(updateDTO.getGroupId());
        
        Account saved = accountRepository.save(account);
        return mapToDTO(saved);
    }
    
    @Transactional
    public void deleteAccount(String accountId) {
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new RuntimeException("Account not found: " + accountId);
        }
        accountRepository.deleteById(accountId);
    }
    
    @Transactional
    public void updateBalance(String accountId, BigDecimal amount) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
        
        BigDecimal newBalance = account.getCurrentBalance().add(amount);
        account.setCurrentBalance(newBalance);
        accountRepository.save(account);
    }
    
    private AccountDTO mapToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        dto.setActiveStatus(account.getActiveStatus());
        dto.setCurrentBalance(account.getCurrentBalance());
        dto.setCreditLimit(account.getCreditLimit());
        dto.setCashCreditLimit(account.getCashCreditLimit());
        dto.setOpenDate(account.getOpenDate());
        dto.setExpirationDate(account.getExpirationDate());
        dto.setReissueDate(account.getReissueDate());
        dto.setCurrentCycleCredit(account.getCurrentCycleCredit());
        dto.setCurrentCycleDebit(account.getCurrentCycleDebit());
        dto.setGroupId(account.getGroupId());
        dto.setAvailableCredit(account.getAvailableCredit());
        dto.setActive(account.isActive());
        dto.setExpired(account.isExpired());
        return dto;
    }
}
