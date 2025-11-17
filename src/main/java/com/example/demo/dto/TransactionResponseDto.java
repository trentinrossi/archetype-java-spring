package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    @Schema(description = "Unique identifier for the transaction", example = "1234567890123456")
    private String transactionId;

    @Schema(description = "Unique transaction numeric ID", example = "1234567890123456")
    private Long tranId;

    @Schema(description = "Transaction type code", example = "01")
    private Integer tranTypeCd;

    @Schema(description = "Transaction category code", example = "1001")
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "ONLINE")
    private String tranSource;

    @Schema(description = "Description of the transaction", example = "Purchase at ABC Store")
    private String tranDesc;

    @Schema(description = "Transaction amount with two decimal places", example = "1234.56")
    private BigDecimal tranAmt;

    @Schema(description = "Card number associated with the transaction", example = "4532123456789012")
    private String tranCardNum;

    @Schema(description = "Merchant identifier", example = "123456789")
    private String tranMerchantId;

    @Schema(description = "Name of the merchant", example = "ABC Grocery Store")
    private String tranMerchantName;

    @Schema(description = "City of the merchant", example = "New York")
    private String tranMerchantCity;

    @Schema(description = "ZIP code of the merchant", example = "10001")
    private String tranMerchantZip;

    @Schema(description = "Original transaction date", example = "2023-12-31")
    private LocalDate tranOrigTs;

    @Schema(description = "Processing transaction date", example = "2023-12-31")
    private LocalDate tranProcTs;

    @Schema(description = "Formatted transaction date (MM/DD/YY)", example = "12/31/23")
    private String formattedTransactionDate;

    @Schema(description = "Formatted transaction amount with sign", example = "+1234.56")
    private String formattedTransactionAmount;

    @Schema(description = "Account ID associated with transaction", example = "12345678901")
    private String accountId;

    @Schema(description = "Card ID associated with transaction", example = "4532123456789012")
    private String cardId;

    @Schema(description = "Merchant ID reference", example = "123456789")
    private String merchantId;

    @Schema(description = "Timestamp when record was created", example = "2023-12-31T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when record was last updated", example = "2023-12-31T14:30:00")
    private LocalDateTime updatedAt;
}
