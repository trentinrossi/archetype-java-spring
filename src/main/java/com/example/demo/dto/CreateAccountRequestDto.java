package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {
    
    @Schema(description = "Account ID", example = "12345678901", required = true)
    @NotNull(message = "Account ID is required")
    @Digits(integer = 11, fraction = 0, message = "Account ID must be a maximum of 11 digits")
    private Long accountId;
    
    @Schema(description = "Current balance of the account", example = "1000.00", required = true)
    @NotNull(message = "Current balance is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current balance must be greater than or equal to 0")
    private BigDecimal currentBalance;
    
    @Schema(description = "Credit limit of the account", example = "5000.00", required = true)
    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Credit limit must be greater than or equal to 0")
    private BigDecimal creditLimit;
    
    @Schema(description = "Current cycle credit", example = "500.00", required = true)
    @NotNull(message = "Current cycle credit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current cycle credit must be greater than or equal to 0")
    private BigDecimal currentCycleCredit;
    
    @Schema(description = "Current cycle debit", example = "200.00", required = true)
    @NotNull(message = "Current cycle debit is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Current cycle debit must be greater than or equal to 0")
    private BigDecimal currentCycleDebit;
    
    @Schema(description = "Expiration date of the account", example = "2025-12-31", required = true)
    @NotNull(message = "Expiration date is required")
    private LocalDate expirationDate;
}
