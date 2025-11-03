package com.example.demo.service;

import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.AccountStatementDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.repository.CardCrossReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {
    
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    
    @Transactional(readOnly = true)
    public List<AccountStatementDTO> generateAllStatements() {
        List<AccountStatementDTO> statements = new ArrayList<>();
        List<CardCrossReference> crossReferences = cardCrossReferenceRepository.findAll();
        
        for (CardCrossReference crossRef : crossReferences) {
            AccountStatementDTO statement = generateStatement(
                crossRef.getCardNumber(),
                crossRef.getCustomerId(),
                crossRef.getAccountId()
            );
            statements.add(statement);
        }
        
        return statements;
    }
    
    @Transactional(readOnly = true)
    public AccountStatementDTO generateStatementByCardNumber(String cardNumber) {
        CardCrossReference crossRef = cardCrossReferenceRepository.findById(cardNumber)
            .orElseThrow(() -> new RuntimeException("Card cross reference not found for card: " + cardNumber));
        
        return generateStatement(
            crossRef.getCardNumber(),
            crossRef.getCustomerId(),
            crossRef.getAccountId()
        );
    }
    
    @Transactional(readOnly = true)
    public List<AccountStatementDTO> generateStatementsByAccountId(String accountId) {
        List<AccountStatementDTO> statements = new ArrayList<>();
        List<CardCrossReference> crossRefs = cardCrossReferenceRepository.findByAccountId(accountId);
        
        for (CardCrossReference crossRef : crossRefs) {
            AccountStatementDTO statement = generateStatement(
                crossRef.getCardNumber(),
                crossRef.getCustomerId(),
                crossRef.getAccountId()
            );
            statements.add(statement);
        }
        
        return statements;
    }
    
    private AccountStatementDTO generateStatement(String cardNumber, String customerId, String accountId) {
        CustomerDTO customer = customerService.getCustomerById(customerId);
        AccountDTO account = accountService.getAccountById(accountId);
        List<TransactionDTO> transactions = transactionService.getTransactionsByCardNumber(cardNumber);
        
        BigDecimal totalAmount = transactions.stream()
            .map(TransactionDTO::getTransactionAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        AccountStatementDTO statement = new AccountStatementDTO();
        statement.setCustomer(customer);
        statement.setAccount(account);
        statement.setTransactions(transactions);
        statement.setTotalAmount(totalAmount);
        
        return statement;
    }
}
