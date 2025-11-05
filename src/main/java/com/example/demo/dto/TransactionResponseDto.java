package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    
    @Schema(description = "Transaction database ID", example = "1")
    private Long id;
    
    @Schema(description = "Transaction ID", example = "TXN123456789")
    private String transactionId;
    
    @Schema(description = "Card number", example = "1234567890123456")
    private String cardNumber;
    
    @Schema(description = "Type code", example = "01")
    private String typeCode;
    
    @Schema(description = "Category code", example = "5411")
    private String categoryCode;
    
    @Schema(description = "Source", example = "POS")
    private String source;
    
    @Schema(description = "Description", example = "Purchase at grocery store")
    private String description;
    
    @Schema(description = "Amount", example = "12345.67")
    private BigDecimal amount;
    
    @Schema(description = "Merchant ID", example = "123456789")
    private Long merchantId;
    
    @Schema(description = "Merchant name", example = "ABC Grocery Store")
    private String merchantName;
    
    @Schema(description = "Merchant city", example = "New York")
    private String merchantCity;
    
    @Schema(description = "Merchant ZIP code", example = "10001")
    private String merchantZip;
    
    @Schema(description = "Original timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime originalTimestamp;
    
    @Schema(description = "Processing timestamp", example = "2024-01-15T10:30:05")
    private LocalDateTime processingTimestamp;
    
    @Schema(description = "Created at timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Updated at timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
