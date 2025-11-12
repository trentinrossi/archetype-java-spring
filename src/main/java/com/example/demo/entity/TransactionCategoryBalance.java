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
    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;

    @Id
    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private String transactionCategoryCode;

    @Column(name = "category_balance", nullable = false, precision = 11, scale = 2)
    private BigDecimal categoryBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Account ID is required");
        }
        if (categoryBalance == null) {
            throw new IllegalArgumentException("Category balance is required");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionCategoryBalanceId implements Serializable {
        private Long accountId;
        private String transactionTypeCode;
        private String transactionCategoryCode;
    }
}
