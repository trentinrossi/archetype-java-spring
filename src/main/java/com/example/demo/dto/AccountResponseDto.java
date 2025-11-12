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
public class AccountResponseDto {

    @Schema(description = "Unique account identifier used as the record key", example = "12345678901")
    private Long accountId;

    @Schema(description = "Account active status indicator - Y for active, N for inactive", example = "Y")
    private String activeStatus;

    @Schema(description = "Current account balance with 2 decimal places, signed amount", example = "1500.50")
    private BigDecimal currentBalance;

    @Schema(description = "Credit limit with 2 decimal places, signed amount", example = "5000.00")
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit with 2 decimal places, signed amount", example = "1000.00")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account opening date in YYYYMMDD format", example = "2023-01-15")
    private LocalDate openDate;

    @Schema(description = "Account expiration date in YYYYMMDD format", example = "2026-01-15")
    private LocalDate expirationDate;

    @Schema(description = "Card reissue date in YYYYMMDD format", example = "2024-01-15")
    private LocalDate reissueDate;

    @Schema(description = "Current cycle credit amount with 2 decimal places, signed amount", example = "500.00")
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current cycle debit amount with 2 decimal places, signed amount", example = "300.00")
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account grouping identifier", example = "GROUP001")
    private String groupId;

    @Schema(description = "Comprehensive account information including balances and status", example = "Account data string")
    private String accountData;

    @Schema(description = "Available credit calculated as credit limit minus current balance", example = "3500.50")
    private BigDecimal availableCredit;

    @Schema(description = "Available cash credit calculated as cash credit limit minus current balance", example = "500.00")
    private BigDecimal availableCashCredit;

    @Schema(description = "Net cycle amount calculated as current cycle credit minus current cycle debit", example = "200.00")
    private BigDecimal netCycleAmount;

    @Schema(description = "Display value for active status - Active or Inactive", example = "Active")
    private String activeStatusDisplay;

    @Schema(description = "Indicates if account is expired", example = "false")
    private Boolean isExpired;

    @Schema(description = "Credit utilization percentage", example = "30.01")
    private BigDecimal creditUtilizationPercentage;

    @Schema(description = "Indicates if account has outstanding balance", example = "true")
    private Boolean hasOutstandingBalance;

    @Schema(description = "Formatted account ID with leading zeros", example = "00012345678")
    private String formattedAccountId;

    @Schema(description = "Account summary information", example = "Account 00012345678 - Status: Y - Balance: 1500.50 - Credit Limit: 5000.00")
    private String accountSummary;

    @Schema(description = "Timestamp when the account was created", example = "2023-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-15T14:20:00")
    private LocalDateTime updatedAt;
}
