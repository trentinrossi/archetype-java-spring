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
public class AccountResponseDto {

    @Schema(description = "Unique identifier for account", example = "ACC123456789")
    private String acctId;

    @Schema(description = "Current balance of the account", example = "1500.75")
    private BigDecimal acctCurrBal;

    @Schema(description = "Formatted current balance of the account", example = "$1,500.75")
    private String formattedAcctCurrBal;

    @Schema(description = "Number of cards associated with this account", example = "3")
    private Integer cardCount;

    @Schema(description = "Number of transactions for this account", example = "45")
    private Integer transactionCount;

    @Schema(description = "Indicates if account has a positive balance for payment", example = "true")
    private Boolean hasBalanceToPay;

    @Schema(description = "Payment amount (equals current balance for full payment)", example = "1500.75")
    private BigDecimal paymentAmount;

    @Schema(description = "Account creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Account last update timestamp", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
