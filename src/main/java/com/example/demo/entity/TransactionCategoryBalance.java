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
public class TransactionCategoryBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trancat_acct_id", nullable = false, length = 11)
    private Long trancatAcctId;

    @Column(name = "trancat_type_cd", nullable = false, length = 2)
    private String trancatTypeCd;

    @Column(name = "trancat_cd", nullable = false, length = 4)
    private Integer trancatCd;

    @Column(name = "tran_cat_bal", nullable = false, precision = 13, scale = 2)
    private BigDecimal tranCatBal;

    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;

    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private Integer transactionCategoryCode;

    @Column(name = "category_balance", nullable = false, precision = 13, scale = 2)
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
    public void validateBalance() {
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
        if (tranCatBal == null) {
            throw new IllegalArgumentException("Balance must be numeric");
        }
        if (trancatAcctId == null) {
            trancatAcctId = accountId;
        }
        if (trancatTypeCd == null) {
            trancatTypeCd = transactionTypeCode;
        }
        if (trancatCd == null) {
            trancatCd = transactionCategoryCode;
        }
        if (tranCatBal == null) {
            tranCatBal = categoryBalance;
        }
    }
}
