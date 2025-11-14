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
public class CreateTransactionRequestDto {

    @Schema(description = "Unique identifier for transaction", example = "1234567890123456", required = true)
    private Long tranId;

    @Schema(description = "Transaction type code", example = "02", required = true)
    private String tranTypeCd;

    @Schema(description = "Transaction category code", example = "2", required = true)
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "POS TERM", required = true)
    private String tranSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE", required = true)
    private String tranDesc;

    @Schema(description = "Transaction amount", example = "1500.00", required = true)
    private BigDecimal tranAmt;

    @Schema(description = "Card number associated with transaction", example = "4111111111111111", required = true)
    private String tranCardNum;

    @Schema(description = "Merchant identifier", example = "999999999", required = true)
    private Long tranMerchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT", required = true)
    private String tranMerchantName;

    @Schema(description = "Merchant city", example = "N/A", required = true)
    private String tranMerchantCity;

    @Schema(description = "Merchant ZIP code", example = "N/A", required = true)
    private String tranMerchantZip;

    @Schema(description = "Transaction origination timestamp", example = "2024-01-15T10:30:00", required = true)
    private LocalDateTime tranOrigTs;

    @Schema(description = "Transaction processing timestamp", example = "2024-01-15T10:30:00", required = true)
    private LocalDateTime tranProcTs;

    @Schema(description = "Account ID associated with transaction", example = "ACC123456789", required = true)
    private String tranAcctId;
}
