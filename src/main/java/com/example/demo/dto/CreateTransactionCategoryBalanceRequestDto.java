package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionCategoryBalanceRequestDto {

    @Schema(description = "Account ID for the transaction category balance", example = "12345678901", required = true)
    @NotNull(message = "Account ID is required")
    private Long trancatAcctId;

    @Schema(description = "Transaction type code", example = "01", required = true)
    @NotNull(message = "Transaction type code is required")
    private String trancatTypeCd;

    @Schema(description = "Transaction category code", example = "0001", required = true)
    @NotNull(message = "Transaction category code is required")
    private String trancatCd;

    @Schema(description = "Balance for the transaction category", example = "1500.50", required = true)
    @NotNull(message = "Transaction category balance is required")
    private BigDecimal tranCatBal;
}
