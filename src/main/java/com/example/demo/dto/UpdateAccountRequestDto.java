package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Schema(description = "Unique account identifier used as the record key", example = "12345678901", required = true)
    @NotNull(message = "Account ID is required")
    @Pattern(regexp = "^\\d{11}$", message = "Invalid Account ID format")
    private String acctId;

    @Schema(description = "Indicates if the account is active. Possible values: 'A' for active, 'I' for inactive", example = "A", required = false)
    @Size(min = 1, max = 1, message = "Account active status must be 1 character")
    private String acctActiveStatus;

    @Schema(description = "Current balance of the account, includes decimal places for cents", example = "1500.50", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Current balance must be non-negative")
    private BigDecimal acctCurrBal;

    @Schema(description = "Maximum credit limit for the account, includes decimal places for cents", example = "5000.00", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be non-negative")
    private BigDecimal acctCreditLimit;

    @Schema(description = "Maximum cash credit limit for the account, includes decimal places for cents", example = "1000.00", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Cash credit limit must be non-negative")
    private BigDecimal acctCashCreditLimit;

    @Schema(description = "Date when the account was opened, format YYYY-MM-DD", example = "2023-01-15", required = false)
    private LocalDate acctOpenDate;

    @Schema(description = "Date when the account expires, format YYYY-MM-DD", example = "2026-01-15", required = false)
    private LocalDate acctExpirationDate;

    @Schema(description = "Date when the account was reissued, format YYYY-MM-DD", example = "2024-01-15", required = false)
    private LocalDate acctReissueDate;

    @Schema(description = "Current cycle credit amount, includes decimal places for cents", example = "250.00", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Current cycle credit must be non-negative")
    private BigDecimal acctCurrCycCredit;

    @Schema(description = "Current cycle debit amount, includes decimal places for cents", example = "150.00", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Current cycle debit must be non-negative")
    private BigDecimal acctCurrCycDebit;

    @Schema(description = "Identifier for the account group, may be used for categorizing accounts", example = "GRP001", required = false)
    private String acctGroupId;
}
