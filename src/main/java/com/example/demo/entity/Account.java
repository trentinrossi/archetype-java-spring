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
@Table(name = "accounts", indexes = {
    @Index(name = "idx_acct_id", columnList = "acct_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "acct_id", nullable = false, length = 11)
    private String acctId;
    
    @Column(name = "acct_curr_bal", nullable = false, precision = 13, scale = 2)
    private BigDecimal acctCurrBal;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards = new ArrayList<>();
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
    
    @PrePersist
    @PreUpdate
    public void validateAccount() {
        if (acctId == null || acctId.trim().isEmpty()) {
            throw new IllegalStateException("Acct ID can NOT be empty...");
        }
        
        if (acctCurrBal == null) {
            throw new IllegalStateException("Account current balance cannot be null");
        }
    }
    
    public boolean hasPositiveBalance() {
        return acctCurrBal != null && acctCurrBal.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public boolean canProcessPayment() {
        if (acctId == null || acctId.trim().isEmpty()) {
            return false;
        }
        
        if (acctCurrBal == null || acctCurrBal.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        return true;
    }
    
    public BigDecimal getFullPaymentAmount() {
        if (!hasPositiveBalance()) {
            return BigDecimal.ZERO;
        }
        return acctCurrBal;
    }
    
    public void processFullBalancePayment() {
        if (!canProcessPayment()) {
            throw new IllegalStateException("You have nothing to pay...");
        }
        
        BigDecimal paymentAmount = acctCurrBal;
        acctCurrBal = acctCurrBal.subtract(paymentAmount);
    }
    
    public void updateBalanceAfterPayment(BigDecimal paymentAmount) {
        if (paymentAmount == null || paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        
        if (acctCurrBal.compareTo(paymentAmount) < 0) {
            throw new IllegalStateException("Payment amount exceeds current balance");
        }
        
        acctCurrBal = acctCurrBal.subtract(paymentAmount);
    }
    
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }
    
    public void addCard(Card card) {
        cards.add(card);
        card.setAccount(this);
    }
    
    public boolean isValidForPayment() {
        return acctId != null && 
               !acctId.trim().isEmpty() && 
               acctCurrBal != null && 
               acctCurrBal.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public String getFormattedBalance() {
        if (acctCurrBal == null) {
            return "$0.00";
        }
        return String.format("$%,.2f", acctCurrBal);
    }
}
