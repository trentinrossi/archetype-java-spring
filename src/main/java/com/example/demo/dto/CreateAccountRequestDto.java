package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {

    @NotNull(message = "Account ID must be 11 digits numeric and exist in account file")
    @Min(value = 10000000000L, message = "Account ID must be 11 digits numeric")
    @Max(value = 99999999999L, message = "Account ID must be 11 digits numeric")
    private Long accountId;

    @NotNull(message = "Current balance is required")
    @Digits(integer = 10, fraction = 2, message = "Current balance must have at most 10 integer digits and 2 decimal places")
    private BigDecimal currentBalance;

    @NotNull(message = "Credit limit is required")
    @Digits(integer = 10, fraction = 2, message = "Credit limit must have at most 10 integer digits and 2 decimal places")
    private BigDecimal creditLimit;

    @NotNull(message = "Customer ID is required")
    @Min(value = 100000000L, message = "Customer ID must be 9 digits numeric")
    @Max(value = 999999999L, message = "Customer ID must be 9 digits numeric")
    private Long customerId;
}
