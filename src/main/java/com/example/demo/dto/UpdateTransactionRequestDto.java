package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {
    
    @Schema(description = "Complete transaction details including amounts, dates, merchant information, and status (max 318 characters)", example = "Transaction data containing amounts, dates, merchant information, and status information...", required = false)
    private String transactionData;
}