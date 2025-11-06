package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Transaction.TransactionId.class)
public class Transaction {
    
    @Id
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;
    
    @Id
    @Column(name = "transaction_id", nullable = false, length = 16)
    private String transactionId;
    
    @Column(name = "transaction_data", nullable = false, length = 318)
    private String transactionData;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Transaction(String cardNumber, String transactionId, String transactionData) {
        this.cardNumber = cardNumber;
        this.transactionId = transactionId;
        this.transactionData = transactionData;
    }
    
    @PrePersist
    @PreUpdate
    private void validateFields() {
        if (cardNumber == null || cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number length");
        }
        if (transactionId == null || transactionId.length() != 16) {
            throw new IllegalArgumentException("Invalid transaction ID length");
        }
        if (transactionData == null || transactionData.isEmpty()) {
            throw new IllegalArgumentException("Transaction data cannot be null or empty");
        }
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionId implements Serializable {
        private String cardNumber;
        private String transactionId;
    }
}