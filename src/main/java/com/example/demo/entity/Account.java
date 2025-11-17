package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private Long acctId;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(Long acctId) {
        this.acctId = acctId;
    }

    /**
     * BR002: Account ID Validation - Account ID must be numeric and exist in the system
     */
    public boolean isValidAccountId() {
        return acctId != null && acctId > 0;
    }

    /**
     * BR014: Account-Card Cross-Reference - Maintain bidirectional lookup
     */
    public void addCard(Card card) {
        cards.add(card);
        card.setAccount(this);
    }

    public void removeCard(Card card) {
        cards.remove(card);
        card.setAccount(null);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setAccount(null);
    }

    @PrePersist
    @PreUpdate
    public void validateAccount() {
        if (acctId == null) {
            throw new IllegalStateException("Account ID must be entered");
        }
        if (acctId <= 0) {
            throw new IllegalStateException("Account ID must be a positive number");
        }
    }
}
