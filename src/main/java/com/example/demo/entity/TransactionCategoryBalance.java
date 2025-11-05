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
@Table(name = "transaction_category_balances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransactionCategoryBalance.TransactionCategoryBalanceId.class)
public class TransactionCategoryBalance {
    
    @Id
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    
    @Id
    @Column(name = "type_code", nullable = false, length = 2)
    private String typeCode;
    
    @Id
    @Column(name = "category_code", nullable = false, length = 4)
    private String categoryCode;
    
    @Column(name = "balance", nullable = false, precision = 11, scale = 2)
    private BigDecimal balance;
    
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
        private Long accountId;
        private String typeCode;
        private String categoryCode;
    }
}
