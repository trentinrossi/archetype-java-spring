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
        
        if (request.getCardNumber() == null || request.getCardNumber().length() != 16) {
            throw new IllegalArgumentException("Invalid card number length - must be exactly 16 characters");
        }
        
        if (request.getTransactionId() == null || request.getTransactionId().length() != 16) {
            throw new IllegalArgumentException("Invalid transaction ID length - must be exactly 16 characters");
        }
        
        if (transactionRepository.existsByCardNumberAndTransactionId(
                request.getCardNumber(), request.getTransactionId())) {
            throw new IllegalArgumentException("Transaction with this card number and transaction ID already exists");
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
    public Optional<TransactionResponseDto> getTransactionByCompositeKey(
            String cardNumber, String transactionId) {
        log.debug("Retrieving transaction by card number: {} and transaction ID: {}", 
                cardNumber, transactionId);
        
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number length - must be exactly 16 characters");
        }
        
        if (transactionId == null || transactionId.length() != 16) {
            throw new IllegalArgumentException("Invalid transaction ID length - must be exactly 16 characters");
        }
        
        return transactionRepository.findByCardNumberAndTransactionId(cardNumber, transactionId)
                .map(this::convertToResponse);
    }
    
    @Transactional
    public TransactionResponseDto updateTransaction(String cardNumber, String transactionId, 
                                                   UpdateTransactionRequestDto request) {
        log.info("Updating transaction with card number: {} and transaction ID: {}", cardNumber, transactionId);
        
        Transaction transaction = transactionRepository.findByCardNumberAndTransactionId(cardNumber, transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        
        if (request.getTransactionData() != null) {
            transaction.setTransactionData(request.getTransactionData());
        }
        
        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction updated successfully");
        return convertToResponse(updatedTransaction);
    }
    
    @Transactional
    public void deleteTransaction(String cardNumber, String transactionId) {
        log.info("Deleting transaction with card number: {} and transaction ID: {}", cardNumber, transactionId);
        
        if (!transactionRepository.existsByCardNumberAndTransactionId(cardNumber, transactionId)) {
            throw new IllegalArgumentException("Transaction not found");
        }
        
        Transaction.TransactionId id = new Transaction.TransactionId(cardNumber, transactionId);
        transactionRepository.deleteById(id);
        log.info("Transaction deleted successfully");
    }
    
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.debug("Retrieving all transactions with pagination");
        return transactionRepository.findAllOrderByCompositeKey(pageable).map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber) {
        log.debug("Retrieving transactions by card number: {}", cardNumber);
        
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number length - must be exactly 16 characters");
        }
        
        return transactionRepository.findByCardNumberOrderByTransactionIdAsc(cardNumber)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber, Pageable pageable) {
        log.debug("Retrieving transactions by card number: {} with pagination", cardNumber);
        
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number length - must be exactly 16 characters");
        }
        
        return transactionRepository.findByCardNumber(cardNumber, pageable)
                .map(this::convertToResponse);
    }
    
    @Transactional(readOnly = true)
    public long countTransactionsByCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number length - must be exactly 16 characters");
        }
        return transactionRepository.countByCardNumber(cardNumber);
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