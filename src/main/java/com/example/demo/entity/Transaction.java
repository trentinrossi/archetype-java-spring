package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_card_number", columnList = "card_number"),
    @Index(name = "idx_transaction_id", columnList = "transaction_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_id", nullable = false, unique = true, length = 16)
    private String transactionId;
    
    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;
    
    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private String transactionCategoryCode;
    
    @Column(name = "transaction_source", nullable = false, length = 10)
    private String transactionSource;
    
    @Column(name = "transaction_description", nullable = false, length = 100)
    private String transactionDescription;
    
    @Column(name = "transaction_amount", nullable = false, precision = 11, scale = 2)
    private BigDecimal transactionAmount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_number", nullable = false)
    private Card card;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Transaction(String transactionId, String transactionTypeCode, 
                      String transactionCategoryCode, String transactionSource, 
                      String transactionDescription, BigDecimal transactionAmount, Card card) {
        this.transactionId = transactionId;
        this.transactionTypeCode = transactionTypeCode;
        this.transactionCategoryCode = transactionCategoryCode;
        this.transactionSource = transactionSource;
        this.transactionDescription = transactionDescription;
        this.transactionAmount = transactionAmount;
        this.card = card;
    }
    
    @PrePersist
    @PreUpdate
    private void validateTransaction() {
        validateCardNumber();
        validateTransactionId();
        validateTransactionAmount();
    }
    
    private void validateCardNumber() {
        if (card == null || card.getCardNumber() == null || !card.getCardNumber().matches("^[a-zA-Z0-9]{16}$")) {
            throw new IllegalArgumentException("Card number must be 16 characters alphanumeric");
        }
    }
    
    private void validateTransactionId() {
        if (transactionId == null || !transactionId.matches("^[a-zA-Z0-9]{16}$")) {
            throw new IllegalArgumentException("Transaction ID must be 16 characters alphanumeric and unique");
        }
    }
    
    private void validateTransactionAmount() {
        if (transactionAmount == null) {
            throw new IllegalArgumentException("Transaction amount must be valid signed numeric with 2 decimal places");
        }
        
        if (transactionAmount.scale() > 2) {
            throw new IllegalArgumentException("Transaction amount must have maximum 2 decimal places");
        }
        
        if (transactionAmount.precision() - transactionAmount.scale() > 9) {
            throw new IllegalArgumentException("Transaction amount exceeds maximum precision");
        }
    }
    
    /**
     * BR001: Transaction Grouping by Card
     * Validates if transaction is valid for grouping by card number
     */
    public boolean isValidForGrouping() {
        return card != null && card.getCardNumber() != null && card.getCardNumber().matches("^[a-zA-Z0-9]{16}$");
    }
    
    /**
     * Get absolute value of transaction amount
     */
    public BigDecimal getAbsoluteAmount() {
        return transactionAmount != null ? transactionAmount.abs() : BigDecimal.ZERO;
    }
    
    /**
     * Check if transaction is a debit (negative amount)
     */
    public boolean isDebit() {
        return transactionAmount != null && transactionAmount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Check if transaction is a credit (positive amount)
     */
    public boolean isCredit() {
        return transactionAmount != null && transactionAmount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Get formatted amount string with 2 decimal places
     */
    public String getFormattedAmount() {
        if (transactionAmount == null) {
            return "0.00";
        }
        return String.format("%.2f", transactionAmount);
    }
    
    /**
     * Get card number from associated card
     */
    public String getCardNumber() {
        return card != null ? card.getCardNumber() : null;
    }
}
