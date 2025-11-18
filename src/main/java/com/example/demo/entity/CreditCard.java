package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "embossed_name", length = 50)
    private String embossedName;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "expiry_month", nullable = false, length = 2)
    private String expiryMonth;

    @Column(name = "expiry_year", nullable = false, length = 4)
    private String expiryYear;

    @Column(name = "active_status", nullable = false, length = 1)
    private String activeStatus;

    @Column(name = "card_status", nullable = false, length = 1)
    private String cardStatus;

    @Column(name = "expiration_month", nullable = false)
    private Integer expirationMonth;

    @Column(name = "expiration_year", nullable = false)
    private Integer expirationYear;

    @Column(name = "expiration_day")
    private Integer expirationDay;

    @Column(name = "cvv_code", nullable = false, length = 3)
    private String cvvCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "version")
    @Version
    private Long version;

    public CreditCard(String cardNumber, Account account, LocalDate expirationDate, String cardStatus, String cvvCode) {
        this.cardNumber = cardNumber;
        this.account = account;
        this.expirationDate = expirationDate;
        this.cardStatus = cardStatus;
        this.activeStatus = cardStatus;
        this.cvvCode = cvvCode;
        extractExpirationComponents();
    }

    @PrePersist
    @PreUpdate
    public void validateAndProcess() {
        validateCardNumber();
        validateEmbossedName();
        validateCardStatus();
        validateExpirationMonth();
        validateExpirationYear();
        validateCvvCode();
        extractExpirationComponents();
    }

    /**
     * Validation: Card number must be 16-digit numeric value
     */
    private void validateCardNumber() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number must be provided and must be 16 digits");
        }
        
        if (!cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("CARD NUMBER MUST BE 16 DIGITS");
        }
        
        if (cardNumber.equals("0000000000000000")) {
            throw new IllegalArgumentException("Please enter a valid card number");
        }
    }

    /**
     * Validation: Embossed name must contain only alphabets and spaces
     */
    private void validateEmbossedName() {
        if (embossedName != null && !embossedName.trim().isEmpty()) {
            if (!embossedName.matches("^[a-zA-Z\\s]+$")) {
                throw new IllegalArgumentException("Card name must contain only alphabets and spaces");
            }
        }
    }

    /**
     * Validation: Card status must be 'Y' or 'N'
     */
    private void validateCardStatus() {
        if (cardStatus != null && !cardStatus.equals("Y") && !cardStatus.equals("N")) {
            throw new IllegalArgumentException("Card status must be Y (active) or N (inactive)");
        }
    }

    /**
     * Validation: Expiration month must be between 1 and 12
     */
    private void validateExpirationMonth() {
        if (expirationMonth != null) {
            if (expirationMonth < 1 || expirationMonth > 12) {
                throw new IllegalArgumentException("Expiration month must be between 1 and 12");
            }
        }
    }

    /**
     * Validation: Expiration year must be between 1950 and 2099
     */
    private void validateExpirationYear() {
        if (expirationYear != null) {
            if (expirationYear < 1950 || expirationYear > 2099) {
                throw new IllegalArgumentException("Expiration year must be between 1950 and 2099");
            }
        }
    }

    /**
     * Validation: CVV code must be 3-digit numeric value
     */
    private void validateCvvCode() {
        if (cvvCode != null && !cvvCode.matches("\\d{3}")) {
            throw new IllegalArgumentException("CVV code must be 3 digits");
        }
    }

    /**
     * Extract expiration components from expiration date
     */
    private void extractExpirationComponents() {
        if (expirationDate != null) {
            this.expirationMonth = expirationDate.getMonthValue();
            this.expirationYear = expirationDate.getYear();
            this.expirationDay = expirationDate.getDayOfMonth();
            
            this.expiryMonth = String.format("%02d", expirationDate.getMonthValue());
            this.expiryYear = String.valueOf(expirationDate.getYear());
        }
    }

    public boolean isActive() {
        return "Y".equals(cardStatus);
    }

    public boolean isExpired() {
        if (expirationDate == null) {
            return false;
        }
        return expirationDate.isBefore(LocalDate.now());
    }

    public String getFormattedCardNumber() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        return cardNumber.substring(0, 4) + " " + 
               cardNumber.substring(4, 8) + " " + 
               cardNumber.substring(8, 12) + " " + 
               cardNumber.substring(12, 16);
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(12, 16);
    }

    public String getExpirationDateFormatted() {
        if (expiryMonth != null && expiryYear != null) {
            return expiryMonth + "/" + expiryYear;
        }
        return "";
    }

    /**
     * BR002: Card Detail Modification
     * Check if field is modifiable
     */
    public boolean isFieldModifiable(String fieldName) {
        // Only embossed_name, card_status, and expiration_date are modifiable
        return "embossedName".equals(fieldName) || 
               "cardStatus".equals(fieldName) || 
               "expirationDate".equals(fieldName);
    }

    /**
     * BR003: Concurrent Update Prevention
     * Version field is used for optimistic locking
     */
    public boolean hasBeenModifiedSince(Long originalVersion) {
        return this.version != null && !this.version.equals(originalVersion);
    }
}
