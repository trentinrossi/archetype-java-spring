package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "Unique account identifier", example = "ACC00001234", required = true)
    @NotBlank(message = "Acct ID can NOT be empty...")
    @Size(min = 11, max = 11, message = "Account ID must be exactly 11 characters")
    private String acctId;

    @Schema(description = "Current account balance with 2 decimal places", example = "1500.00", required = true)
    @NotNull(message = "Account current balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Account balance must be greater than or equal to zero")
    private BigDecimal acctCurrBal;
}
