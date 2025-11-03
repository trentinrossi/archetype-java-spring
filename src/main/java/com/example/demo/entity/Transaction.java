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
    @Column(name = "transaction_id", nullable = false, length = 16)
    private String transactionId;
    
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;
    
    @Column(name = "account_id", nullable = false, length = 11)
    private String accountId;
    
    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;
    
    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private String transactionCategoryCode;
    
    @Column(name = "transaction_source", nullable = false, length = 50)
    private String transactionSource;
    
    @Column(name = "transaction_description", nullable = false, length = 100)
    private String transactionDescription;
    
    @Column(name = "transaction_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal transactionAmount;
    
    @Column(name = "merchant_id")
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_number", referencedColumnName = "card_number", insertable = false, updatable = false)
    private Card card;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
