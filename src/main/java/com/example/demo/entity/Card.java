package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
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
    private Integer cvvCode;

    @Column(name = "expiration_date", nullable = false, length = 10)
    private String expirationDate;

    @Column(name = "xref_card_num", nullable = false, length = 16)
    private String xrefCardNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateCardData() {
        validateCardNumber();
        validateAccountId();
        validateCustomerId();
        validateExpirationDate();
        if (xrefCardNum == null) {
            xrefCardNum = cardNumber;
        }
    }

    private void validateCardNumber() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
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

    private void validateAccountId() {
        if (accountId == null) {
            throw new IllegalArgumentException("Account number is required");
        }
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() > 11) {
            throw new IllegalArgumentException("Account number must be 11 digits");
        }
        if (accountId == 0) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
    }

    private void validateCustomerId() {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        String customerIdStr = String.valueOf(customerId);
        if (customerIdStr.length() > 9) {
            throw new IllegalArgumentException("Customer ID exceeds maximum length");
        }
        if (customerId == 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
    }

    private void validateExpirationDate() {
        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Expiration date is required");
        }
        try {
            String dateStr = expirationDate.replace("-", "").replace("/", "");
            if (dateStr.length() != 8) {
                throw new IllegalArgumentException("Invalid expiration date");
            }
            int year = Integer.parseInt(dateStr.substring(0, 4));
            int month = Integer.parseInt(dateStr.substring(4, 6));
            int day = Integer.parseInt(dateStr.substring(6, 8));
            if (year < 1900 || year > 9999 || month < 1 || month > 12 || day < 1 || day > 31) {
                throw new IllegalArgumentException("Invalid expiration date");
            }
            LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
    }

    public boolean isExpired() {
        try {
            String dateStr = expirationDate.replace("-", "").replace("/", "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate expDate = LocalDate.parse(dateStr, formatter);
            return expDate.isBefore(LocalDate.now());
        } catch (Exception e) {
            return true;
        }
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return "****************";
        }
        return "************" + cardNumber.substring(12);
    }
}
