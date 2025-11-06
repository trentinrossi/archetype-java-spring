package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    
    @Schema(description = "Card number portion of composite primary key", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @Schema(description = "Unique transaction identifier portion of composite primary key", example = "TXN1234567890123", required = true)
    private String transactionId;
    
    @Schema(description = "Complete transaction details including amounts, dates, merchant information, and status", required = true)
    private String transactionData;
    
    @Schema(description = "Timestamp when the transaction record was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the transaction record was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}