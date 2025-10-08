package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Card Data DTO
 * Data Transfer Object for CardData entity
 * Based on COBOL CBACT02C.cbl business rules
 */
public class CardDataDto {

    /**
     * FDCARDNUM - 16-character alphanumeric field serving as the record key
     */
    @NotNull(message = "Card number cannot be null")
    @NotBlank(message = "Card number cannot be blank")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String cardNumber;

    /**
     * FDCARDDATA - 134-character alphanumeric field containing the card data
     */
    @NotNull(message = "Card data cannot be null")
    @NotBlank(message = "Card data cannot be blank")
    @Size(max = 134, message = "Card data cannot exceed 134 characters")
    private String cardData;

    /**
     * Audit fields
     */
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default constructor
     */
    public CardDataDto() {
    }

    /**
     * Constructor with required fields
     */
    public CardDataDto(String cardNumber, String cardData) {
        this.cardNumber = cardNumber;
        this.cardData = cardData;
    }

    /**
     * Full constructor
     */
    public CardDataDto(String cardNumber, String cardData, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.cardNumber = cardNumber;
        this.cardData = cardData;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    public String toString() {
        return "CardDataDto{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardData='" + cardData + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}