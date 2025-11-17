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
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @Column(name = "card_num", nullable = false, length = 16)
    private String cardNum;

    @Column(name = "acct_id", nullable = false, length = 11)
    private Long acctId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "acct_id")
    private Account account;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Card(String cardNum, Long acctId) {
        this.cardNum = cardNum;
        this.acctId = acctId;
    }

    /**
     * BR003: Card Number Validation - Card Number must be numeric and exist in the system
     */
    public boolean isValidCardNumber() {
        if (cardNum == null || cardNum.trim().isEmpty()) {
            return false;
        }
        if (cardNum.length() != 16) {
            return false;
        }
        return cardNum.matches("\\d{16}");
    }

    /**
     * BR014: Account-Card Cross-Reference - Maintain bidirectional relationship
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setCard(this);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setCard(null);
    }

    @PrePersist
    @PreUpdate
    public void validateCard() {
        if (cardNum == null || cardNum.trim().isEmpty()) {
            throw new IllegalStateException("Card Number must be entered");
        }
        if (cardNum.length() != 16) {
            throw new IllegalStateException("Card Number must be 16 digits");
        }
        if (!cardNum.matches("\\d{16}")) {
            throw new IllegalStateException("Card Number must be numeric");
        }
        if (acctId == null) {
            throw new IllegalStateException("Account ID must be entered");
        }
        if (acctId <= 0) {
            throw new IllegalStateException("Account ID must be a positive number");
        }
    }
}
