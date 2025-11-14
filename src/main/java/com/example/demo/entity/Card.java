package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "customer_id", nullable = false, length = 9)
    private Long customerId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    public Card(String cardNumber, Long accountId, Long customerId) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.customerId = customerId;
    }

    @PrePersist
    @PreUpdate
    private void validateCard() {
        validateCardNumber();
        validateAccountId();
        validateCustomerId();
    }

    private void validateCardNumber() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        
        if (cardNumber.length() != 16) {
            throw new IllegalArgumentException("Card number must be 16 characters");
        }
    }

    private void validateAccountId() {
        if (accountId == null) {
            throw new IllegalArgumentException("Account number is required");
        }
        
        String accountIdStr = String.valueOf(accountId);
        
        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("Account number must be 11 digits");
        }
        
        if (accountId == 0L) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
    }

    private void validateCustomerId() {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        
        String customerIdStr = String.valueOf(customerId);
        if (customerIdStr.length() != 9) {
            throw new IllegalArgumentException("Customer ID must be 9 digits");
        }
    }
}
