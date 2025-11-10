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
 * Card number must be exactly 16 digits numeric.
 */
@Entity
@Table(name = "credit_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    
    @Id
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @Column(name = "card_status", nullable = false, length = 1)
    private String cardStatus;
    
    @Column(name = "cardholder_name", length = 100)
    private String cardholderName;
    
    @Column(name = "expiry_month", length = 2)
    private String expiryMonth;
    
    @Column(name = "expiry_year", length = 4)
    private String expiryYear;
    
    @Column(name = "card_type", length = 20)
    private String cardType;
    
    @Column(name = "credit_limit")
    private Double creditLimit;
    
    @Column(name = "available_credit")
    private Double availableCredit;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Constructor with required fields
     * @param cardNumber the 16-digit card number
     * @param account the associated account
     * @param cardStatus the status code (single character)
     */
    public CreditCard(String cardNumber, Account account, String cardStatus) {
        this.cardNumber = cardNumber;
        this.account = account;
        this.cardStatus = cardStatus;
    }
    
    /**
     * Get the card status as enum
     * @return CardStatus enum value
     */
    public CardStatus getCardStatusEnum() {
        return cardStatus != null ? CardStatus.fromCode(cardStatus) : null;
    }
    
    /**
     * Set the card status from enum
     * @param status the CardStatus enum value
     */
    public void setCardStatusEnum(CardStatus status) {
        this.cardStatus = status != null ? status.getCode() : null;
    }
    
    /**
     * Check if the card is active and usable
     * @return true if card status is active
     */
    public boolean isActive() {
        CardStatus status = getCardStatusEnum();
        return status != null && status.isUsable();
    }
    
    /**
     * Check if the card can be modified
     * @return true if card is not cancelled
     */
    public boolean canModify() {
        CardStatus status = getCardStatusEnum();
        return status != null && status.canModify();
    }
    
    /**
     * Get masked card number for display (shows last 4 digits)
     * @return masked card number (e.g., "************1234")
     */
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "*".repeat(cardNumber.length() - 4) + cardNumber.substring(cardNumber.length() - 4);
    }
    
    /**
     * Check if card is expired
     * @return true if card is expired
     */
    public boolean isExpired() {
        if (expiryMonth == null || expiryYear == null) {
            return false;
        }
        try {
            int month = Integer.parseInt(expiryMonth);
            int year = Integer.parseInt(expiryYear);
            LocalDateTime now = LocalDateTime.now();
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();
            
            return year < currentYear || (year == currentYear && month < currentMonth);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Get the account ID from the associated account
     * @return account ID or null if account not set
     */
    public String getAccountId() {
        return account != null ? account.getAccountId() : null;
    }
}
