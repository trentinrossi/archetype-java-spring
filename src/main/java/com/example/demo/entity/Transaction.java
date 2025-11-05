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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "transaction_id", nullable = false, length = 16, unique = true)
    private String transactionId;
    
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;
    
    @Column(name = "type_code", nullable = false, length = 2)
    private String typeCode;
    
    @Column(name = "category_code", nullable = false, length = 4)
    private String categoryCode;
    
    @Column(name = "source", nullable = false, length = 10)
    private String source;
    
    @Column(name = "description", length = 100)
    private String description;
    
    @Column(name = "amount", nullable = false, precision = 11, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;
    
    @Column(name = "merchant_name", nullable = false, length = 50)
    private String merchantName;
    
    @Column(name = "merchant_city", length = 50)
    private String merchantCity;
    
    @Column(name = "merchant_zip", length = 10)
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
