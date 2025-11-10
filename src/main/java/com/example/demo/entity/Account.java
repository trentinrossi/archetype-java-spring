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

/**
 * Entity representing a customer account that can have multiple credit cards associated.
 * Account ID must be exactly 11 digits numeric.
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "account_id", nullable = false, length = 11)
    private String accountId;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Constructor with account ID
     * @param accountId the 11-digit account identifier
     */
    public Account(String accountId) {
        this.accountId = accountId;
    }
    
    /**
     * Add a credit card to this account
     * @param creditCard the credit card to add
     */
    public void addCreditCard(CreditCard creditCard) {
        creditCards.add(creditCard);
        creditCard.setAccount(this);
    }
    
    /**
     * Remove a credit card from this account
     * @param creditCard the credit card to remove
     */
    public void removeCreditCard(CreditCard creditCard) {
        creditCards.remove(creditCard);
        creditCard.setAccount(null);
    }
    
    /**
     * Get the number of credit cards associated with this account
     * @return count of credit cards
     */
    public int getCreditCardCount() {
        return creditCards != null ? creditCards.size() : 0;
    }
}
