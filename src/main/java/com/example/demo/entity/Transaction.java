package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, length = 16)
    private String transactionId;

    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

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

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "merchant_name", length = 50)
    private String merchantName;

    @Column(name = "merchant_city", length = 50)
    private String merchantCity;

    @Column(name = "merchant_zip", length = 10)
    private String merchantZip;

    @Column(name = "original_timestamp", nullable = false)
    private LocalDateTime originalTimestamp;

    @Column(name = "processing_timestamp", nullable = false)
    private LocalDateTime processingTimestamp;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "account_data", length = 318)
    private String accountData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_number", referencedColumnName = "card_number", insertable = false, updatable = false)
    private Card card;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateTransaction() {
        if (transactionId == null || transactionId.length() != 16) {
            throw new IllegalArgumentException("Transaction ID must be 16 characters");
        }
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        if (transactionAmount == null) {
            throw new IllegalArgumentException("Transaction amount is required");
        }
    }

    public boolean isInterestTransaction() {
        return "01".equals(transactionTypeCode) && "05".equals(transactionCategoryCode);
    }

    public boolean isSystemGenerated() {
        return "System".equalsIgnoreCase(transactionSource);
    }
}
