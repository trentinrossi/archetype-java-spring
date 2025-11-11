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
public class TransactionResponseDto {

    @Schema(description = "Unique transaction identifier", example = "1")
    private Long tranId;

    @Schema(description = "Transaction type code (02 for bill payment)", example = "02")
    private String tranTypeCd;

    @Schema(description = "Transaction category code (2 for bill payment)", example = "2")
    private Integer tranCatCd;

    @Schema(description = "Source of transaction", example = "POS TERM")
    private String tranSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE")
    private String tranDesc;

    @Schema(description = "Transaction amount with 2 decimal places", example = "1500.00")
    private BigDecimal tranAmt;

    @Schema(description = "Card number used for transaction", example = "1234567890123456")
    private String tranCardNum;

    @Schema(description = "Masked card number for display", example = "************3456")
    private String maskedCardNum;

    @Schema(description = "Merchant identifier", example = "999999999")
    private Integer tranMerchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT")
    private String tranMerchantName;

    @Schema(description = "Merchant city", example = "NEW YORK")
    private String tranMerchantCity;

    @Schema(description = "Merchant ZIP code", example = "10001")
    private String tranMerchantZip;

    @Schema(description = "Transaction origination timestamp")
    private LocalDateTime tranOrigTs;

    @Schema(description = "Transaction processing timestamp")
    private LocalDateTime tranProcTs;

    @Schema(description = "Account ID for the transaction", example = "ACC00001234")
    private String accountId;

    @Schema(description = "Formatted transaction amount", example = "$1500.00")
    private String formattedAmount;

    @Schema(description = "Indicates if this is a bill payment transaction", example = "true")
    private Boolean isBillPayment;

    @Schema(description = "Transaction summary", example = "Transaction ID: 1, Type: BILL PAYMENT - ONLINE, Amount: $1500.00, Date: 2024-01-15T10:30:00")
    private String transactionSummary;

    @Schema(description = "Transaction creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Transaction last update timestamp")
    private LocalDateTime updatedAt;

    public TransactionResponseDto(Long tranId, String tranTypeCd, Integer tranCatCd, String tranSource,
                                 String tranDesc, BigDecimal tranAmt, String tranCardNum, Integer tranMerchantId,
                                 String tranMerchantName, String tranMerchantCity, String tranMerchantZip,
                                 LocalDateTime tranOrigTs, LocalDateTime tranProcTs, String accountId) {
        this.tranId = tranId;
        this.tranTypeCd = tranTypeCd;
        this.tranCatCd = tranCatCd;
        this.tranSource = tranSource;
        this.tranDesc = tranDesc;
        this.tranAmt = tranAmt;
        this.tranCardNum = tranCardNum;
        this.maskedCardNum = maskCardNumber(tranCardNum);
        this.tranMerchantId = tranMerchantId;
        this.tranMerchantName = tranMerchantName;
        this.tranMerchantCity = tranMerchantCity;
        this.tranMerchantZip = tranMerchantZip;
        this.tranOrigTs = tranOrigTs;
        this.tranProcTs = tranProcTs;
        this.accountId = accountId;
        this.formattedAmount = formatAmount(tranAmt);
        this.isBillPayment = "02".equals(tranTypeCd) && Integer.valueOf(2).equals(tranCatCd);
        this.transactionSummary = buildTransactionSummary();
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

    private String buildTransactionSummary() {
        return String.format("Transaction ID: %d, Type: %s, Amount: %s, Date: %s",
                           this.tranId, this.tranDesc, this.formattedAmount,
                           this.tranOrigTs != null ? this.tranOrigTs.toString() : "N/A");
    }
}
