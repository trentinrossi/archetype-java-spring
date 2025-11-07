package com.example.demo.dto;

import com.example.demo.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new credit card
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCreditCardRequestDto {
    
    @Schema(description = "Unique 16-digit credit card number", 
            example = "1234567890123456", 
            required = true)
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 numeric digits")
    private String cardNumber;
    
    @Schema(description = "Associated account identifier - must be exactly 11 numeric digits", 
            example = "12345678901", 
            required = true)
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "\\d{11}", message = "Account ID must be exactly 11 numeric digits")
    private String accountId;
    
    @Schema(description = "Current status of the credit card", 
            example = "ACTIVE", 
            required = true)
    @NotNull(message = "Card status is required")
    private CardStatus cardStatus;
}
