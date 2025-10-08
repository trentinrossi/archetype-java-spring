package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Card Data Entity
 * Represents card data records from the CARDFILEFILE indexed file
 * Based on COBOL CBACT02C.cbl business rules
 */
@Entity
@Table(name = "card_data")
public class CardData {

    /**
     * FDCARDNUM - 16-character alphanumeric field serving as the record key
     * Used as primary key for record identification in the indexed file
     */
    @Id
    @Column(name = "card_number", length = 16, nullable = false)
    @NotNull(message = "Card number cannot be null")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String cardNumber;

    /**
     * FDCARDDATA - 134-character alphanumeric field containing the card data
     * Contains the main data payload for each card record
     */
    @Column(name = "card_data", length = 134, nullable = false)
    @NotNull(message = "Card data cannot be null")
    @Size(max = 134, message = "Card data cannot exceed 134 characters")
    private String cardData;

    /**
     * Audit fields for tracking record creation and modification
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public CardData() {
    }

    /**
     * Constructor with required fields
     */
    public CardData(String cardNumber, String cardData) {
        this.cardNumber = cardNumber;
        this.cardData = cardData;
    }

    /**
     * JPA lifecycle callbacks
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardData() {
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardData)) return false;
        CardData cardData = (CardData) o;
        return cardNumber != null && cardNumber.equals(cardData.cardNumber);
    }

    @Override
    public int hashCode() {
        return cardNumber != null ? cardNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CardData{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardData='" + cardData + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}