package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a new account
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {
    
    @Schema(description = "Account ID - must be 11 numeric digits", example = "12345678901", required = true)
    private String accountId;
    
    @Schema(description = "Customer ID", example = "123456789", required = true)
    private String customerId;
    
    @Schema(description = "Account number", example = "4111111111111111", required = false)
    private String accountNumber;
    
    @Schema(description = "Account type", example = "CREDIT_CARD", required = true)
    private String accountType;
    
    @Schema(description = "Credit limit", example = "5000.00", required = true)
    private BigDecimal creditLimit;
    
    @Schema(description = "Cash advance limit", example = "1000.00", required = false)
    private BigDecimal cashAdvanceLimit;
    
    @Schema(description = "Interest rate", example = "18.99", required = false)
    private BigDecimal interestRate;
    
    @Schema(description = "Annual fee", example = "95.00", required = false)
    private BigDecimal annualFee;
    
    @Schema(description = "Account open date", example = "2023-01-15", required = true)
    private LocalDate accountOpenDate;
    
    @Schema(description = "Autopay enabled", example = "false", required = false)
    private Boolean autopayEnabled;
    
    @Schema(description = "Paperless statements", example = "true", required = false)
    private Boolean paperlessStatements;
}
