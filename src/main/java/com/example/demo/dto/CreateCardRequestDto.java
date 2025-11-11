package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequestDto {

    @NotBlank(message = "Card number must be 16 characters alphanumeric")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters alphanumeric")
    @Pattern(regexp = "^[a-zA-Z0-9]{16}$", message = "Card number must be 16 characters alphanumeric")
    private String cardNumber;

    @NotNull(message = "Customer ID must be 9 digits numeric and exist in customer file")
    @Min(value = 100000000L, message = "Customer ID must be 9 digits numeric")
    @Max(value = 999999999L, message = "Customer ID must be 9 digits numeric")
    private Long customerId;

    @NotNull(message = "Account ID must be 11 digits numeric and exist in account file")
    @Min(value = 10000000000L, message = "Account ID must be 11 digits numeric")
    @Max(value = 99999999999L, message = "Account ID must be 11 digits numeric")
    private Long accountId;
}
