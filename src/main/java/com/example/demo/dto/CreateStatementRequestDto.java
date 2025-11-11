package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStatementRequestDto {

    @NotNull(message = "Account ID must be 11 digits numeric and exist in account file")
    @Min(value = 10000000000L, message = "Account ID must be 11 digits numeric")
    @Max(value = 99999999999L, message = "Account ID must be 11 digits numeric")
    private Long accountId;

    private LocalDateTime statementPeriodStart;
    
    private LocalDateTime statementPeriodEnd;
}
