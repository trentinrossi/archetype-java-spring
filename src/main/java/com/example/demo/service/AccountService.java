package com.example.demo.service;

import com.example.demo.dto.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public AccountDTO getAccountById(String accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        return convertToDTO(account);
    }
    
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByStatus(String activeStatus) {
        return accountRepository.findByActiveStatus(activeStatus).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByGroupId(String groupId) {
        return accountRepository.findByGroupId(groupId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = convertToEntity(accountDTO);
        Account savedAccount = accountRepository.save(account);
        return convertToDTO(savedAccount);
    }
    
    @Transactional
    public AccountDTO updateAccount(String accountId, AccountDTO accountDTO) {
        Account existingAccount = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        existingAccount.setActiveStatus(accountDTO.getActiveStatus());
        existingAccount.setCurrentBalance(accountDTO.getCurrentBalance());
        existingAccount.setCreditLimit(accountDTO.getCreditLimit());
        existingAccount.setCashCreditLimit(accountDTO.getCashCreditLimit());
        existingAccount.setOpenDate(accountDTO.getOpenDate());
        existingAccount.setExpirationDate(accountDTO.getExpirationDate());
        existingAccount.setReissueDate(accountDTO.getReissueDate());
        existingAccount.setCurrentCycleCredit(accountDTO.getCurrentCycleCredit());
        existingAccount.setCurrentCycleDebit(accountDTO.getCurrentCycleDebit());
        existingAccount.setAddressZipCode(accountDTO.getAddressZipCode());
        existingAccount.setGroupId(accountDTO.getGroupId());
        
        Account updatedAccount = accountRepository.save(existingAccount);
        return convertToDTO(updatedAccount);
    }
    
    @Transactional
    public void deleteAccount(String accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
        accountRepository.deleteById(accountId);
    }
    
    private AccountDTO convertToDTO(Account account) {
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
        dto.setAddressZipCode(account.getAddressZipCode());
        dto.setGroupId(account.getGroupId());
        return dto;
    }
    
    private Account convertToEntity(AccountDTO dto) {
        Account account = new Account();
        account.setAccountId(dto.getAccountId());
        account.setActiveStatus(dto.getActiveStatus());
        account.setCurrentBalance(dto.getCurrentBalance());
        account.setCreditLimit(dto.getCreditLimit());
        account.setCashCreditLimit(dto.getCashCreditLimit());
        account.setOpenDate(dto.getOpenDate());
        account.setExpirationDate(dto.getExpirationDate());
        account.setReissueDate(dto.getReissueDate());
        account.setCurrentCycleCredit(dto.getCurrentCycleCredit());
        account.setCurrentCycleDebit(dto.getCurrentCycleDebit());
        account.setAddressZipCode(dto.getAddressZipCode());
        account.setGroupId(dto.getGroupId());
        return account;
    }
}
