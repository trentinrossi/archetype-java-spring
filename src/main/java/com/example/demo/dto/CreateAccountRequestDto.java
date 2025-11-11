package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @Schema(description = "Unique account identifier", example = "ACC00001234", required = true)
    @NotBlank(message = "Acct ID can NOT be empty...")
    @Size(max = 11, message = "Account ID must not exceed 11 characters")
    private String accountId;

    @Schema(description = "Current account balance with 2 decimal places", example = "1500.00", required = true)
    @NotNull(message = "Current balance is required")
    @DecimalMin(value = "0.01", message = "You have nothing to pay...")
    private BigDecimal currentBalance;
}
