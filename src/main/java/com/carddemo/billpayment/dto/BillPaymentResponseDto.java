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
public class BillPaymentResponseDto {

    @Schema(description = "Payment status", example = "SUCCESS")
    private String status;

    @Schema(description = "Payment message", example = "Bill payment processed successfully")
    private String message;

    @Schema(description = "Account ID", example = "ACC00001234")
    private String accountId;

    @Schema(description = "Payment amount", example = "1500.00")
    private BigDecimal paymentAmount;

    @Schema(description = "Previous account balance", example = "1500.00")
    private BigDecimal previousBalance;

    @Schema(description = "New account balance", example = "0.00")
    private BigDecimal newBalance;

    @Schema(description = "Transaction ID", example = "1")
    private Long transactionId;

    @Schema(description = "Card number used (masked)", example = "************3456")
    private String maskedCardNumber;

    @Schema(description = "Payment timestamp")
    private LocalDateTime paymentTimestamp;

    @Schema(description = "Formatted payment amount", example = "$1500.00")
    private String formattedPaymentAmount;

    public BillPaymentResponseDto(String status, String message, String accountId, 
                                 BigDecimal paymentAmount, BigDecimal previousBalance, 
                                 BigDecimal newBalance, Long transactionId, String cardNumber) {
        this.status = status;
        this.message = message;
        this.accountId = accountId;
        this.paymentAmount = paymentAmount;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
        this.transactionId = transactionId;
        this.maskedCardNumber = maskCardNumber(cardNumber);
        this.paymentTimestamp = LocalDateTime.now();
        this.formattedPaymentAmount = formatAmount(paymentAmount);
    }

    private String maskCardNumber(String cardNum) {
        if (cardNum == null || cardNum.length() != 16) {
            return "****************";
        }
        return "************" + cardNum.substring(12);
    }

    private String formatAmount(BigDecimal amount) {
        return amount != null ? String.format("$%.2f", amount) : "$0.00";
    }
}
