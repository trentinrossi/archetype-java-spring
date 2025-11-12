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
    
    public CardRecord(String cardNumber, String cardData) {
        this.cardNumber = cardNumber;
        this.cardData = cardData;
    }
    
    @PrePersist
    @PreUpdate
    private void validateCardRecord() {
        validateCardNumber();
    }
    
    private void validateCardNumber() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid card number format");
        }
        
        if (cardNumber.length() != 16) {
            throw new IllegalArgumentException("Invalid card number format");
        }
        
        if (!cardNumber.matches("^[0-9]{16}$")) {
            throw new IllegalArgumentException("Card number must contain only numbers");
        }
        
        if (cardNumber.matches("^0{16}$")) {
            throw new IllegalArgumentException("Card filter is blank");
        }
        
        if (cardNumber.matches("^\\s{16}$")) {
            throw new IllegalArgumentException("Card filter is blank");
        }
    }
    
    public boolean isValidFormat() {
        if (cardNumber == null || cardNumber.length() != 16) {
            return false;
        }
        return cardNumber.matches("^[0-9]{16}$");
    }
    
    public boolean isBlankOrZeros() {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return true;
        }
        return cardNumber.matches("^[0\\s]{16}$");
    }
    
    public boolean isNumericOnly() {
        if (cardNumber == null) {
            return false;
        }
        return cardNumber.matches("^[0-9]+$");
    }
}
