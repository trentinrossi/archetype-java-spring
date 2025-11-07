package com.example.demo.dto;

import com.example.demo.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for credit card response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardResponseDto {
    
    @Schema(description = "Internal ID of the credit card", example = "1")
    private Long id;
    
    @Schema(description = "16-digit credit card number", example = "1234567890123456")
    private String cardNumber;
    
    @Schema(description = "Masked card number showing only last 4 digits", example = "************3456")
    private String maskedCardNumber;
    
    @Schema(description = "Associated account identifier", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Current status of the credit card", example = "ACTIVE")
    private CardStatus cardStatus;
    
    @Schema(description = "Display name for card status", example = "Active")
    private String cardStatusDisplayName;
    
    @Schema(description = "Whether the card is active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Whether the card can perform transactions", example = "true")
    private Boolean canPerformTransactions;
    
    @Schema(description = "Whether the card is blocked", example = "false")
    private Boolean isBlocked;
    
    @Schema(description = "Timestamp when the card was created")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the card was last updated")
    private LocalDateTime updatedAt;
}
