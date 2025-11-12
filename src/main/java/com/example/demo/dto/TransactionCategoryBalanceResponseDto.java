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
public class TransactionCategoryBalanceResponseDto {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Account ID for the transaction category balance", example = "12345678901")
    private Long trancatAcctId;

    @Schema(description = "Transaction type code", example = "01")
    private String trancatTypeCd;

    @Schema(description = "Transaction category code", example = "0001")
    private String trancatCd;

    @Schema(description = "Balance for the transaction category", example = "1500.50")
    private BigDecimal tranCatBal;

    @Schema(description = "Timestamp when created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when last updated", example = "2023-06-15T14:30:00")
    private LocalDateTime updatedAt;
}
