package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Schema(description = "Current account balance with 2 decimal places", example = "1500.00", required = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Account balance must be greater than or equal to zero")
    private BigDecimal acctCurrBal;
}
