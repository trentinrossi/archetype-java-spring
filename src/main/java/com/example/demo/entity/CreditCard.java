package com.example.demo.entity;

import com.example.demo.enums.CardStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a credit card in the system with associated account and status information.
 * Business Rule BR004: Credit card records can be filtered by account ID and/or card number.
 * Business Rule BR008: Records that do not match the specified filter criteria are excluded from display.
 */
@Entity
@Table(name = "credit_cards", indexes = {
    @Index(name = "idx_card_number", columnList = "card_number"),
    @Index(name = "idx_account_id", columnList = "account_id"),
    @Index(name = "idx_card_status", columnList = "card_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", nullable = false, length = 1)
    private CardStatus cardStatus;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Constructor with card number, account, and status
     */
    public CreditCard(String cardNumber, Account account, CardStatus cardStatus) {
        this.cardNumber = cardNumber;
        this.account = account;
        this.cardStatus = cardStatus;
    }
    
    /**
     * Validates that card number is numeric and exactly 16 digits
     * Business Rule: Card number must be numeric and exactly 16 digits if supplied
     */
    public boolean isValidCardNumber() {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return false;
        }
        return cardNumber.matches("\\d{16}");
    }
    
    /**
     * Check if card number is blank or zeros (indicates no filter)
     * Business Rule: Card number can be blank or zeros to indicate no filter
     */
    public static boolean isNoFilterCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return true;
        }
        return cardNumber.matches("0+");
    }
    
    /**
     * Get masked card number for display (shows only last 4 digits)
     */
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }
    
    /**
     * Check if card is active
     */
    public boolean isActive() {
        return cardStatus != null && cardStatus.isActive();
    }
    
    /**
     * Check if card can perform transactions
     */
    public boolean canPerformTransactions() {
        return cardStatus != null && cardStatus.canPerformTransactions();
    }
    
    /**
     * Check if card is blocked
     */
    public boolean isBlocked() {
        return cardStatus != null && cardStatus.isBlocked();
    }
    
    /**
     * Get the account ID as string
     */
    public String getAccountId() {
        return account != null ? account.getAccountId() : null;
    }
    
    /**
     * Get card status display name
     */
    public String getCardStatusDisplayName() {
        return cardStatus != null ? cardStatus.getDisplayName() : "Unknown";
    }
}
