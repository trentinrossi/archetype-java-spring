package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {

    @Size(min = 2, max = 2, message = "Transaction type code must be 2 characters")
    private String transactionTypeCode;

    @Size(min = 4, max = 4, message = "Transaction category code must be 4 digits")
    @Pattern(regexp = "^\\d{4}$", message = "Transaction category code must be 4 digits numeric")
    private String transactionCategoryCode;

    @Size(max = 10, message = "Transaction source must be maximum 10 characters")
    private String transactionSource;

    @Size(max = 100, message = "Transaction description must be maximum 100 characters")
    private String transactionDescription;

    @Digits(integer = 9, fraction = 2, message = "Transaction amount must be valid signed numeric with 2 decimal places")
    private BigDecimal transactionAmount;
}
