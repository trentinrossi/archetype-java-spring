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
public class TransactionResponseDto {

    @Schema(description = "Transaction database ID", example = "1")
    private Long id;

    @Schema(description = "Unique identifier for transaction", example = "1234567890123456")
    private Long tranId;

    @Schema(description = "Transaction type code", example = "02")
    private String tranTypeCd;

    @Schema(description = "Transaction category code", example = "2")
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "POS TERM")
    private String tranSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE")
    private String tranDesc;

    @Schema(description = "Transaction amount", example = "1500.00")
    private BigDecimal tranAmt;

    @Schema(description = "Formatted transaction amount", example = "$1,500.00")
    private String tranAmtFormatted;

    @Schema(description = "Card number associated with transaction", example = "4111111111111111")
    private String tranCardNum;

    @Schema(description = "Merchant identifier", example = "999999999")
    private Long tranMerchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT")
    private String tranMerchantName;

    @Schema(description = "Merchant city", example = "N/A")
    private String tranMerchantCity;

    @Schema(description = "Merchant ZIP code", example = "N/A")
    private String tranMerchantZip;

    @Schema(description = "Transaction origination timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime tranOrigTs;

    @Schema(description = "Transaction processing timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime tranProcTs;

    @Schema(description = "Account ID associated with transaction", example = "ACC123456789")
    private String tranAcctId;

    @Schema(description = "Indicates if this is a bill payment transaction", example = "true")
    private Boolean isBillPayment;

    @Schema(description = "Transaction creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Transaction last update timestamp", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
