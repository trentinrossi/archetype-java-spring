package com.carddemo.billpayment.entity;

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
    private String acctId;

    @Column(name = "acct_curr_bal", nullable = false, precision = 13, scale = 2)
    private BigDecimal acctCurrBal;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CardCrossReference> cardCrossReferences = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(String acctId, BigDecimal acctCurrBal) {
        this.acctId = acctId;
        this.acctCurrBal = acctCurrBal;
    }

    public boolean hasPositiveBalance() {
        return this.acctCurrBal != null && this.acctCurrBal.compareTo(BigDecimal.ZERO) > 0;
    }

    public void processFullBalancePayment() {
        if (hasPositiveBalance()) {
            this.acctCurrBal = BigDecimal.ZERO;
        }
    }

    public BigDecimal getPaymentAmount() {
        return hasPositiveBalance() ? this.acctCurrBal : BigDecimal.ZERO;
    }

    public void subtractAmount(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.acctCurrBal = this.acctCurrBal.subtract(amount);
            if (this.acctCurrBal.compareTo(BigDecimal.ZERO) < 0) {
                this.acctCurrBal = BigDecimal.ZERO;
            }
        }
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setAccount(null);
    }

    public void addCardCrossReference(CardCrossReference cardCrossReference) {
        cardCrossReferences.add(cardCrossReference);
        cardCrossReference.setAccount(this);
    }

    public void removeCardCrossReference(CardCrossReference cardCrossReference) {
        cardCrossReferences.remove(cardCrossReference);
        cardCrossReference.setAccount(null);
    }

    @PrePersist
    @PreUpdate
    public void validateAccount() {
        if (this.acctId == null || this.acctId.trim().isEmpty()) {
            throw new IllegalStateException("Acct ID can NOT be empty...");
        }
        if (this.acctId.length() > 11) {
            throw new IllegalStateException("Account ID cannot exceed 11 characters");
        }
        if (this.acctCurrBal == null) {
            throw new IllegalStateException("Account current balance cannot be null");
        }
    }
}
