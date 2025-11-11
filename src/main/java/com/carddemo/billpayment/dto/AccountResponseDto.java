package com.carddemo.billpayment.dto;

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

    @Schema(description = "Unique account identifier", example = "ACC00001234")
    private String acctId;

    @Schema(description = "Current account balance with 2 decimal places", example = "1500.00")
    private BigDecimal acctCurrBal;

    @Schema(description = "Indicates if the account has a positive balance available for payment", example = "true")
    private Boolean hasPositiveBalance;

    @Schema(description = "Payment amount equals the current account balance for full payment", example = "1500.00")
    private BigDecimal paymentAmount;

    @Schema(description = "Indicates if the account is eligible for bill payment", example = "true")
    private Boolean eligibleForPayment;

    @Schema(description = "Message indicating payment status or balance information", example = "Account ready for payment")
    private String paymentStatusMessage;

    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Account last update timestamp")
    private LocalDateTime updatedAt;

    public AccountResponseDto(String acctId, BigDecimal acctCurrBal) {
        this.acctId = acctId;
        this.acctCurrBal = acctCurrBal;
        this.hasPositiveBalance = acctCurrBal != null && acctCurrBal.compareTo(BigDecimal.ZERO) > 0;
        this.paymentAmount = this.hasPositiveBalance ? acctCurrBal : BigDecimal.ZERO;
        this.eligibleForPayment = this.hasPositiveBalance;
        this.paymentStatusMessage = determinePaymentStatusMessage();
    }

    private String determinePaymentStatusMessage() {
        if (acctCurrBal == null) {
            return "Balance information not available";
        }
        if (acctCurrBal.compareTo(BigDecimal.ZERO) <= 0) {
            return "You have nothing to pay...";
        }
        return "Account ready for payment";
    }
}
