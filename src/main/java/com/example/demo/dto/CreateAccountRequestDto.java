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
public class CreateAccountRequestDto {
    
    @Schema(description = "Unique account identification number (11 numeric digits)", example = "12345678901", required = true)
    @NotBlank(message = "Account ID is required")
    @Pattern(regexp = "\\d{11}", message = "Account ID must be 11 numeric digits")
    private String accountId;
    
    @Schema(description = "Account balance, credit limit, dates, and status information with proper decimal handling", example = "Account data containing balance and credit information", required = true)
    @NotBlank(message = "Account data is required")
    @Size(max = 289, message = "Account data must not exceed 289 characters")
    private String accountData;
    
    @Schema(description = "Customer ID associated with this account", example = "1", required = true)
    @NotBlank(message = "Customer ID is required")
    private Long customerId;
}
