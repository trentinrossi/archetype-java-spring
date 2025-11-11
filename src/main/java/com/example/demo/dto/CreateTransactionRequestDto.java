package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequestDto {

    @NotBlank(message = "Card number must be 16 characters alphanumeric")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters alphanumeric")
    @Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "Card number must be 16 characters alphanumeric")
    private String cardNumber;

    @NotBlank(message = "Transaction ID must be 16 characters alphanumeric and unique")
    @Size(min = 16, max = 16, message = "Transaction ID must be 16 characters alphanumeric and unique")
    @Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "Transaction ID must be 16 characters alphanumeric and unique")
    private String transactionId;

    @NotBlank(message = "Transaction type code is required")
    @Size(min = 2, max = 2, message = "Transaction type code must be 2 characters")
    private String transactionTypeCode;

    @NotBlank(message = "Transaction category code is required")
    @Size(min = 4, max = 4, message = "Transaction category code must be 4 digits")
    @Pattern(regexp = "^\\d{4}$", message = "Transaction category code must be 4 digits numeric")
    private String transactionCategoryCode;

    @NotBlank(message = "Transaction source is required")
    @Size(max = 10, message = "Transaction source must be maximum 10 characters")
    private String transactionSource;

    @NotBlank(message = "Transaction description is required")
    @Size(max = 100, message = "Transaction description must be maximum 100 characters")
    private String transactionDescription;

    @NotNull(message = "Transaction amount must be valid signed numeric with 2 decimal places")
    @Digits(integer = 9, fraction = 2, message = "Transaction amount must be valid signed numeric with 2 decimal places")
    private BigDecimal transactionAmount;
}
