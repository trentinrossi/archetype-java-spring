package com.example.demo.controller;

import com.example.demo.dto.AccountStatementDTO;
import com.example.demo.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
public class StatementController {
    
    private final StatementService statementService;
    
    @GetMapping
    public ResponseEntity<List<AccountStatementDTO>> generateAllStatements() {
        List<AccountStatementDTO> statements = statementService.generateAllStatements();
        return ResponseEntity.ok(statements);
    }
    
    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<AccountStatementDTO> generateStatementByCardNumber(
            @PathVariable String cardNumber) {
        AccountStatementDTO statement = statementService.generateStatementByCardNumber(cardNumber);
        return ResponseEntity.ok(statement);
    }
    
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<AccountStatementDTO>> generateStatementsByAccountId(
            @PathVariable String accountId) {
        List<AccountStatementDTO> statements = statementService.generateStatementsByAccountId(accountId);
        return ResponseEntity.ok(statements);
    }
}
