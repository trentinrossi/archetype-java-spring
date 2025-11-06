package com.example.demo.dto;

import com.example.demo.enums.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for transaction data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    
    @Schema(description = "Card number (masked)", example = "************1111")
    private String cardNumber;
    
    @Schema(description = "Transaction ID", example = "TXN1234567890123")
    private String transactionId;
    
    @Schema(description = "Transaction type", example = "PURCHASE")
    private String transactionType;
    
    @Schema(description = "Transaction amount", example = "125.50")
    private BigDecimal transactionAmount;
    
    @Schema(description = "Transaction date", example = "2024-01-20")
    private LocalDate transactionDate;
    
    @Schema(description = "Transaction time", example = "2024-01-20T14:30:00")
    private LocalDateTime transactionTime;
    
    @Schema(description = "Post date", example = "2024-01-21")
    private LocalDate postDate;
    
    @Schema(description = "Merchant name", example = "Amazon.com")
    private String merchantName;
    
    @Schema(description = "Merchant category", example = "ONLINE_RETAIL")
    private String merchantCategory;
    
    @Schema(description = "Merchant ID", example = "AMZN123")
    private String merchantId;
    
    @Schema(description = "Merchant city", example = "Seattle")
    private String merchantCity;
    
    @Schema(description = "Merchant state", example = "WA")
    private String merchantState;
    
    @Schema(description = "Merchant country", example = "USA")
    private String merchantCountry;
    
    @Schema(description = "Merchant ZIP code", example = "98101")
    private String merchantZip;
    
    @Schema(description = "Authorization code", example = "AUTH123456")
    private String authorizationCode;
    
    @Schema(description = "Authorization date", example = "2024-01-20")
    private LocalDate authorizationDate;
    
    @Schema(description = "Authorization amount", example = "125.50")
    private BigDecimal authorizationAmount;
    
    @Schema(description = "Transaction status", example = "POSTED")
    private TransactionStatus transactionStatus;
    
    @Schema(description = "Transaction status display name", example = "Posted")
    private String transactionStatusDisplayName;
    
    @Schema(description = "Currency code", example = "USD")
    private String currencyCode;
    
    @Schema(description = "Original amount (for foreign transactions)", example = "100.00")
    private BigDecimal originalAmount;
    
    @Schema(description = "Original currency (for foreign transactions)", example = "EUR")
    private String originalCurrency;
    
    @Schema(description = "Exchange rate", example = "1.0850")
    private BigDecimal exchangeRate;
    
    @Schema(description = "Reference number", example = "REF987654321")
    private String referenceNumber;
    
    @Schema(description = "Description", example = "Online purchase")
    private String description;
    
    @Schema(description = "Is international transaction", example = "false")
    private Boolean isInternational;
    
    @Schema(description = "Is recurring transaction", example = "false")
    private Boolean isRecurring;
    
    @Schema(description = "Is disputed", example = "false")
    private Boolean isDisputed;
    
    @Schema(description = "Dispute date", example = "null")
    private LocalDate disputeDate;
    
    @Schema(description = "Dispute reason", example = "null")
    private String disputeReason;
    
    @Schema(description = "Is reversed", example = "false")
    private Boolean isReversed;
    
    @Schema(description = "Reversal date", example = "null")
    private LocalDate reversalDate;
    
    @Schema(description = "Reversal reason", example = "null")
    private String reversalReason;
    
    @Schema(description = "Reward points earned", example = "125")
    private Integer rewardPointsEarned;
    
    @Schema(description = "Cashback amount", example = "1.25")
    private BigDecimal cashbackAmount;
    
    @Schema(description = "Fee amount", example = "0.00")
    private BigDecimal feeAmount;
    
    @Schema(description = "Interest amount", example = "0.00")
    private BigDecimal interestAmount;
    
    @Schema(description = "Total amount (including fees and interest)", example = "125.50")
    private BigDecimal totalAmount;
    
    @Schema(description = "Statement date", example = "2024-02-01")
    private LocalDate statementDate;
    
    @Schema(description = "Billing cycle", example = "202401")
    private String billingCycle;
    
    @Schema(description = "Is pending", example = "false")
    private Boolean isPending;
    
    @Schema(description = "Is posted", example = "true")
    private Boolean isPosted;
    
    @Schema(description = "Can be disputed", example = "true")
    private Boolean canBeDisputed;
    
    @Schema(description = "Can be reversed", example = "false")
    private Boolean canBeReversed;
    
    @Schema(description = "Created timestamp", example = "2024-01-20T14:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last updated timestamp", example = "2024-01-21T10:00:00")
    private LocalDateTime updatedAt;
}
