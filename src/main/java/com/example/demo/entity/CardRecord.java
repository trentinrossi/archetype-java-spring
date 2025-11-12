package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRecord {

    @Id
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "card_data", nullable = false, length = 134)
    private String cardData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateCardRecord() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Card number is required");
        }
        if (cardNumber.length() != 16) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        if (!cardNumber.matches("^[0-9]{16}$")) {
            throw new IllegalArgumentException("Card number must contain only numbers");
        }
        if (cardNumber.equals("0000000000000000")) {
            throw new IllegalArgumentException("Invalid card number format");
        }
    }
}
