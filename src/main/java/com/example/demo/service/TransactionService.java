package com.example.demo.service;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        log.info("Creating new transaction with card number: {} and transaction ID: {}", 
                request.getCardNumber(), request.getTransactionId());
        
        validateCardNumber(request.getCardNumber());
        validateTransactionId(request.getTransactionId());
        
        if (transactionRepository.existsByCardNumberAndTransactionId(
                request.getCardNumber(), request.getTransactionId())) {
            throw new IllegalArgumentException(
                    "Transaction already exists for card number: " + request.getCardNumber() + 
                    " and transaction ID: " + request.getTransactionId());
        }
        
        Transaction transaction = new Transaction();
        transaction.setCardNumber(request.getCardNumber());
        transaction.setTransactionId(request.getTransactionId());
        transaction.setTransactionData(request.getTransactionData());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully");
        
        return convertToResponse(savedTransaction);
    }
    
    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionByCardNumberAndTransactionId(
            String cardNumber, String transactionId) {
        log.debug("Retrieving transaction by card number: {} and transaction ID: {}", 
                cardNumber, transactionId);
        
        validateCardNumber(cardNumber);
        validateTransactionId(transactionId);
        
        return transactionRepository.findByCardNumberAndTransactionId(cardNumber, transactionId)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.debug("Retrieving all transactions with pagination");
        return transactionRepository.findAllOrderByCompositeKey(pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber) {
        log.debug("Retrieving transactions for card number: {}", cardNumber);
        
        validateCardNumber(cardNumber);
        
        return transactionRepository.findByCardNumberOrderByTransactionIdAsc(cardNumber)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TransactionResponseDto updateTransaction(String cardNumber, String transactionId, 
                                                    UpdateTransactionRequestDto request) {
        log.info("Updating transaction with card number: {} and transaction ID: {}", 
                cardNumber, transactionId);
        
        validateCardNumber(cardNumber);
        validateTransactionId(transactionId);
        
        Transaction transaction = transactionRepository.findByCardNumberAndTransactionId(
                cardNumber, transactionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Transaction not found for card number: " + cardNumber + 
                        " and transaction ID: " + transactionId));
        
        if (request.getTransactionData() != null) {
            transaction.setTransactionData(request.getTransactionData());
        }
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction updated successfully");
        
        return convertToResponse(updatedTransaction);
    }
    
    @Transactional
    public void deleteTransaction(String cardNumber, String transactionId) {
        log.info("Deleting transaction with card number: {} and transaction ID: {}", 
                cardNumber, transactionId);
        
        validateCardNumber(cardNumber);
        validateTransactionId(transactionId);
        
        Transaction transaction = transactionRepository.findByCardNumberAndTransactionId(
                cardNumber, transactionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Transaction not found for card number: " + cardNumber + 
                        " and transaction ID: " + transactionId));
        
        transactionRepository.delete(transaction);
        log.info("Transaction deleted successfully");
    }
    
    @Transactional(readOnly = true)
    public long countTransactionsByCardNumber(String cardNumber) {
        log.debug("Counting transactions for card number: {}", cardNumber);
        
        validateCardNumber(cardNumber);
        
        return transactionRepository.countByCardNumber(cardNumber);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByCardNumberAndTransactionId(String cardNumber, String transactionId) {
        log.debug("Checking existence of transaction with card number: {} and transaction ID: {}", 
                cardNumber, transactionId);
        
        validateCardNumber(cardNumber);
        validateTransactionId(transactionId);
        
        return transactionRepository.existsByCardNumberAndTransactionId(cardNumber, transactionId);
    }
    
    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        
        if (cardNumber.length() != 16) {
            throw new IllegalArgumentException("Card number must be exactly 16 characters");
        }
    }
    
    private void validateTransactionId(String transactionId) {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID is required");
        }
        
        if (transactionId.length() != 16) {
            throw new IllegalArgumentException("Transaction ID must be exactly 16 characters");
        }
    }
    
    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setCardNumber(transaction.getCardNumber());
        response.setTransactionId(transaction.getTransactionId());
        response.setTransactionData(transaction.getTransactionData());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
