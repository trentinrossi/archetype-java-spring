package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "Unique identifier for account", example = "ACC123456789", required = true)
    @NotNull(message = "Acct ID can NOT be empty...")
    @NotBlank(message = "Acct ID can NOT be empty...")
    private String acctId;

    @Schema(description = "Current balance of the account", example = "1500.75", required = true)
    @NotNull(message = "Account current balance is required")
    @DecimalMin(value = "0.01", message = "You have nothing to pay...")
    private BigDecimal acctCurrBal;
}
