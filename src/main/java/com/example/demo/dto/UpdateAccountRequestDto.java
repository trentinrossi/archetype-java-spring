package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateAccountRequestDto
 * 
 * Request DTO for updating an existing account.
 * 
 * Business Rules Implemented:
 * - BR004: Account Filter Validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Schema(description = "Unique account identifier - must be numeric and exactly 11 digits if supplied", 
            example = "12345678901", 
            required = false)
    @Pattern(regexp = "^(?!0+$)[0-9]{11}$", 
             message = "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER")
    private String accountId;
}
