package com.example.demo.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing credit card.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCreditCardRequestDto {
    
    @Schema(description = "Status code of the credit card (single character)", example = "A", required = false)
    @Pattern(regexp = "^[A-Z]$", message = "Card status must be a single uppercase letter")
    private String cardStatus;
    
    @Schema(description = "Name of the cardholder", example = "John Doe", required = false)
    private String cardholderName;
    
    @Schema(description = "Expiry month (2 digits)", example = "12", required = false)
    @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Expiry month must be between 01 and 12")
    private String expiryMonth;
    
    @Schema(description = "Expiry year (4 digits)", example = "2025", required = false)
    @Pattern(regexp = "^\\d{4}$", message = "Expiry year must be 4 digits")
    private String expiryYear;
    
    @Schema(description = "Type of card (e.g., VISA, MASTERCARD)", example = "VISA", required = false)
    private String cardType;
    
    @Schema(description = "Credit limit for the card", example = "5000.00", required = false)
    private BigDecimal creditLimit;
    
    @Schema(description = "Available credit on the card", example = "5000.00", required = false)
    private BigDecimal availableCredit;
}
