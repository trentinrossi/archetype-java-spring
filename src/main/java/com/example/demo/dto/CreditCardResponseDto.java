package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.enums.CardStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for credit card response data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardResponseDto {
    
    @Schema(description = "16-digit credit card number", example = "1234567890123456")
    private String cardNumber;
    
    @Schema(description = "Masked card number for display", example = "************3456")
    private String maskedCardNumber;
    
    @Schema(description = "Associated account identifier", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Status code of the credit card", example = "A")
    private String cardStatus;
    
    @Schema(description = "Status enum value", example = "ACTIVE")
    private CardStatus cardStatusEnum;
    
    @Schema(description = "Display name of card status", example = "Active")
    private String cardStatusDisplayName;
    
    @Schema(description = "Name of the cardholder", example = "John Doe")
    private String cardholderName;
    
    @Schema(description = "Expiry month", example = "12")
    private String expiryMonth;
    
    @Schema(description = "Expiry year", example = "2025")
    private String expiryYear;
    
    @Schema(description = "Type of card", example = "VISA")
    private String cardType;
    
    @Schema(description = "Credit limit for the card", example = "5000.00")
    private BigDecimal creditLimit;
    
    @Schema(description = "Available credit on the card", example = "4500.00")
    private BigDecimal availableCredit;
    
    @Schema(description = "Whether the card is active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Whether the card is expired", example = "false")
    private Boolean isExpired;
    
    @Schema(description = "Whether the card can be modified", example = "true")
    private Boolean canModify;
    
    @Schema(description = "Timestamp when the card was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the card was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
