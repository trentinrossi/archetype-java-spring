package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {

    @Schema(description = "Unique identifier for the transaction", example = "1234567890123456", required = true)
    @NotBlank(message = "Transaction ID cannot be empty")
    @Size(min = 16, max = 16, message = "Transaction ID must be 16 characters")
    private String transactionId;

    @Schema(description = "Transaction type code", example = "01", required = true)
    @NotNull(message = "Transaction Type Code must be entered")
    private Integer tranTypeCd;

    @Schema(description = "Transaction category code", example = "1001", required = true)
    @NotNull(message = "Transaction Category Code must be entered")
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "ONLINE", required = true)
    @NotBlank(message = "Transaction Source must be entered")
    @Size(max = 10, message = "Transaction source must not exceed 10 characters")
    private String tranSource;

    @Schema(description = "Description of the transaction", example = "Purchase at ABC Store", required = true)
    @NotBlank(message = "Transaction Description must be entered")
    @Size(max = 60, message = "Transaction description must not exceed 60 characters")
    private String tranDesc;

    @Schema(description = "Transaction amount with two decimal places", example = "1234.56", required = true)
    @NotNull(message = "Transaction Amount must be entered")
    private BigDecimal tranAmt;

    @Schema(description = "Card number associated with the transaction", example = "4532123456789012", required = true)
    @NotBlank(message = "Card number must be entered")
    @Size(max = 16, message = "Card number must not exceed 16 characters")
    private String tranCardNum;

    @Schema(description = "Merchant identifier", example = "123456789", required = true)
    @NotBlank(message = "Merchant ID must be entered")
    @Size(max = 9, message = "Merchant ID must not exceed 9 characters")
    private String tranMerchantId;

    @Schema(description = "Name of the merchant", example = "ABC Grocery Store", required = true)
    @NotBlank(message = "Merchant Name must be entered")
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String tranMerchantName;

    @Schema(description = "City of the merchant", example = "New York", required = true)
    @NotBlank(message = "Merchant City must be entered")
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String tranMerchantCity;

    @Schema(description = "ZIP code of the merchant", example = "10001", required = true)
    @NotBlank(message = "Merchant Zip must be entered")
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String tranMerchantZip;

    @Schema(description = "Original transaction date in YYYY-MM-DD format", example = "2023-12-31", required = true)
    @NotBlank(message = "Original Transaction Date must be entered")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format YYYY-MM-DD")
    private String tranOrigTs;

    @Schema(description = "Processing transaction date in YYYY-MM-DD format", example = "2023-12-31", required = true)
    @NotBlank(message = "Processing Transaction Date must be entered")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format YYYY-MM-DD")
    private String tranProcTs;

    @Schema(description = "Account ID (optional if card number provided)", example = "12345678901", required = false)
    private String accountId;

    @Schema(description = "Card ID (optional if account ID provided)", example = "4532123456789012", required = false)
    private String cardId;

    @Schema(description = "Merchant ID reference (optional)", example = "123456789", required = false)
    private String merchantId;
}
