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
@Table(name = "statements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 75)
    private String customerName;

    @Column(name = "customer_address", nullable = false, length = 150)
    private String customerAddress;

    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "fico_score", nullable = false, length = 3)
    private Integer ficoScore;

    @Column(name = "total_transaction_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalTransactionAmount;

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
    public void validateStatement() {
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
        if (ficoScore == null || ficoScore < 300 || ficoScore > 850) {
            throw new IllegalArgumentException("FICO score must be between 300 and 850");
        }
    }
}
