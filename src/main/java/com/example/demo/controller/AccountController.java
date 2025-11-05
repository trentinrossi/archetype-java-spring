package com.example.demo.controller;

import com.example.demo.dto.AccountCreateDTO;
import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountUpdateDTO;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
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
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccountDTO>> getAccountsByStatus(@PathVariable String status) {
        List<AccountDTO> accounts = accountService.getAccountsByStatus(status);
        return ResponseEntity.ok(accounts);
    }
    
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountCreateDTO createDTO) {
        AccountDTO created = accountService.createAccount(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable String accountId,
            @Valid @RequestBody AccountUpdateDTO updateDTO) {
        AccountDTO updated = accountService.updateAccount(accountId, updateDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
