package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequestDto {
    
    @Schema(description = "Card number (16 digits)", example = "1234567890123456", required = true)
    @NotNull(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must contain only numeric digits")
    private String cardNumber;
    
    @Schema(description = "Account ID (max 11 digits)", example = "12345678901", required = true)
    @NotNull(message = "Account ID is required")
    @Digits(integer = 11, fraction = 0, message = "Account ID must be a numeric value with maximum 11 digits")
    private Long accountId;
    
    @Schema(description = "Customer ID (max 9 digits)", example = "123456789", required = true)
    @NotNull(message = "Customer ID is required")
    @Digits(integer = 9, fraction = 0, message = "Customer ID must be a numeric value with maximum 9 digits")
    private Long customerId;
}
