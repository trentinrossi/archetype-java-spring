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
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @Column(name = "transaction_id", length = 16, nullable = false)
    private String transactionId;
    
    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;
    
    @Column(name = "account_id", length = 11, nullable = false)
    private String accountId;
    
    @Column(name = "transaction_type_code", length = 2, nullable = false)
    private String transactionTypeCode;
    
    @Column(name = "transaction_category_code", nullable = false)
    private Integer transactionCategoryCode;
    
    @Column(name = "transaction_source", length = 10, nullable = false)
    private String transactionSource;
    
    @Column(name = "transaction_description", length = 100, nullable = false)
    private String transactionDescription;
    
    @Column(name = "transaction_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal transactionAmount;
    
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;
    
    @Column(name = "merchant_name", length = 50, nullable = false)
    private String merchantName;
    
    @Column(name = "merchant_city", length = 50, nullable = false)
    private String merchantCity;
    
    @Column(name = "merchant_zip", length = 10, nullable = false)
    private String merchantZip;
    
    @Column(name = "original_timestamp", nullable = false)
    private LocalDateTime originalTimestamp;
    
    @Column(name = "processing_timestamp", nullable = false)
    private LocalDateTime processingTimestamp;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
