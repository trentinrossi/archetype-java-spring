package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "acct_id", nullable = false, length = 11)
    private String accountId;

    @Column(name = "acct_curr_bal", nullable = false, precision = 13, scale = 2)
    private BigDecimal currentBalance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardCrossReference> cardCrossReferences = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(String accountId, BigDecimal currentBalance) {
        this.accountId = accountId;
        this.currentBalance = currentBalance;
    }

    /**
     * BR002: Balance Check - Verifies that the account has a positive balance to pay
     */
    public boolean hasPositiveBalance() {
        return currentBalance != null && currentBalance.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * BR004: Full Balance Payment - Payment processes the full current account balance
     */
    public void processFullPayment() {
        if (hasPositiveBalance()) {
            this.currentBalance = BigDecimal.ZERO;
        }
    }

    /**
     * BR007: Account Balance Update - Updates account balance after successful payment
     */
    public void subtractAmount(BigDecimal amount) {
        if (amount != null && currentBalance != null) {
            this.currentBalance = this.currentBalance.subtract(amount);
        }
    }

    @PrePersist
    @PreUpdate
    protected void validateAccount() {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalStateException("Acct ID can NOT be empty...");
        }
        
        if (accountId.length() > 11) {
            throw new IllegalStateException("Account ID cannot exceed 11 characters");
        }
        
        if (currentBalance == null) {
            throw new IllegalStateException("Current balance cannot be null");
        }
    }
}
