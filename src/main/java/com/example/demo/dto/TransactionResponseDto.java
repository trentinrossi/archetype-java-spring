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

    @Schema(description = "Date when the transaction occurred", example = "01/15/24")
    private String transactionDate;

    @Schema(description = "Description of the transaction", example = "Purchase at grocery store")
    private String transactionDescription;

    @Schema(description = "Amount of the transaction", example = "125.50")
    private BigDecimal transactionAmount;

    @Schema(description = "Transaction timestamp", example = "2024-01-15T10:30:00")
    private String transactionTimestamp;

    @Schema(description = "Unique transaction identifier", example = "1234567890123456")
    private Long tranId;

    @Schema(description = "Transaction type code", example = "01")
    private Integer tranTypeCd;

    @Schema(description = "Transaction category code", example = "1001")
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "ONLINE")
    private String tranSource;

    @Schema(description = "Description of the transaction", example = "Purchase at Walmart")
    private String tranDesc;

    @Schema(description = "Transaction amount", example = "125.50")
    private BigDecimal tranAmt;

    @Schema(description = "Card number associated with the transaction", example = "4111111111111111")
    private Long tranCardNum;

    @Schema(description = "Merchant identifier", example = "123456789")
    private Long tranMerchantId;

    @Schema(description = "Name of the merchant", example = "Walmart")
    private String tranMerchantName;

    @Schema(description = "City of the merchant", example = "New York")
    private String tranMerchantCity;

    @Schema(description = "ZIP code of the merchant", example = "10001")
    private String tranMerchantZip;

    @Schema(description = "Original timestamp", example = "2024-01-15")
    private LocalDate tranOrigTs;

    @Schema(description = "Processing timestamp", example = "2024-01-15")
    private LocalDate tranProcTs;

    @Schema(description = "Credit card number", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Transaction type code", example = "PU")
    private String transactionTypeCode;

    @Schema(description = "Transaction category code", example = "GROC")
    private String transactionCategoryCode;

    @Schema(description = "Transaction source", example = "ONLINE")
    private String transactionSource;

    @Schema(description = "Original transaction timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime originalTransactionTimestamp;

    @Schema(description = "Processed transaction timestamp", example = "2024-01-15T10:35:00")
    private LocalDateTime processedTransactionTimestamp;

    @Schema(description = "Merchant ID", example = "123456789")
    private String merchantId;

    @Schema(description = "Merchant name", example = "Walmart")
    private String merchantName;

    @Schema(description = "Merchant city", example = "New York")
    private String merchantCity;

    @Schema(description = "Merchant ZIP", example = "10001")
    private String merchantZip;

    @Schema(description = "Formatted transaction date in MM/DD/YY format", example = "01/15/24")
    private String formattedTransactionDate;

    @Schema(description = "Formatted transaction amount with sign", example = "+125.50")
    private String formattedTransactionAmount;

    @Schema(description = "Formatted original timestamp", example = "01/15/2024")
    private String formattedOriginalTimestamp;

    @Schema(description = "Formatted processed timestamp", example = "01/15/2024")
    private String formattedProcessedTimestamp;

    @Schema(description = "Transaction type display name", example = "Purchase")
    private String transactionTypeDisplayName;

    @Schema(description = "Transaction category display name", example = "Groceries")
    private String transactionCategoryDisplayName;

    @Schema(description = "Transaction source display name", example = "Online Purchase")
    private String transactionSourceDisplayName;

    @Schema(description = "Masked card number", example = "************1111")
    private String maskedCardNumber;

    @Schema(description = "Full merchant address", example = "New York, 10001")
    private String fullMerchantAddress;

    @Schema(description = "Transaction sign indicator", example = "+")
    private String transactionSign;

    @Schema(description = "Absolute value of transaction amount", example = "125.50")
    private BigDecimal absoluteTransactionAmount;

    @Schema(description = "Indicates if this is a debit transaction", example = "false")
    private Boolean isDebit;

    @Schema(description = "Indicates if this is a credit transaction", example = "true")
    private Boolean isCredit;

    @Schema(description = "Page number where this transaction appears", example = "1")
    private Integer pageNumber;

    @Schema(description = "Position of this transaction on the page", example = "5")
    private Integer positionOnPage;

    @Schema(description = "Indicates if this transaction can be selected", example = "true")
    private Boolean selectable;

    @Schema(description = "Navigation context", example = "LIST")
    private String navigationContext;

    @Schema(description = "Account ID", example = "12345678901")
    private Long accountId;

    @Schema(description = "Card ID", example = "4111111111111111")
    private Long cardId;

    @Schema(description = "Created timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Updated timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
