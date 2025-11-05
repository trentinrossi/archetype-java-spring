package com.example.demo.service;

import com.example.demo.dto.BillPaymentDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Card;
import com.example.demo.entity.CardCrossReference;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CardCrossReferenceRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CardCrossReferenceRepository cardCrossReferenceRepository;
    
    @Transactional(readOnly = true)
    public TransactionDTO getTransactionById(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + transactionId));
        return mapToDTO(transaction);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCardNumber(String cardNumber) {
        return transactionRepository.findByCardNumberOrderByOriginationTimestampDesc(cardNumber).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public BillPaymentDTO processBillPayment(BillPaymentDTO paymentDTO) {
        // Validate account exists
        Account account = accountRepository.findByAccountId(paymentDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + paymentDTO.getAccountId()));
        
        // Check if there's a balance to pay
        if (account.getCurrentBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("You have nothing to pay");
        }
        
        // Get card number from cross-reference
        CardCrossReference crossRef = cardCrossReferenceRepository.findByAccountId(paymentDTO.getAccountId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No card found for account: " + paymentDTO.getAccountId()));
        
        // Generate new transaction ID
        String newTransactionId = generateNextTransactionId();
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(newTransactionId);
        transaction.setCardNumber(crossRef.getCardNumber());
        transaction.setTypeCode("02");
        transaction.setCategoryCode(2);
        transaction.setSource("POS TERM");
        transaction.setDescription("BILL PAYMENT - ONLINE");
        transaction.setAmount(account.getCurrentBalance());
        transaction.setMerchantId("999999999");
        transaction.setMerchantName("BILL PAYMENT");
        transaction.setMerchantCity("N/A");
        transaction.setMerchantZip("N/A");
        
        LocalDateTime now = LocalDateTime.now();
        transaction.setOriginationTimestamp(now);
        transaction.setProcessingTimestamp(now);
        
        transactionRepository.save(transaction);
        
        // Update account balance
        BigDecimal newBalance = account.getCurrentBalance().subtract(transaction.getAmount());
        account.setCurrentBalance(newBalance);
        accountRepository.save(account);
        
        // Prepare response
        BillPaymentDTO response = new BillPaymentDTO();
        response.setAccountId(paymentDTO.getAccountId());
        response.setCurrentBalance(BigDecimal.ZERO);
        response.setPaymentAmount(transaction.getAmount());
        response.setTransactionId(newTransactionId);
        response.setConfirmPayment("Y");
        
        return response;
    }
    
    private String generateNextTransactionId() {
        Transaction lastTransaction = transactionRepository.findTopByOrderByTransactionIdDesc()
                .orElse(null);
        
        if (lastTransaction == null) {
            return "0000000000000001";
        }
        
        long lastId = Long.parseLong(lastTransaction.getTransactionId());
        long nextId = lastId + 1;
        return String.format("%016d", nextId);
    }
    
    private TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setCardNumber(transaction.getCardNumber());
        dto.setTypeCode(transaction.getTypeCode());
        dto.setCategoryCode(transaction.getCategoryCode());
        dto.setSource(transaction.getSource());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setMerchantId(transaction.getMerchantId());
        dto.setMerchantName(transaction.getMerchantName());
        dto.setMerchantCity(transaction.getMerchantCity());
        dto.setMerchantZip(transaction.getMerchantZip());
        dto.setOriginationTimestamp(transaction.getOriginationTimestamp());
        dto.setProcessingTimestamp(transaction.getProcessingTimestamp());
        return dto;
    }
}
