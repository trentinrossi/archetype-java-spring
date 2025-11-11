package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {

    @Schema(description = "Transaction type code (02 for bill payment)", example = "02", required = true)
    @NotBlank(message = "Transaction type code is required")
    @Size(max = 2, message = "Transaction type code must not exceed 2 characters")
    private String transactionTypeCode;

    @Schema(description = "Transaction category code (2 for bill payment)", example = "2", required = true)
    @NotNull(message = "Transaction category code is required")
    private Integer transactionCategoryCode;

    @Schema(description = "Source of transaction", example = "POS TERM", required = true)
    @NotBlank(message = "Transaction source is required")
    @Size(max = 10, message = "Transaction source must not exceed 10 characters")
    private String transactionSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE", required = true)
    @NotBlank(message = "Transaction description is required")
    @Size(max = 50, message = "Transaction description must not exceed 50 characters")
    private String description;

    @Schema(description = "Transaction amount with 2 decimal places", example = "1500.00", required = true)
    @NotNull(message = "Transaction amount is required")
    private BigDecimal amount;

    @Schema(description = "Card number used for transaction", example = "4111111111111111", required = true)
    @NotBlank(message = "Card number is required")
    @Size(max = 16, message = "Card number must not exceed 16 characters")
    private String cardNumber;

    @Schema(description = "Merchant identifier", example = "999999999", required = true)
    @NotNull(message = "Merchant ID is required")
    private Long merchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT", required = true)
    @NotBlank(message = "Merchant name is required")
    @Size(max = 50, message = "Merchant name must not exceed 50 characters")
    private String merchantName;

    @Schema(description = "Merchant city", example = "N/A", required = true)
    @NotBlank(message = "Merchant city is required")
    @Size(max = 50, message = "Merchant city must not exceed 50 characters")
    private String merchantCity;

    @Schema(description = "Merchant ZIP code", example = "N/A", required = true)
    @NotBlank(message = "Merchant ZIP code is required")
    @Size(max = 10, message = "Merchant ZIP code must not exceed 10 characters")
    private String merchantZip;

    @Schema(description = "Account ID for the transaction", example = "ACC00001234", required = true)
    @NotBlank(message = "Account ID is required")
    @Size(max = 11, message = "Account ID must not exceed 11 characters")
    private String accountId;
}
