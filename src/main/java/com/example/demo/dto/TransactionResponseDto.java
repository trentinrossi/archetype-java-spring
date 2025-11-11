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

    @Schema(description = "Unique transaction identifier", example = "123456789")
    private Long transactionId;

    @Schema(description = "Transaction type code", example = "02")
    private String transactionTypeCode;

    @Schema(description = "Transaction category code", example = "2")
    private Integer transactionCategoryCode;

    @Schema(description = "Source of transaction", example = "POS TERM")
    private String transactionSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE")
    private String description;

    @Schema(description = "Transaction amount with 2 decimal places", example = "1500.00")
    private BigDecimal amount;

    @Schema(description = "Card number used for transaction", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Merchant identifier", example = "999999999")
    private Long merchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT")
    private String merchantName;

    @Schema(description = "Merchant city", example = "N/A")
    private String merchantCity;

    @Schema(description = "Merchant ZIP code", example = "N/A")
    private String merchantZip;

    @Schema(description = "Transaction origination timestamp", example = "2024-01-15T14:45:00")
    private LocalDateTime originationTimestamp;

    @Schema(description = "Transaction processing timestamp", example = "2024-01-15T14:45:00")
    private LocalDateTime processingTimestamp;

    @Schema(description = "Account ID for the transaction", example = "ACC00001234")
    private String accountId;

    @Schema(description = "Timestamp when the transaction was created", example = "2024-01-15T14:45:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the transaction was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
