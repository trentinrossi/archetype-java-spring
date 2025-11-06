package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {
    
    @Schema(description = "Card number portion of composite primary key", example = "1234567890123456", required = true, minLength = 16, maxLength = 16)
    private String cardNumber;
    
    @Schema(description = "Unique transaction identifier portion of composite primary key", example = "TXN1234567890123", required = true, minLength = 16, maxLength = 16)
    private String transactionId;
    
    @Schema(description = "Complete transaction details including amounts, dates, merchant information, and status", example = "AMT:100.00|DATE:2024-01-15|MERCHANT:ACME Store|STATUS:APPROVED", required = true, maxLength = 318)
    private String transactionData;
}