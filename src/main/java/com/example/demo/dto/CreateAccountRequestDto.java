package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateAccountRequestDto
 * 
 * Request DTO for creating a new account.
 * 
 * Business Rules Implemented:
 * - BR004: Account Filter Validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "Unique account identifier - must be numeric and exactly 11 digits", 
            example = "12345678901", 
            required = true)
    @NotNull(message = "Account ID is required")
    @Pattern(regexp = "^(?!0+$)[0-9]{11}$", 
             message = "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER")
    private String accountId;
}
