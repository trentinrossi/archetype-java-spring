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
    private String acctId;

    @Schema(description = "Indicates if the account is active. Possible values: 'A' for active, 'I' for inactive", example = "A")
    private String acctActiveStatus;

    @Schema(description = "Current balance of the account, includes decimal places for cents", example = "1500.50")
    private BigDecimal acctCurrBal;

    @Schema(description = "Maximum credit limit for the account, includes decimal places for cents", example = "5000.00")
    private BigDecimal acctCreditLimit;

    @Schema(description = "Maximum cash credit limit for the account, includes decimal places for cents", example = "1000.00")
    private BigDecimal acctCashCreditLimit;

    @Schema(description = "Date when the account was opened, format YYYY-MM-DD", example = "2023-01-15")
    private LocalDate acctOpenDate;

    @Schema(description = "Date when the account expires, format YYYY-MM-DD", example = "2026-01-15")
    private LocalDate acctExpirationDate;

    @Schema(description = "Date when the account was reissued, format YYYY-MM-DD", example = "2024-01-15")
    private LocalDate acctReissueDate;

    @Schema(description = "Current cycle credit amount, includes decimal places for cents", example = "250.00")
    private BigDecimal acctCurrCycCredit;

    @Schema(description = "Current cycle debit amount, includes decimal places for cents", example = "150.00")
    private BigDecimal acctCurrCycDebit;

    @Schema(description = "Identifier for the account group, may be used for categorizing accounts", example = "GRP001")
    private String acctGroupId;

    @Schema(description = "Available credit calculated as credit limit minus current balance", example = "3500.50")
    private BigDecimal availableCredit;

    @Schema(description = "Available cash credit calculated as cash credit limit minus current balance", example = "500.00")
    private BigDecimal availableCashCredit;

    @Schema(description = "Current cycle net amount calculated as current cycle credit minus current cycle debit", example = "100.00")
    private BigDecimal currentCycleNetAmount;

    @Schema(description = "Indicates if the account is currently active based on active status", example = "true")
    private Boolean isActive;

    @Schema(description = "Indicates if the account has expired based on expiration date", example = "false")
    private Boolean isExpired;

    @Schema(description = "Indicates if the account has been reissued based on reissue date presence", example = "true")
    private Boolean hasBeenReissued;

    @Schema(description = "Active status display name", example = "Active")
    private String activeStatusDisplayName;

    @Schema(description = "Timestamp when the record was created", example = "2023-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the record was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;

    public void computeDerivedFields() {
        if (this.acctCreditLimit != null && this.acctCurrBal != null) {
            this.availableCredit = this.acctCreditLimit.subtract(this.acctCurrBal);
        }

        if (this.acctCashCreditLimit != null && this.acctCurrBal != null) {
            this.availableCashCredit = this.acctCashCreditLimit.subtract(this.acctCurrBal);
        }

        if (this.acctCurrCycCredit != null && this.acctCurrCycDebit != null) {
            this.currentCycleNetAmount = this.acctCurrCycCredit.subtract(this.acctCurrCycDebit);
        }

        this.isActive = "A".equalsIgnoreCase(this.acctActiveStatus);

        if (this.acctExpirationDate != null) {
            this.isExpired = this.acctExpirationDate.isBefore(LocalDate.now());
        }

        this.hasBeenReissued = this.acctReissueDate != null;

        if ("A".equalsIgnoreCase(this.acctActiveStatus)) {
            this.activeStatusDisplayName = "Active";
        } else if ("I".equalsIgnoreCase(this.acctActiveStatus)) {
            this.activeStatusDisplayName = "Inactive";
        } else {
            this.activeStatusDisplayName = "Unknown";
        }
    }
}
