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
    
    @Column(name = "type_code", length = 2, nullable = false)
    private String typeCode;
    
    @Column(name = "category_code", nullable = false)
    private Integer categoryCode;
    
    @Column(name = "source", length = 10, nullable = false)
    private String source;
    
    @Column(name = "description", length = 60)
    private String description;
    
    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;
    
    @Column(name = "merchant_id", length = 9)
    private String merchantId;
    
    @Column(name = "merchant_name", length = 30)
    private String merchantName;
    
    @Column(name = "merchant_city", length = 25)
    private String merchantCity;
    
    @Column(name = "merchant_zip", length = 10)
    private String merchantZip;
    
    @Column(name = "origination_timestamp", nullable = false)
    private LocalDateTime originationTimestamp;
    
    @Column(name = "processing_timestamp", nullable = false)
    private LocalDateTime processingTimestamp;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
