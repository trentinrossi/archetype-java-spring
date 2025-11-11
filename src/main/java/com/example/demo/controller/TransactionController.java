package com.example.demo.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Retrieving all transactions with pagination");
        Page<TransactionResponseDto> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        log.info("Retrieving transaction by ID: {}", id);
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/transaction-id/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionByTransactionId(@PathVariable String transactionId) {
        log.info("Retrieving transaction by transaction ID: {}", transactionId);
        return transactionService.getTransactionByTransactionId(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/card/{cardNumber}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByCardNumber(
            @PathVariable String cardNumber,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Retrieving transactions for card number: {}", cardNumber);
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByCardNumber(cardNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByAccountId(
            @PathVariable Long accountId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Retrieving transactions for account ID: {}", accountId);
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByAccountId(accountId, pageable);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Valid @RequestBody CreateTransactionRequestDto request) {
        log.info("Creating new transaction with transaction ID: {}", request.getTransactionId());
        TransactionResponseDto response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {}", id);
        TransactionResponseDto response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.info("Deleting transaction with ID: {}", id);
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/card/{cardNumber}/total-amount")
    public ResponseEntity<BigDecimal> getTotalTransactionAmountByCard(@PathVariable String cardNumber) {
        log.info("Calculating total transaction amount for card: {}", cardNumber);
        BigDecimal total = transactionService.calculateTotalTransactionAmountByCard(cardNumber);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/account/{accountId}/total-amount")
    public ResponseEntity<BigDecimal> getTotalTransactionAmountByAccount(@PathVariable Long accountId) {
        log.info("Calculating total transaction amount for account ID: {}", accountId);
        BigDecimal total = transactionService.calculateTotalTransactionAmountByAccount(accountId);
        return ResponseEntity.ok(total);
    }
}
