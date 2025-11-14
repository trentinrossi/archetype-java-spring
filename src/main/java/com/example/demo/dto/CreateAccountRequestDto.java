package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "Account Identification Number - Primary key. Must be 11 digits, numeric, and non-zero", example = "12345678901", required = true)
    private Long accountId;

    @Schema(description = "Account Active Status (Y/N). Must be 'Y' or 'N'", example = "Y", required = true)
    private String activeStatus;

    @Schema(description = "Current Balance with 2 decimal places. Must be numeric with 2 decimal places and non-negative", example = "1500.50", required = true)
    private BigDecimal currentBalance;

    @Schema(description = "Credit Limit with 2 decimal places. Must be numeric with 2 decimal places and non-negative", example = "5000.00", required = true)
    private BigDecimal creditLimit;

    @Schema(description = "Cash Credit Limit with 2 decimal places. Must be numeric with 2 decimal places and non-negative", example = "1000.00", required = true)
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account Opening Date in YYYY-MM-DD format. Must be in YYYY-MM-DD format with valid year (1900-2099), month (01-12), and day", example = "2023-01-15", required = true)
    private LocalDate openDate;

    @Schema(description = "Account Expiration Date in YYYY-MM-DD format. Must be in YYYY-MM-DD format with valid year (1900-2099), month (01-12), and day", example = "2025-01-15", required = true)
    private LocalDate expirationDate;

    @Schema(description = "Account Reissue Date in YYYY-MM-DD format. Must be in YYYY-MM-DD format with valid year (1900-2099), month (01-12), and day", example = "2024-01-15", required = true)
    private LocalDate reissueDate;

    @Schema(description = "Current Cycle Credit with 2 decimal places. Must be numeric with 2 decimal places and non-negative", example = "250.75", required = true)
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current Cycle Debit with 2 decimal places. Must be numeric with 2 decimal places and non-negative", example = "300.25", required = true)
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account Group ID. Accounts can optionally be assigned to a group ID for organizational purposes", example = "GRP001", required = false)
    private String groupId;

    @Schema(description = "Customer Identification Number - Foreign key. Must exist in Customer Master file", example = "123456789", required = true)
    private Long customerId;

    @Schema(description = "Current status of the account. Account status indicates whether an account is active or inactive", example = "A", required = true)
    private String accountStatus;
}
