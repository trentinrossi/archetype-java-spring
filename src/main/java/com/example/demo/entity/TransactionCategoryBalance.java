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
@Table(name = "transaction_category_balances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransactionCategoryBalanceId.class)
public class TransactionCategoryBalance {
    
    @Id
    @Column(name = "account_id", nullable = false, length = 11)
    private String accountId;
    
    @Id
    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;
    
    @Id
    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private String transactionCategoryCode;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
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
