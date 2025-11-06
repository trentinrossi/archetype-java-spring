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
    
    @Schema(description = "Card number portion of composite primary key", example = "1234567890123456")
    private String cardNumber;
    
    @Schema(description = "Unique transaction identifier portion of composite primary key", example = "TXN1234567890123")
    private String transactionId;
    
    @Schema(description = "Complete transaction details including amounts, dates, merchant information, and status")
    private String transactionData;
    
    @Schema(description = "Timestamp when the transaction record was created")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the transaction record was last updated")
    private LocalDateTime updatedAt;
}
