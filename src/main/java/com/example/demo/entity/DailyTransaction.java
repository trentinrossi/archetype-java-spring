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
@Table(name = "daily_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyTransaction {

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

    @Column(name = "merchant_name", nullable = false, length = 50)
    private String merchantName;

    @Column(name = "merchant_city", nullable = false, length = 50)
    private String merchantCity;

    @Column(name = "merchant_zip", nullable = false, length = 10)
    private String merchantZip;

    @Column(name = "original_timestamp", nullable = false, length = 26)
    private String originalTimestamp;

    @Column(name = "processing_timestamp", nullable = false, length = 26)
    private String processingTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_number", referencedColumnName = "card_number", insertable = false, updatable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", referencedColumnName = "merchant_id", insertable = false, updatable = false)
    private Merchant merchant;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (transactionId == null || transactionId.length() != 16) {
            throw new IllegalArgumentException("Transaction ID must be 16 characters");
        }
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
    }
}
