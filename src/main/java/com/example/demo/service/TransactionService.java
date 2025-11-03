package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));
        return convertToDTO(transaction);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCardNumber(String cardNumber) {
        return transactionRepository.findByCardNumberOrderByOriginalTimestampDesc(cardNumber).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByAccountId(String accountId) {
        return transactionRepository.findByAccountIdOrderByOriginalTimestampDesc(accountId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = convertToEntity(transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }
    
    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setCardNumber(transaction.getCardNumber());
        dto.setAccountId(transaction.getAccountId());
        dto.setTransactionTypeCode(transaction.getTransactionTypeCode());
        dto.setTransactionCategoryCode(transaction.getTransactionCategoryCode());
        dto.setTransactionSource(transaction.getTransactionSource());
        dto.setTransactionDescription(transaction.getTransactionDescription());
        dto.setTransactionAmount(transaction.getTransactionAmount());
        dto.setMerchantId(transaction.getMerchantId());
        dto.setMerchantName(transaction.getMerchantName());
        dto.setMerchantCity(transaction.getMerchantCity());
        dto.setMerchantZip(transaction.getMerchantZip());
        dto.setOriginalTimestamp(transaction.getOriginalTimestamp());
        dto.setProcessingTimestamp(transaction.getProcessingTimestamp());
        return dto;
    }
    
    private Transaction convertToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(dto.getTransactionId());
        transaction.setCardNumber(dto.getCardNumber());
        transaction.setAccountId(dto.getAccountId());
        transaction.setTransactionTypeCode(dto.getTransactionTypeCode());
        transaction.setTransactionCategoryCode(dto.getTransactionCategoryCode());
        transaction.setTransactionSource(dto.getTransactionSource());
        transaction.setTransactionDescription(dto.getTransactionDescription());
        transaction.setTransactionAmount(dto.getTransactionAmount());
        transaction.setMerchantId(dto.getMerchantId());
        transaction.setMerchantName(dto.getMerchantName());
        transaction.setMerchantCity(dto.getMerchantCity());
        transaction.setMerchantZip(dto.getMerchantZip());
        transaction.setOriginalTimestamp(dto.getOriginalTimestamp());
        transaction.setProcessingTimestamp(dto.getProcessingTimestamp());
        return transaction;
    }
}
