package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceDto {

    @Schema(description = "Unique account identifier", example = "ACC00001234", required = true)
    @NotBlank(message = "Acct ID can NOT be empty...")
    @Size(max = 11, message = "Account ID must not exceed 11 characters")
    private String accountId;

    @Schema(description = "Current account balance with 2 decimal places", example = "1500.00")
    private BigDecimal currentBalance;

    @Schema(description = "Indicates if account has a positive balance available for payment", example = "true")
    private Boolean hasPositiveBalance;
}
