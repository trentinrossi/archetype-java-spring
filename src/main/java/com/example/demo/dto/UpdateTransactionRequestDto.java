package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {

    @Schema(description = "Date when the transaction occurred", example = "01/15/24", required = false)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/\\d{2}$", message = "Date must be in MM/DD/YY format")
    private String transactionDate;

    @Schema(description = "Description of the transaction", example = "Purchase at grocery store", required = false)
    @Size(max = 26, message = "Description must not exceed 26 characters")
    private String transactionDescription;

    @Schema(description = "Amount of the transaction", example = "125.50", required = false)
    @DecimalMin(value = "-99999999.99", message = "Amount must be >= -99999999.99")
    @DecimalMax(value = "99999999.99", message = "Amount must be <= 99999999.99")
    private BigDecimal transactionAmount;

    @Schema(description = "Transaction type code", example = "01", required = false)
    private Integer tranTypeCd;

    @Schema(description = "Transaction category code", example = "1001", required = false)
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "ONLINE", required = false)
    @Size(max = 10, message = "Source must not exceed 10 characters")
    private String tranSource;

    @Schema(description = "Description of the transaction", example = "Purchase at Walmart", required = false)
    @Size(max = 60, message = "Description must not exceed 60 characters")
    private String tranDesc;

    @Schema(description = "Transaction amount", example = "125.50", required = false)
    @DecimalMin(value = "-99999999.99", message = "Amount must be >= -99999999.99")
    @DecimalMax(value = "99999999.99", message = "Amount must be <= 99999999.99")
    private BigDecimal tranAmt;

    @Schema(description = "Merchant name", example = "Walmart", required = false)
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String tranMerchantName;

    @Schema(description = "Merchant city", example = "New York", required = false)
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String tranMerchantCity;

    @Schema(description = "Merchant ZIP", example = "10001", required = false)
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String tranMerchantZip;

    @Schema(description = "Original timestamp", example = "2024-01-15", required = false)
    private LocalDate tranOrigTs;

    @Schema(description = "Processing timestamp", example = "2024-01-15", required = false)
    private LocalDate tranProcTs;

    @Schema(description = "Transaction type code", example = "PU", required = false)
    @Size(max = 2, message = "Type code must not exceed 2 characters")
    private String transactionTypeCode;

    @Schema(description = "Transaction category code", example = "GROC", required = false)
    @Size(max = 4, message = "Category code must not exceed 4 characters")
    private String transactionCategoryCode;

    @Schema(description = "Transaction source", example = "ONLINE", required = false)
    @Size(max = 10, message = "Source must not exceed 10 characters")
    private String transactionSource;

    @Schema(description = "Original transaction timestamp", example = "2024-01-15T10:30:00", required = false)
    private LocalDateTime originalTransactionTimestamp;

    @Schema(description = "Processed transaction timestamp", example = "2024-01-15T10:35:00", required = false)
    private LocalDateTime processedTransactionTimestamp;

    @Schema(description = "Merchant name", example = "Walmart", required = false)
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String merchantName;

    @Schema(description = "Merchant city", example = "New York", required = false)
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String merchantCity;

    @Schema(description = "Merchant ZIP", example = "10001", required = false)
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String merchantZip;
}
