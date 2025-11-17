package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {

    @Schema(description = "Unique identifier for the transaction", example = "1234567890123456", required = true)
    @NotBlank(message = "Transaction ID cannot be empty")
    @Size(min = 16, max = 16, message = "Transaction ID must be 16 characters")
    private String transactionId;

    @Schema(description = "Date when the transaction occurred in MM/DD/YY format", example = "01/15/24", required = true)
    @NotBlank(message = "Transaction date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{2}$", message = "Transaction date must be in MM/DD/YY format")
    private String transactionDate;

    @Schema(description = "Description of the transaction", example = "Purchase at grocery store", required = true)
    @NotBlank(message = "Transaction description is required")
    @Size(max = 26, message = "Transaction description must not exceed 26 characters")
    private String transactionDescription;

    @Schema(description = "Amount of the transaction", example = "125.50", required = true)
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "-99999999.99", message = "Transaction amount must be >= -99999999.99")
    @DecimalMax(value = "99999999.99", message = "Transaction amount must be <= 99999999.99")
    private BigDecimal transactionAmount;

    @Schema(description = "Transaction type code", example = "01", required = true)
    @NotNull(message = "Transaction type code is required")
    private Integer tranTypeCd;

    @Schema(description = "Transaction category code", example = "1001", required = true)
    @NotNull(message = "Transaction category code is required")
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "ONLINE", required = true)
    @NotBlank(message = "Transaction source is required")
    @Size(max = 10, message = "Transaction source must not exceed 10 characters")
    private String tranSource;

    @Schema(description = "Description of the transaction", example = "Purchase at Walmart", required = true)
    @NotBlank(message = "Transaction description is required")
    @Size(max = 60, message = "Transaction description must not exceed 60 characters")
    private String tranDesc;

    @Schema(description = "Transaction amount", example = "125.50", required = true)
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "-99999999.99", message = "Amount must be >= -99999999.99")
    @DecimalMax(value = "99999999.99", message = "Amount must be <= 99999999.99")
    private BigDecimal tranAmt;

    @Schema(description = "Card number associated with the transaction", example = "4111111111111111", required = true)
    @NotNull(message = "Card number is required")
    private Long tranCardNum;

    @Schema(description = "Merchant identifier", example = "123456789", required = true)
    @NotNull(message = "Merchant ID is required")
    private Long tranMerchantId;

    @Schema(description = "Name of the merchant", example = "Walmart", required = true)
    @NotBlank(message = "Merchant name is required")
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String tranMerchantName;

    @Schema(description = "City of the merchant", example = "New York", required = true)
    @NotBlank(message = "Merchant city is required")
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String tranMerchantCity;

    @Schema(description = "ZIP code of the merchant", example = "10001", required = true)
    @NotBlank(message = "Merchant ZIP is required")
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String tranMerchantZip;

    @Schema(description = "Original timestamp in YYYY-MM-DD", example = "2024-01-15", required = true)
    @NotNull(message = "Original timestamp is required")
    private LocalDate tranOrigTs;

    @Schema(description = "Processing timestamp in YYYY-MM-DD", example = "2024-01-15", required = true)
    @NotNull(message = "Processing timestamp is required")
    private LocalDate tranProcTs;

    @Schema(description = "Credit card number", example = "4111111111111111", required = true)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters")
    @Pattern(regexp = "^\\d{16}$", message = "Card number must be 16 digits")
    private String cardNumber;

    @Schema(description = "Transaction type code", example = "PU", required = true)
    @NotBlank(message = "Transaction type code is required")
    @Size(max = 2, message = "Type code must not exceed 2 characters")
    private String transactionTypeCode;

    @Schema(description = "Transaction category code", example = "GROC", required = true)
    @NotBlank(message = "Category code is required")
    @Size(max = 4, message = "Category code must not exceed 4 characters")
    private String transactionCategoryCode;

    @Schema(description = "Transaction source", example = "ONLINE", required = true)
    @NotBlank(message = "Transaction source is required")
    @Size(max = 10, message = "Source must not exceed 10 characters")
    private String transactionSource;

    @Schema(description = "Original transaction timestamp", example = "2024-01-15T10:30:00", required = true)
    @NotNull(message = "Original timestamp is required")
    private LocalDateTime originalTransactionTimestamp;

    @Schema(description = "Processed transaction timestamp", example = "2024-01-15T10:35:00", required = true)
    @NotNull(message = "Processed timestamp is required")
    private LocalDateTime processedTransactionTimestamp;

    @Schema(description = "Merchant ID", example = "123456789", required = true)
    @NotBlank(message = "Merchant ID is required")
    @Size(max = 9, message = "Merchant ID must not exceed 9 characters")
    @Pattern(regexp = "^\\d{1,9}$", message = "Merchant ID must be numeric")
    private String merchantId;

    @Schema(description = "Merchant name", example = "Walmart", required = true)
    @NotBlank(message = "Merchant name is required")
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String merchantName;

    @Schema(description = "Merchant city", example = "New York", required = true)
    @NotBlank(message = "Merchant city is required")
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String merchantCity;

    @Schema(description = "Merchant ZIP", example = "10001", required = true)
    @NotBlank(message = "Merchant ZIP is required")
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String merchantZip;

    @Schema(description = "Account ID", example = "12345678901", required = false)
    private Long accountId;

    @Schema(description = "Card ID", example = "4111111111111111", required = false)
    private Long cardId;
}
