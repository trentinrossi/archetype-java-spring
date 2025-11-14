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

    @Schema(description = "Account Identification Number - Primary key", example = "12345678901")
    private Long accountId;

    @Schema(description = "Account Active Status (Y/N)", example = "Y")
    private String activeStatus;

    @Schema(description = "Current Balance with 2 decimal places", example = "1500.50")
    private BigDecimal currentBalance;

    @Schema(description = "Credit Limit with 2 decimal places", example = "5000.00")
    private BigDecimal creditLimit;

    @Schema(description = "Cash Credit Limit with 2 decimal places", example = "1000.00")
    private BigDecimal cashCreditLimit;

    @Schema(description = "Account Opening Date in YYYY-MM-DD format", example = "2023-01-15")
    private LocalDate openDate;

    @Schema(description = "Account Expiration Date in YYYY-MM-DD format", example = "2025-01-15")
    private LocalDate expirationDate;

    @Schema(description = "Account Reissue Date in YYYY-MM-DD format", example = "2024-01-15")
    private LocalDate reissueDate;

    @Schema(description = "Current Cycle Credit with 2 decimal places", example = "250.75")
    private BigDecimal currentCycleCredit;

    @Schema(description = "Current Cycle Debit with 2 decimal places", example = "300.25")
    private BigDecimal currentCycleDebit;

    @Schema(description = "Account Group ID", example = "GRP001")
    private String groupId;

    @Schema(description = "Customer Identification Number - Foreign key", example = "123456789")
    private Long customerId;

    @Schema(description = "Current status of the account", example = "A")
    private String accountStatus;

    @Schema(description = "Available credit calculated as credit limit minus current balance", example = "3499.50")
    private BigDecimal availableCredit;

    @Schema(description = "Available cash credit calculated as cash credit limit minus current balance", example = "500.00")
    private BigDecimal availableCashCredit;

    @Schema(description = "Net cycle amount calculated as current cycle credit minus current cycle debit", example = "-49.50")
    private BigDecimal netCycleAmount;

    @Schema(description = "Indicates if account is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Indicates if account is expired", example = "false")
    private Boolean isExpired;

    @Schema(description = "Indicates if account has group assignment", example = "true")
    private Boolean hasGroupAssignment;

    @Schema(description = "Active status display value", example = "Active")
    private String activeStatusDisplay;

    @Schema(description = "Account status display value", example = "Active")
    private String accountStatusDisplay;

    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp")
    private LocalDateTime updatedAt;
}
