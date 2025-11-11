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
 * CreditCard Entity
 * 
 * Represents a credit card with its associated account and status information.
 * 
 * Field Specifications:
 * - card_number: numeric (length: 16), required, unique 16-digit credit card number
 * - account_id: numeric (length: 11), required, associated account identifier
 * - card_status: alphanumeric (length: 1), required, current status of the credit card
 * 
 * Business Rules Implemented:
 * - BR002: Card Number Filter Validation
 * - BR004: Account Filter Validation
 * - BR005: Card Status Filter Validation
 * - BR006: Filter Record Matching
 * - BR012: View Card Details
 * - BR013: Update Card Information
 * 
 * Relationships:
 * - Many-to-One with Account
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

    @Column(name = "account_id", nullable = false, length = 11)
    private String accountId;

    @Column(name = "card_status", nullable = false, length = 1)
    private char cardStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public CreditCard(String cardNumber, String accountId, char cardStatus) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.cardStatus = cardStatus;
    }

    /**
     * Validates card data before persisting or updating
     * Implements BR002, BR004, BR005
     */
    @PrePersist
    @PreUpdate
    private void validateCardData() {
        validateCardNumber();
        validateAccountId();
        validateCardStatus();
    }

    /**
     * Validates card number
     * Implements BR002: Card Number Filter Validation
     */
    private void validateCardNumber() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number cannot be null or empty");
        }

        String trimmedCardNumber = cardNumber.trim();

        // BR002: Cannot be blank, spaces, or zeros
        if (trimmedCardNumber.matches("^0+$")) {
            throw new IllegalArgumentException("CARD FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        // BR002: Must be numeric and exactly 16 digits
        if (!trimmedCardNumber.matches("^\\d{16}$")) {
            throw new IllegalArgumentException("CARD ID FILTER,IF SUPPLIED MUST BE A 16 DIGIT NUMBER");
        }
    }

    /**
     * Validates account ID
     * Implements BR004: Account Filter Validation
     */
    private void validateAccountId() {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        String trimmedAccountId = accountId.trim();

        // BR004: Cannot be blank, spaces, or zeros
        if (trimmedAccountId.matches("^0+$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        // BR004: Must be numeric and exactly 11 digits
        if (!trimmedAccountId.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }
    }

    /**
     * Validates card status
     * Implements BR005: Card Status Filter Validation
     */
    private void validateCardStatus() {
        if (!CardStatus.isValidCode(cardStatus)) {
            throw new IllegalArgumentException("Invalid card status code: " + cardStatus);
        }
    }

    /**
     * Gets the CardStatus enum for this card
     * 
     * @return CardStatus enum value
     */
    public CardStatus getCardStatusEnum() {
        return CardStatus.fromCode(cardStatus);
    }

    /**
     * Sets the card status from CardStatus enum
     * 
     * @param status CardStatus enum value
     */
    public void setCardStatusEnum(CardStatus status) {
        this.cardStatus = status.getCode();
    }

    /**
     * Gets the display name for the card status
     * 
     * @return Card status display name
     */
    public String getCardStatusDisplayName() {
        return CardStatus.fromCode(cardStatus).getDisplayName();
    }

    /**
     * Checks if the card is active
     * 
     * @return true if card status is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return this.cardStatus == CardStatus.ACTIVE.getCode();
    }

    /**
     * Checks if the card is blocked
     * 
     * @return true if card status is BLOCKED, false otherwise
     */
    public boolean isBlocked() {
        return this.cardStatus == CardStatus.BLOCKED.getCode();
    }

    /**
     * Checks if the card is closed
     * 
     * @return true if card status is CLOSED, false otherwise
     */
    public boolean isClosed() {
        return this.cardStatus == CardStatus.CLOSED.getCode();
    }

    /**
     * Checks if the card is inactive
     * 
     * @return true if card status is INACTIVE, false otherwise
     */
    public boolean isInactive() {
        return this.cardStatus == CardStatus.INACTIVE.getCode();
    }

    /**
     * Gets formatted card number with spaces (XXXX XXXX XXXX XXXX)
     * 
     * @return Formatted card number
     */
    public String getFormattedCardNumber() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4) + " " + 
               cardNumber.substring(4, 8) + " " + 
               cardNumber.substring(8, 12) + " " + 
               cardNumber.substring(12, 16);
    }

    /**
     * Gets masked card number (**** **** **** XXXX)
     * 
     * @return Masked card number showing only last 4 digits
     */
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(12, 16);
    }

    /**
     * Validates if a given card number is valid
     * Implements BR002: Card Number Filter Validation
     * 
     * @param cardNumber The card number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }

        String trimmedCardNumber = cardNumber.trim();

        // Cannot be all zeros
        if (trimmedCardNumber.matches("^0+$")) {
            return false;
        }

        // Must be numeric and exactly 16 digits
        if (!trimmedCardNumber.matches("^\\d{16}$")) {
            return false;
        }

        return true;
    }
}
