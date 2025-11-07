package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for account data.
 * Contains all account fields including computed values.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    
    @Schema(description = "Unique account identifier", example = "12345678901")
    private Long acctId;
    
    @Schema(description = "Account active status ('A' for active, 'I' for inactive)", example = "A")
    private String acctActiveStatus;
    
    @Schema(description = "Human-readable active status", example = "Active")
    private String acctActiveStatusDisplay;
    
    @Schema(description = "Indicates if the account is currently active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Current balance of the account", example = "1500.50")
    private BigDecimal acctCurrBal;
    
    @Schema(description = "Maximum credit limit for the account", example = "5000.00")
    private BigDecimal acctCreditLimit;
    
    @Schema(description = "Maximum cash credit limit for the account", example = "1000.00")
    private BigDecimal acctCashCreditLimit;
    
    @Schema(description = "Available credit (credit limit minus current balance)", example = "3500.50")
    private BigDecimal availableCredit;
    
    @Schema(description = "Available cash credit (cash credit limit minus current balance)", example = "-500.50")
    private BigDecimal availableCashCredit;
    
    @Schema(description = "Date when the account was opened", example = "2024-01-15")
    private LocalDate acctOpenDate;
    
    @Schema(description = "Date when the account expires", example = "2027-01-15")
    private LocalDate acctExpirationDate;
    
    @Schema(description = "Indicates if the account is expired", example = "false")
    private Boolean isExpired;
    
    @Schema(description = "Date when the account was reissued", example = "2025-06-01")
    private LocalDate acctReissueDate;
    
    @Schema(description = "Current cycle credit amount", example = "250.00")
    private BigDecimal acctCurrCycCredit;
    
    @Schema(description = "Current cycle debit amount", example = "150.00")
    private BigDecimal acctCurrCycDebit;
    
    @Schema(description = "Net cycle amount (credit minus debit)", example = "100.00")
    private BigDecimal netCycleAmount;
    
    @Schema(description = "Identifier for the account group", example = "GRP001")
    private String acctGroupId;
    
    @Schema(description = "Timestamp when the account was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
