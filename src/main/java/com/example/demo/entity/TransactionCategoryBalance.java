package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_category_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransactionCategoryBalance.TransactionCategoryBalanceId.class)
public class TransactionCategoryBalance {
    
    @Id
    @Column(name = "account_id", length = 11, nullable = false)
    private String accountId;
    
    @Id
    @Column(name = "transaction_type_code", length = 2, nullable = false)
    private String transactionTypeCode;
    
    @Id
    @Column(name = "transaction_category_code", nullable = false)
    private Integer transactionCategoryCode;
    
    @Column(name = "category_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal categoryBalance;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionCategoryBalanceId implements Serializable {
        private String accountId;
        private String transactionTypeCode;
        private Integer transactionCategoryCode;
    }
}
