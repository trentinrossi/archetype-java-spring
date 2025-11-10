package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new account.
 * Account ID must be exactly 11 digits numeric.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {
    
    @Schema(description = "Unique 11-digit account identifier", example = "12345678901", required = true)
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "^\\d{11}$", message = "Account ID must be exactly 11 digits")
    private String accountId;
}
