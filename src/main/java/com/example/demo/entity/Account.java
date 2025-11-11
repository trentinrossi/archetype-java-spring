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
    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(Long accountId, BigDecimal currentBalance, BigDecimal creditLimit, Customer customer) {
        this.accountId = accountId;
        this.currentBalance = currentBalance;
        this.creditLimit = creditLimit;
        this.customer = customer;
    }

    @PrePersist
    @PreUpdate
    private void validateAccount() {
        if (accountId == null) {
            throw new IllegalStateException("Account ID must be 11 digits numeric and exist in account file");
        }
        
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() != 11 || !accountIdStr.matches("\\d{11}")) {
            throw new IllegalStateException("Account ID must be 11 digits numeric");
        }
        
        if (currentBalance == null) {
            throw new IllegalStateException("Current balance cannot be null");
        }
        
        if (creditLimit == null) {
            throw new IllegalStateException("Credit limit cannot be null");
        }
    }

    /**
     * Calculate available credit based on current balance and credit limit
     */
    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(currentBalance);
    }

    /**
     * Check if account is over the credit limit
     */
    public boolean isOverLimit() {
        return currentBalance.compareTo(creditLimit) > 0;
    }

    /**
     * Add a card to this account
     */
    public void addCard(Card card) {
        cards.add(card);
        card.setAccount(this);
    }

    /**
     * Remove a card from this account
     */
    public void removeCard(Card card) {
        cards.remove(card);
        card.setAccount(null);
    }

    /**
     * BR003: Transaction Amount Summation
     * Calculate total transaction amount for all cards in this account
     */
    public BigDecimal calculateTotalTransactionAmount() {
        BigDecimal total = BigDecimal.ZERO;
        
        for (Card card : cards) {
            if (card.getTransactions() != null) {
                for (Transaction transaction : card.getTransactions()) {
                    if (transaction.getTransactionAmount() != null) {
                        total = total.add(transaction.getTransactionAmount());
                    }
                }
            }
        }
        
        return total;
    }

    /**
     * Get all transactions across all cards for this account
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> allTransactions = new ArrayList<>();
        
        for (Card card : cards) {
            if (card.getTransactions() != null) {
                allTransactions.addAll(card.getTransactions());
            }
        }
        
        return allTransactions;
    }

    /**
     * BR009: Card-Account-Customer Linkage
     * Verify proper linkage between account and customer
     */
    public boolean hasValidLinkage() {
        return customer != null && accountId != null;
    }

    /**
     * Update account balance by adding the specified amount
     */
    public void updateBalance(BigDecimal amount) {
        if (amount != null) {
            this.currentBalance = this.currentBalance.add(amount);
        }
    }

    /**
     * Check if adding an additional amount would exceed credit limit
     */
    public boolean isWithinCreditLimit(BigDecimal additionalAmount) {
        if (additionalAmount == null) {
            return true;
        }
        BigDecimal potentialBalance = currentBalance.add(additionalAmount);
        return potentialBalance.compareTo(creditLimit) <= 0;
    }
}
