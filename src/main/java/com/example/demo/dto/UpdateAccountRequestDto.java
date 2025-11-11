package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Schema(description = "Current account balance with 2 decimal places", example = "1500.00", required = false)
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    private BigDecimal currentBalance;
}
