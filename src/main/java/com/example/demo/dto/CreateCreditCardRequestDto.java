package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateCreditCardRequestDto
 * 
 * Request DTO for creating a new credit card.
 * 
 * Business Rules Implemented:
 * - BR002: Card Number Filter Validation
 * - BR004: Account Filter Validation
 * - BR005: Card Status Filter Validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditCardRequestDto {

    @Schema(description = "Unique 16-digit credit card number", 
            example = "1234567890123456", 
            required = true)
    @NotNull(message = "Card number is required")
    @Pattern(regexp = "^(?!0+$)[0-9]{16}$", 
             message = "CARD ID FILTER,IF SUPPLIED MUST BE A 16 DIGIT NUMBER")
    private String cardNumber;

    @Schema(description = "Associated account identifier - must be 11 digits", 
            example = "12345678901", 
            required = true)
    @NotNull(message = "Account ID is required")
    @Pattern(regexp = "^(?!0+$)[0-9]{11}$", 
             message = "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER")
    private String accountId;

    @Schema(description = "Current status of the credit card (single character)", 
            example = "A", 
            required = true)
    @NotNull(message = "Card status is required")
    @Size(min = 1, max = 1, message = "Card status must be exactly 1 character")
    @Pattern(regexp = "^[AIBCPSELTD]$", 
             message = "Card status must be a valid status code")
    private String cardStatus;
}
