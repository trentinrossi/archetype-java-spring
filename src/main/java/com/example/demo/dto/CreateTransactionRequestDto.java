package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {
    
    @Schema(description = "Card number portion of composite primary key", example = "1234567890123456", required = true)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String cardNumber;
    
    @Schema(description = "Unique transaction identifier portion of composite primary key", example = "TXN1234567890123", required = true)
    @NotBlank(message = "Transaction ID is required")
    @Size(min = 16, max = 16, message = "Transaction ID must be exactly 16 characters")
    private String transactionId;
    
    @Schema(description = "Complete transaction details including amounts, dates, merchant information, and status", required = true)
    @NotBlank(message = "Transaction data is required")
    @Size(max = 318, message = "Transaction data exceeds maximum length of 318 characters")
    private String transactionData;
}
