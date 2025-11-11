package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreditCardFilterRequestDto
 * 
 * Request DTO for filtering credit cards.
 * 
 * Business Rules Implemented:
 * - BR002: Card Number Filter Validation
 * - BR004: Account Filter Validation
 * - BR005: Card Status Filter Validation
 * - BR006: Filter Record Matching
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardFilterRequestDto {

    @Schema(description = "Filter by 16-digit credit card number (optional)", 
            example = "1234567890123456", 
            required = false)
    @Pattern(regexp = "^$|^(?!0+$)[0-9]{16}$", 
             message = "CARD ID FILTER,IF SUPPLIED MUST BE A 16 DIGIT NUMBER")
    private String cardNumber;

    @Schema(description = "Filter by 11-digit account identifier (optional)", 
            example = "12345678901", 
            required = false)
    @Pattern(regexp = "^$|^(?!0+$)[0-9]{11}$", 
             message = "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER")
    private String accountId;

    @Schema(description = "Filter by card status (optional)", 
            example = "A", 
            required = false)
    @Pattern(regexp = "^$|^[AIBCPSELTD]$", 
             message = "Card status must be a valid status code if supplied")
    private String cardStatus;

    @Schema(description = "Page number for pagination (0-based)", 
            example = "0", 
            required = false)
    private Integer page;

    @Schema(description = "Page size for pagination", 
            example = "10", 
            required = false)
    private Integer size;

    /**
     * Checks if card number filter is blank, spaces, or zeros
     * Implements BR002: Card Number Filter Validation
     * 
     * @return true if blank, false otherwise
     */
    public boolean isCardNumberBlank() {
        return cardNumber == null || cardNumber.trim().isEmpty() || cardNumber.matches("^0+$");
    }

    /**
     * Checks if account ID filter is blank, spaces, or zeros
     * Implements BR004: Account Filter Validation
     * 
     * @return true if blank, false otherwise
     */
    public boolean isAccountIdBlank() {
        return accountId == null || accountId.trim().isEmpty() || accountId.matches("^0+$");
    }

    /**
     * Checks if card status filter is blank
     * Implements BR005: Card Status Filter Validation
     * 
     * @return true if blank, false otherwise
     */
    public boolean isCardStatusBlank() {
        return cardStatus == null || cardStatus.trim().isEmpty();
    }

    /**
     * Checks if any filter is applied
     * Implements BR006: Filter Record Matching
     * 
     * @return true if at least one filter is applied, false otherwise
     */
    public boolean hasAnyFilter() {
        return !isCardNumberBlank() || !isAccountIdBlank() || !isCardStatusBlank();
    }
}
