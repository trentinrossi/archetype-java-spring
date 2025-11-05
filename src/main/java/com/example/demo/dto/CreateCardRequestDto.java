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
    
    @Schema(description = "Card number (16 digits)", example = "1234567890123456", required = true)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must contain only numeric digits")
    private String cardNumber;

    @Schema(description = "Status of the card", example = "ACTIVE", required = true)
    @NotBlank(message = "Status is required")
    private String status;

    @Schema(description = "Additional card details", example = "Premium card with rewards", required = false)
    @Size(max = 150, message = "Card details must not exceed 150 characters")
    private String cardDetails;
}
