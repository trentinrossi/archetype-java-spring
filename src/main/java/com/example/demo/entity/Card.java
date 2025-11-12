package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    @Column(name = "cvv_code", nullable = false, length = 3)
    private String cvvCode;

    @Column(name = "expiration_date", nullable = false, length = 10)
    private String expirationDate;

    @Column(name = "xref_card_num", nullable = false, length = 16)
    private String xrefCardNum;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void validateCardData() {
        validateCardNumber(this.cardNumber);
        validateAccountId(this.accountId);
        validateCustomerId(this.customerId);
        validateCvvCode(this.cvvCode);
        validateExpirationDate(this.expirationDate);
        validateXrefCardNum(this.xrefCardNum);
    }

    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid card number format");
        }
        if (cardNumber.trim().length() != 16) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        if (!cardNumber.matches("^[0-9]{16}$")) {
            throw new IllegalArgumentException("Card number must contain only numbers");
        }
        if (cardNumber.equals("0000000000000000")) {
            throw new IllegalArgumentException("Invalid card number format");
        }
    }

    private void validateAccountId(Long accountId) {
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Invalid account number. Must be 11 digits and not zero.");
        }
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("Account number must be 11 digits");
        }
    }

    private void validateCustomerId(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        String customerIdStr = String.valueOf(customerId);
        if (customerIdStr.length() > 9) {
            throw new IllegalArgumentException("Customer ID exceeds maximum length");
        }
    }

    private void validateCvvCode(String cvvCode) {
        if (cvvCode == null || cvvCode.trim().isEmpty()) {
            throw new IllegalArgumentException("CVV code is required");
        }
        if (cvvCode.trim().length() != 3) {
            throw new IllegalArgumentException("CVV code must be 3 digits");
        }
        if (!cvvCode.matches("^[0-9]{3}$")) {
            throw new IllegalArgumentException("CVV code must be numeric");
        }
    }

    private void validateExpirationDate(String expirationDate) {
        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
        if (expirationDate.trim().length() != 10) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            java.time.LocalDate.parse(expirationDate, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
    }

    private void validateXrefCardNum(String xrefCardNum) {
        if (xrefCardNum == null || xrefCardNum.trim().isEmpty()) {
            throw new IllegalArgumentException("XREF card number is required");
        }
        if (xrefCardNum.trim().length() != 16) {
            throw new IllegalArgumentException("XREF card number must be 16 characters");
        }
    }

    public boolean isExpired() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            java.time.LocalDate expDate = java.time.LocalDate.parse(this.expirationDate, formatter);
            return expDate.isBefore(java.time.LocalDate.now());
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    public String getMaskedCardNumber() {
        if (this.cardNumber == null || this.cardNumber.length() != 16) {
            return "****************";
        }
        return "************" + this.cardNumber.substring(12);
    }

    public String getFormattedCardNumber() {
        if (this.cardNumber == null || this.cardNumber.length() != 16) {
            return this.cardNumber;
        }
        return this.cardNumber.substring(0, 4) + " " +
               this.cardNumber.substring(4, 8) + " " +
               this.cardNumber.substring(8, 12) + " " +
               this.cardNumber.substring(12, 16);
    }
}
