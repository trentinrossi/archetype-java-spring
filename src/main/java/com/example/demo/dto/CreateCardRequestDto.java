package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequestDto {

    @Schema(description = "Credit card number - must be exactly 16 numeric digits", example = "4532015112830366", required = true)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 digits")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must contain only numbers and be exactly 16 digits")
    private String cardNumber;

    @Schema(description = "Associated account identifier - must be exactly 11 numeric digits, not all zeros", example = "12345678901", required = true)
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^(?!00000000000)[0-9]{11}$", message = "Account number must contain only numbers, be exactly 11 digits, and not be all zeros")
    private String accountId;

    @Schema(description = "Customer ID linked to the card - must be exactly 9 numeric digits", example = "123456789", required = true)
    @NotBlank(message = "Customer ID is required")
    @Size(max = 9, message = "Customer ID must be 9 digits or less")
    @Pattern(regexp = "^[0-9]{1,9}$", message = "Customer ID must contain only numbers and be up to 9 digits")
    private String customerId;

    @Schema(description = "Security code for the card - must be exactly 3 numeric digits", example = "123", required = true)
    @NotBlank(message = "CVV code is required")
    @Size(min = 3, max = 3, message = "CVV code must be 3 digits")
    @Pattern(regexp = "^[0-9]{3}$", message = "CVV code must contain only numbers and be exactly 3 digits")
    private String cvvCode;

    @Schema(description = "Date when the card expires in YYYY-MM-DD format", example = "2025-12-31", required = true)
    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid expiration date - must be in YYYY-MM-DD format")
    private String expirationDate;

    @Schema(description = "Credit card number for cross-reference - must be exactly 16 alphanumeric characters", example = "4532015112830366", required = true)
    @NotBlank(message = "Cross-reference card number is required")
    @Size(min = 16, max = 16, message = "Cross-reference card number must be 16 characters")
    private String xrefCardNum;
}
