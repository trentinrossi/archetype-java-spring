package com.example.demo.controller;

import com.example.demo.dto.AccountDTO;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountService accountService;
    
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable String accountId) {
        AccountDTO account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }
    
    @GetMapping("/status/{activeStatus}")
    public ResponseEntity<List<AccountDTO>> getAccountsByStatus(@PathVariable String activeStatus) {
        List<AccountDTO> accounts = accountService.getAccountsByStatus(activeStatus);
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByGroupId(@PathVariable String groupId) {
        List<AccountDTO> accounts = accountService.getAccountsByGroupId(groupId);
        return ResponseEntity.ok(accounts);
    }
    
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
    
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable String accountId,
            @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);
        return ResponseEntity.ok(updatedAccount);
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
