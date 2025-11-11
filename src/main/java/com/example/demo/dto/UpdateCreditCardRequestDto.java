package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateCreditCardRequestDto
 * 
 * Request DTO for updating an existing credit card.
 * 
 * Business Rules Implemented:
 * - BR005: Card Status Filter Validation
 * - BR013: Update Card Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCreditCardRequestDto {

    @Schema(description = "Current status of the credit card (single character)", 
            example = "A", 
            required = false)
    @Size(min = 1, max = 1, message = "Card status must be exactly 1 character")
    @Pattern(regexp = "^[AIBCPSELTD]$", 
             message = "Card status must be a valid status code")
    private String cardStatus;
}
