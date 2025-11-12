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
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "cvv_code", nullable = false, length = 3)
    private String cvvCode;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "xref_card_num", nullable = false, length = 16)
    private String xrefCardNum;

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
    public void validateCardData() {
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
        if (customerId == null || customerId == 0) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (cvvCode == null || cvvCode.length() != 3 || !cvvCode.matches("\\d{3}")) {
            throw new IllegalArgumentException("CVV code must be 3 digits");
        }
        if (expirationDate == null) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
        this.xrefCardNum = this.cardNumber;
    }

    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return cardNumber;
        }
        return "************" + cardNumber.substring(12);
    }
}
