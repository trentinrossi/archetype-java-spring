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

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "card_status", nullable = false, length = 1)
    private String cardStatus;

    @Column(name = "cvv_code", nullable = false, length = 3)
    private String cvvCode;

    @Column(name = "embossed_name", nullable = false, length = 50)
    private String embossedName;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

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
    public void validate() {
        if (cardNumber == null || !cardNumber.matches("\\d{16}")) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Account ID is required");
        }
        if (cardStatus == null || (!cardStatus.equals("Y") && !cardStatus.equals("N"))) {
            throw new IllegalArgumentException("Card status must be Y or N");
        }
        if (cvvCode == null || !cvvCode.matches("\\d{3}")) {
            throw new IllegalArgumentException("CVV code must be 3 digits");
        }
        if (embossedName == null || embossedName.trim().isEmpty()) {
            throw new IllegalArgumentException("Embossed name is required");
        }
    }

    public boolean isActive() {
        return "Y".equals(cardStatus);
    }

    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }
}
