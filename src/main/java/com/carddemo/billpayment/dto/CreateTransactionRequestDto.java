package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {

    @Schema(description = "Transaction type code (02 for bill payment)", example = "02", required = true)
    @NotBlank(message = "Transaction type code is required")
    @Size(min = 2, max = 2, message = "Transaction type code must be exactly 2 characters")
    private String tranTypeCd;

    @Schema(description = "Transaction category code (2 for bill payment)", example = "2", required = true)
    @NotNull(message = "Transaction category code is required")
    @Min(value = 0, message = "Transaction category code must be non-negative")
    @Max(value = 9, message = "Transaction category code must be a single digit")
    private Integer tranCatCd;

    @Schema(description = "Source of transaction", example = "POS TERM", required = true)
    @NotBlank(message = "Transaction source is required")
    @Size(max = 10, message = "Transaction source cannot exceed 10 characters")
    private String tranSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE", required = true)
    @NotBlank(message = "Transaction description is required")
    @Size(max = 50, message = "Transaction description cannot exceed 50 characters")
    private String tranDesc;

    @Schema(description = "Transaction amount with 2 decimal places", example = "1500.00", required = true)
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than zero")
    private BigDecimal tranAmt;

    @Schema(description = "Card number used for transaction", example = "1234567890123456", required = true)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String tranCardNum;

    @Schema(description = "Merchant identifier", example = "999999999", required = true)
    @NotNull(message = "Merchant ID is required")
    @Min(value = 0, message = "Merchant ID must be non-negative")
    private Integer tranMerchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT", required = true)
    @NotBlank(message = "Merchant name is required")
    @Size(max = 50, message = "Merchant name cannot exceed 50 characters")
    private String tranMerchantName;

    @Schema(description = "Merchant city", example = "NEW YORK", required = true)
    @NotBlank(message = "Merchant city is required")
    @Size(max = 50, message = "Merchant city cannot exceed 50 characters")
    private String tranMerchantCity;

    @Schema(description = "Merchant ZIP code", example = "10001", required = true)
    @NotBlank(message = "Merchant ZIP code is required")
    @Size(max = 10, message = "Merchant ZIP code cannot exceed 10 characters")
    private String tranMerchantZip;

    @Schema(description = "Transaction origination timestamp", example = "2024-01-15T10:30:00", required = false)
    private LocalDateTime tranOrigTs;

    @Schema(description = "Transaction processing timestamp", example = "2024-01-15T10:30:00", required = false)
    private LocalDateTime tranProcTs;

    @Schema(description = "Account ID for the transaction", example = "ACC00001234", required = true)
    @NotBlank(message = "Account ID is required")
    @Size(min = 11, max = 11, message = "Account ID must be exactly 11 characters")
    private String accountId;
}
