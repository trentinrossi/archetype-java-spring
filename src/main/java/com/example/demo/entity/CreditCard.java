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

    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "card_status", nullable = false, length = 1)
    private String cardStatus;

    @Column(name = "cvv_code", nullable = false, length = 3)
    private Integer cvvCode;

    @Column(name = "embossed_name", nullable = false, length = 50)
    private String embossedName;

    @Column(name = "cardholder_name", nullable = false, length = 50)
    private String cardholderName;

    @Column(name = "expiration_date", nullable = false, length = 10)
    private LocalDate expirationDate;

    @Column(name = "expiration_month", nullable = false, length = 2)
    private Integer expirationMonth;

    @Column(name = "expiration_year", nullable = false, length = 4)
    private Integer expirationYear;

    @Column(name = "expiration_day", nullable = false, length = 2)
    private Integer expirationDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateCreditCard() {
        if (cardNumber == null || !cardNumber.matches("^[0-9]{16}$")) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
        if (cardStatus == null || (!cardStatus.equals("Y") && !cardStatus.equals("N"))) {
            throw new IllegalArgumentException("Card status must be either 'Y' or 'N', cannot be blank");
        }
        if (cardholderName == null || cardholderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Cardholder name must contain only alphabetic characters and spaces, cannot be blank");
        }
        if (!cardholderName.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Cardholder name must contain only alphabetic characters and spaces");
        }
        if (expirationMonth == null || expirationMonth < 1 || expirationMonth > 12) {
            throw new IllegalArgumentException("Expiration month must be a numeric value between 01 and 12");
        }
        if (expirationYear == null || expirationYear < 1950 || expirationYear > 2099) {
            throw new IllegalArgumentException("Expiration year must be a numeric value between 1950 and 2099");
        }
        if (embossedName == null) {
            embossedName = cardholderName;
        }
    }

    public boolean isExpired() {
        if (expirationDate != null) {
            return expirationDate.isBefore(LocalDate.now());
        }
        return false;
    }

    public boolean isActive() {
        return "Y".equals(cardStatus);
    }
}
