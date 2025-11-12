package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountCrossReferenceRequestDto {

    @Schema(description = "Credit card number serving as primary identifier", example = "1234567890123456", required = true)
    @NotNull(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters in length")
    private String cardNumber;

    @Schema(description = "Additional account cross-reference information", example = "Account cross-reference data", required = true)
    @NotNull(message = "Cross reference data is required")
    @Size(max = 34, message = "Cross reference data must not exceed 34 characters")
    private String crossReferenceData;
}
