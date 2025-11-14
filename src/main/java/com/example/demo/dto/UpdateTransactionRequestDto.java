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
public class UpdateTransactionRequestDto {

    @Schema(description = "Transaction type code", example = "02", required = false)
    private String tranTypeCd;

    @Schema(description = "Transaction category code", example = "2", required = false)
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "POS TERM", required = false)
    private String tranSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE", required = false)
    private String tranDesc;

    @Schema(description = "Transaction amount", example = "1500.00", required = false)
    private BigDecimal tranAmt;

    @Schema(description = "Merchant identifier", example = "999999999", required = false)
    private Long tranMerchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT", required = false)
    private String tranMerchantName;

    @Schema(description = "Merchant city", example = "N/A", required = false)
    private String tranMerchantCity;

    @Schema(description = "Merchant ZIP code", example = "N/A", required = false)
    private String tranMerchantZip;

    @Schema(description = "Transaction processing timestamp", example = "2024-01-15T10:30:00", required = false)
    private LocalDateTime tranProcTs;
}
