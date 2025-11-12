package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    @Schema(description = "Primary key for account identification", example = "12345678901")
    private String accountId;

    @Schema(description = "Account active status - Y for active, N for inactive", example = "Y")
    private String activeStatus;

    @Schema(description = "Account active status display name", example = "Active")
    private String activeStatusDisplay;

    @Schema(description = "Current account balance with 2 decimal places, signed amount", example = "1500.50")
    private BigDecimal currentBalance;

    @Schema(description = "Credit limit with 2 decimal places, signed amount", example = "5000.00")
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit with 2 decimal places, signed amount", example = "1000.00")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account opening date in YYYYMMDD format", example = "20230115")
    private String openDate;

    @Schema(description = "Account opening date formatted for display", example = "2023-01-15")
    private String openDateFormatted;

    @Schema(description = "Account expiration date in YYYYMMDD format", example = "20280115")
    private String expirationDate;

    @Schema(description = "Account expiration date formatted for display", example = "2028-01-15")
    private String expirationDateFormatted;

    @Schema(description = "Card reissue date in YYYYMMDD format", example = "20250115")
    private String reissueDate;

    @Schema(description = "Card reissue date formatted for display", example = "2025-01-15")
    private String reissueDateFormatted;

    @Schema(description = "Current cycle credit amount with 2 decimal places, signed amount", example = "250.00")
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current cycle debit amount with 2 decimal places, signed amount", example = "100.00")
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account grouping identifier", example = "GRP001")
    private String groupId;

    @Schema(description = "Comprehensive account information including balances and status", example = "Account data string")
    private String accountData;

    @Schema(description = "Available credit calculated as credit limit minus current balance", example = "3500.50")
    private BigDecimal availableCredit;

    @Schema(description = "Available cash credit calculated as cash credit limit minus current balance", example = "500.00")
    private BigDecimal availableCashCredit;

    @Schema(description = "Net cycle amount calculated as current cycle credit minus current cycle debit", example = "150.00")
    private BigDecimal netCycleAmount;

    @Schema(description = "Indicates if account is expired based on expiration date", example = "false")
    private Boolean isExpired;

    @Schema(description = "Indicates if account is active based on active status", example = "true")
    private Boolean isActive;

    @Schema(description = "Credit utilization percentage", example = "30.01")
    private BigDecimal creditUtilizationPercentage;

    @Schema(description = "Days until expiration", example = "1095")
    private Long daysUntilExpiration;

    @Schema(description = "Account age in days since opening", example = "365")
    private Long accountAgeInDays;

    @Schema(description = "Formatted current balance", example = "$1,500.50")
    private String formattedBalance;

    @Schema(description = "Formatted credit limit", example = "$5,000.00")
    private String formattedCreditLimit;

    @Schema(description = "Formatted cash credit limit", example = "$1,000.00")
    private String formattedCashCreditLimit;

    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp")
    private LocalDateTime updatedAt;
}
