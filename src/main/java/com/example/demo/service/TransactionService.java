package com.example.demo.service;

import com.example.demo.dto.CreateTransactionRequestDto;
import com.example.demo.dto.UpdateTransactionRequestDto;
import com.example.demo.dto.TransactionResponseDto;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Card;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto request) {
        log.info("Creating new transaction with ID: {}", request.getTransactionId());

        if (transactionRepository.existsByTransactionId(request.getTransactionId())) {
            throw new IllegalArgumentException("Transaction ID must be 16 characters alphanumeric and unique");
        }

        Card card = cardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("Card number must be 16 characters alphanumeric"));

        // BR005: Transaction Table Capacity Limit - max 10 transactions per card
        if (!card.canAddTransaction()) {
            throw new IllegalStateException("Cannot add transaction: maximum capacity of 10 transactions reached");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        transaction.setTransactionTypeCode(request.getTransactionTypeCode());
        transaction.setTransactionCategoryCode(request.getTransactionCategoryCode());
        transaction.setTransactionSource(request.getTransactionSource());
        transaction.setTransactionDescription(request.getTransactionDescription());
        transaction.setTransactionAmount(request.getTransactionAmount());
        transaction.setCard(card);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully with ID: {}", savedTransaction.getTransactionId());
        return convertToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionById(Long id) {
        log.info("Retrieving transaction by ID: {}", id);
        return transactionRepository.findById(id).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<TransactionResponseDto> getTransactionByTransactionId(String transactionId) {
        log.info("Retrieving transaction by transaction ID: {}", transactionId);
        return transactionRepository.findByTransactionId(transactionId).map(this::convertToResponse);
    }

    @Transactional
    public TransactionResponseDto updateTransaction(Long id, UpdateTransactionRequestDto request) {
        log.info("Updating transaction with ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        if (request.getTransactionTypeCode() != null) {
            transaction.setTransactionTypeCode(request.getTransactionTypeCode());
        }

        if (request.getTransactionCategoryCode() != null) {
            transaction.setTransactionCategoryCode(request.getTransactionCategoryCode());
        }

        if (request.getTransactionSource() != null) {
            transaction.setTransactionSource(request.getTransactionSource());
        }

        if (request.getTransactionDescription() != null) {
            transaction.setTransactionDescription(request.getTransactionDescription());
        }

        if (request.getTransactionAmount() != null) {
            transaction.setTransactionAmount(request.getTransactionAmount());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        log.info("Transaction updated successfully with ID: {}", updatedTransaction.getTransactionId());
        return convertToResponse(updatedTransaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        log.info("Deleting transaction with ID: {}", id);
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction not found");
        }
        transactionRepository.deleteById(id);
        log.info("Transaction deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getAllTransactions(Pageable pageable) {
        log.info("Retrieving all transactions with pagination");
        return transactionRepository.findAll(pageable).map(this::convertToResponse);
    }

    /**
     * BR001: Transaction Grouping by Card
     * Get all transactions grouped by card number
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByCardNumber(String cardNumber, Pageable pageable) {
        log.info("Retrieving transactions for card number: {}", cardNumber);
        return transactionRepository.findByCard_CardNumber(cardNumber, pageable).map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDto> getTransactionsByAccountId(Long accountId, Pageable pageable) {
        log.info("Retrieving transactions for account ID: {}", accountId);
        return transactionRepository.findByAccountId(accountId, pageable).map(this::convertToResponse);
    }

    /**
     * BR003: Transaction Amount Summation
     * Calculate total transaction amount for a card
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalTransactionAmountByCard(String cardNumber) {
        log.info("Calculating total transaction amount for card: {}", cardNumber);
        BigDecimal total = transactionRepository.sumTransactionAmountsByCardNumber(cardNumber);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * BR003: Transaction Amount Summation
     * Calculate total transaction amount for an account
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalTransactionAmountByAccount(Long accountId) {
        log.info("Calculating total transaction amount for account ID: {}", accountId);
        BigDecimal total = transactionRepository.sumTransactionAmountsByAccountId(accountId);
        return total != null ? total : BigDecimal.ZERO;
    }

    private TransactionResponseDto convertToResponse(Transaction transaction) {
        TransactionResponseDto response = new TransactionResponseDto();
        response.setId(transaction.getId());
        response.setCardNumber(transaction.getCardNumber());
        response.setTransactionId(transaction.getTransactionId());
        response.setTransactionTypeCode(transaction.getTransactionTypeCode());
        response.setTransactionCategoryCode(transaction.getTransactionCategoryCode());
        response.setTransactionSource(transaction.getTransactionSource());
        response.setTransactionDescription(transaction.getTransactionDescription());
        response.setTransactionAmount(transaction.getTransactionAmount());
        response.setFormattedAmount(transaction.getFormattedAmount());
        response.setIsDebit(transaction.isDebit());
        response.setIsCredit(transaction.isCredit());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
