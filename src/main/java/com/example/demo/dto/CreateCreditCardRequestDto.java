package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditCardRequestDto {

    @Schema(description = "16-digit credit card number", example = "1234567890123456", required = true)
    @NotBlank(message = "Card number must be provided and must be 16 digits")
    @Pattern(regexp = "^[0-9]{16}$", message = "CARD NUMBER MUST BE 16 DIGITS")
    @Pattern(regexp = "^(?!0+$)[0-9]{16}$", message = "Please enter a valid card number")
    private String cardNumber;

    @Schema(description = "11-digit account number associated with the card", example = "12345678901", required = true)
    @NotNull(message = "Account ID must be provided and must be 11 digits")
    private Long accountId;

    @Schema(description = "Name embossed on the credit card (alphabets and spaces only)", example = "JOHN DOE", required = false)
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Card name must contain only alphabets and spaces")
    @Size(max = 50, message = "Embossed name must not exceed 50 characters")
    private String embossedName;

    @Schema(description = "Card expiration date in YYYY-MM-DD format", example = "2025-12-31", required = true)
    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;

    @Schema(description = "Active status of the card - Y for active, N for inactive", example = "Y", required = true)
    @NotBlank(message = "Card status is required")
    @Pattern(regexp = "^[YN]$", message = "Card status must be Y (active) or N (inactive)")
    @Size(min = 1, max = 1, message = "Card status must be exactly 1 character")
    private String cardStatus;

    @Schema(description = "Card Verification Value (3 digits)", example = "123", required = true)
    @NotBlank(message = "CVV code is required")
    @Pattern(regexp = "^[0-9]{3}$", message = "CVV code must be 3 digits")
    private String cvvCode;
}
