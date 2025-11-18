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
    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Account(Long accountId) {
        this.accountId = accountId;
    }
    
    @PrePersist
    @PreUpdate
    private void validateAccountId() {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID must be provided and must be 11 digits");
        }
        
        String accountIdStr = String.valueOf(accountId);
        
        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("ACCOUNT NUMBER MUST BE 11 DIGITS");
        }
        
        if (!accountIdStr.matches("\\d{11}")) {
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }
        
        if (accountId == 0L || accountIdStr.equals("00000000000")) {
            throw new IllegalArgumentException("Please enter a valid account number");
        }
    }
    
    public boolean isValidAccountId() {
        if (accountId == null) {
            return false;
        }
        
        String accountIdStr = String.valueOf(accountId);
        
        if (accountIdStr.length() != 11) {
            return false;
        }
        
        if (!accountIdStr.matches("\\d{11}")) {
            return false;
        }
        
        if (accountId == 0L || accountIdStr.equals("00000000000")) {
            return false;
        }
        
        return true;
    }
    
    public void addCreditCard(CreditCard creditCard) {
        creditCards.add(creditCard);
        creditCard.setAccount(this);
    }
    
    public void removeCreditCard(CreditCard creditCard) {
        creditCards.remove(creditCard);
        creditCard.setAccount(null);
    }
    
    public int getCreditCardCount() {
        return creditCards != null ? creditCards.size() : 0;
    }
    
    public String getFormattedAccountId() {
        if (accountId == null) {
            return "";
        }
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() == 11) {
            return accountIdStr.substring(0, 3) + "-" + 
                   accountIdStr.substring(3, 7) + "-" + 
                   accountIdStr.substring(7, 11);
        }
        return accountIdStr;
    }
}
