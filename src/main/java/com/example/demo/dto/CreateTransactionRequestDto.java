package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Request DTO for creating a new transaction
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {
    
    @Schema(description = "Card number - must be exactly 16 characters", example = "4111111111111111", required = true)
    private String cardNumber;
    
    @Schema(description = "Transaction ID - must be exactly 16 characters", example = "TXN1234567890123", required = true)
    private String transactionId;
    
    @Schema(description = "Transaction type", example = "PURCHASE", required = true)
    private String transactionType;
    
    @Schema(description = "Transaction amount", example = "125.50", required = true)
    private BigDecimal transactionAmount;
    
    @Schema(description = "Transaction date", example = "2024-01-20", required = true)
    private LocalDate transactionDate;
    
    @Schema(description = "Transaction time", example = "2024-01-20T14:30:00", required = false)
    private LocalDateTime transactionTime;
    
    @Schema(description = "Merchant name", example = "Amazon.com", required = false)
    private String merchantName;
    
    @Schema(description = "Merchant category", example = "ONLINE_RETAIL", required = false)
    private String merchantCategory;
    
    @Schema(description = "Merchant ID", example = "AMZN123", required = false)
    private String merchantId;
    
    @Schema(description = "Merchant city", example = "Seattle", required = false)
    private String merchantCity;
    
    @Schema(description = "Merchant state", example = "WA", required = false)
    private String merchantState;
    
    @Schema(description = "Merchant country", example = "USA", required = false)
    private String merchantCountry;
    
    @Schema(description = "Merchant ZIP code", example = "98101", required = false)
    private String merchantZip;
    
    @Schema(description = "Authorization code", example = "AUTH123456", required = false)
    private String authorizationCode;
    
    @Schema(description = "Currency code", example = "USD", required = false)
    private String currencyCode;
    
    @Schema(description = "Original amount (for foreign transactions)", example = "100.00", required = false)
    private BigDecimal originalAmount;
    
    @Schema(description = "Original currency (for foreign transactions)", example = "EUR", required = false)
    private String originalCurrency;
    
    @Schema(description = "Exchange rate", example = "1.0850", required = false)
    private BigDecimal exchangeRate;
    
    @Schema(description = "Reference number", example = "REF987654321", required = false)
    private String referenceNumber;
    
    @Schema(description = "Description", example = "Online purchase", required = false)
    private String description;
    
    @Schema(description = "Is international transaction", example = "false", required = false)
    private Boolean isInternational;
    
    @Schema(description = "Is recurring transaction", example = "false", required = false)
    private Boolean isRecurring;
}
