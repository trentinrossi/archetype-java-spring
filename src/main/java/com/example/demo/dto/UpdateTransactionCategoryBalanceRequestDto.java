package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionCategoryBalanceRequestDto {

    @Schema(description = "Transaction type code", example = "01", required = false)
    private String trancatTypeCd;

    @Schema(description = "Transaction category code", example = "0001", required = false)
    private String trancatCd;

    @Schema(description = "Balance for the transaction category", example = "1500.50", required = false)
    private BigDecimal tranCatBal;
}
