package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CreditCardResponseDto
 * 
 * Response DTO for credit card information.
 * 
 * Business Rules Implemented:
 * - BR002: Card Number Filter Validation
 * - BR004: Account Filter Validation
 * - BR005: Card Status Filter Validation
 * - BR012: View Card Details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardResponseDto {

    @Schema(description = "Unique 16-digit credit card number", example = "1234567890123456")
    private String cardNumber;

    @Schema(description = "Masked credit card number for display", example = "**** **** **** 3456")
    private String maskedCardNumber;

    @Schema(description = "Formatted credit card number", example = "1234 5678 9012 3456")
    private String formattedCardNumber;

    @Schema(description = "Associated account identifier", example = "12345678901")
    private String accountId;

    @Schema(description = "Current status of the credit card", example = "A")
    private String cardStatus;

    @Schema(description = "Card status display name", example = "Active")
    private String cardStatusDisplayName;

    @Schema(description = "Indicates if the card is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Indicates if the card is blocked", example = "false")
    private Boolean isBlocked;

    @Schema(description = "Indicates if the card is closed", example = "false")
    private Boolean isClosed;

    @Schema(description = "Timestamp when the card was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the card was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
