package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {
    
    @Schema(description = "Complete transaction details including amounts, dates, merchant information, and status", required = false)
    @Size(max = 318, message = "Transaction data exceeds maximum length of 318 characters")
    private String transactionData;
}
