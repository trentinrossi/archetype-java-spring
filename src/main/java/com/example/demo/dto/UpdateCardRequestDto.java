package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardRequestDto {

    @Min(value = 100000000L, message = "Customer ID must be 9 digits numeric")
    @Max(value = 999999999L, message = "Customer ID must be 9 digits numeric")
    private Long customerId;

    @Min(value = 10000000000L, message = "Account ID must be 11 digits numeric")
    @Max(value = 99999999999L, message = "Account ID must be 11 digits numeric")
    private Long accountId;
}
