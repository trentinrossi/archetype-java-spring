package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing a customer account that owns one or more credit cards.
 * Business Rule: Account serves as the primary container for credit card management.
 */
@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_account_id", columnList = "account_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "account_id", nullable = false, unique = true, length = 11)
    private String accountId;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards = new ArrayList<>();
    
    @ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Constructor with account ID
     */
    public Account(String accountId) {
        this.accountId = accountId;
    }
    
    /**
     * Validates that account ID is numeric and exactly 11 digits
     */
    public boolean isValidAccountId() {
        if (accountId == null || accountId.isEmpty()) {
            return false;
        }
        return accountId.matches("\\d{11}");
    }
    
    /**
     * Add a credit card to this account
     */
    public void addCreditCard(CreditCard card) {
        creditCards.add(card);
        card.setAccount(this);
    }
    
    /**
     * Remove a credit card from this account
     */
    public void removeCreditCard(CreditCard card) {
        creditCards.remove(card);
        card.setAccount(null);
    }
    
    /**
     * Add a user with access to this account
     */
    public void addUser(User user) {
        users.add(user);
        user.getAccounts().add(this);
    }
    
    /**
     * Remove user access from this account
     */
    public void removeUser(User user) {
        users.remove(user);
        user.getAccounts().remove(this);
    }
    
    /**
     * Get the count of active credit cards
     */
    public long getActiveCreditCardCount() {
        return creditCards.stream()
                .filter(card -> card.getCardStatus() != null && card.getCardStatus().isActive())
                .count();
    }
}
