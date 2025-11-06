package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {
    
    @Schema(description = "Primary key - unique account identification number (11 numeric digits)", example = "12345678901", required = true)
    private Long accountId;
    
    @Schema(description = "Account balance, credit limit, dates, and status information with proper decimal handling", example = "Account data containing balance and credit information", required = true)
    private String accountData;
}