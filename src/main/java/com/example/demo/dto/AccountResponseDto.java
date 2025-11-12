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

    @Schema(description = "Current account balance with 2 decimal places, signed amount", example = "2500.75")
    private BigDecimal currentBalance;

    @Schema(description = "Credit limit with 2 decimal places, signed amount", example = "10000.00")
    private BigDecimal creditLimit;

    @Schema(description = "Cash credit limit with 2 decimal places, signed amount", example = "2000.00")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account opening date in YYYYMMDD format", example = "20230101")
    private String openDate;

    @Schema(description = "Account expiration date in YYYYMMDD format", example = "20280101")
    private String expirationDate;

    @Schema(description = "Card reissue date in YYYYMMDD format", example = "20250601")
    private String reissueDate;

    @Schema(description = "Current cycle credit amount with 2 decimal places, signed amount", example = "750.00")
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current cycle debit amount with 2 decimal places, signed amount", example = "450.00")
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account grouping identifier", example = "GROUP123")
    private String groupId;

    @Schema(description = "Account active status indicator", example = "A")
    private String accountStatus;

    @Schema(description = "Available credit calculated as credit limit minus current balance", example = "7500.00")
    private BigDecimal availableCredit;

    @Schema(description = "Available cash credit calculated as cash credit limit minus current balance", example = "500.00")
    private BigDecimal availableCashCredit;

    @Schema(description = "Net cycle amount calculated as current cycle credit minus current cycle debit", example = "200.00")
    private BigDecimal netCycleAmount;

    @Schema(description = "Credit utilization percentage calculated as (current balance / credit limit) * 100", example = "25.00")
    private BigDecimal creditUtilizationPercentage;

    @Schema(description = "Display name for account active status", example = "Active")
    private String activeStatusDisplay;

    @Schema(description = "Display name for account status", example = "Active")
    private String accountStatusDisplay;

    @Schema(description = "Indicates if the account is expired based on expiration date", example = "false")
    private Boolean isExpired;

    @Schema(description = "Indicates if the account is over the credit limit", example = "false")
    private Boolean isOverLimit;

    @Schema(description = "Total accumulated interest for the account", example = "125.50")
    private BigDecimal totalAccumulatedInterest;

    @Schema(description = "Number of days since account was opened", example = "365")
    private Long daysSinceOpened;

    @Schema(description = "Number of days until account expires", example = "1095")
    private Long daysUntilExpiration;

    @Schema(description = "Timestamp when the account was created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the account was last updated", example = "2023-06-15T14:30:00")
    private LocalDateTime updatedAt;
}
