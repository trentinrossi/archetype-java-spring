package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a new account.
 * Contains all required fields for account creation with validation rules.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDto {
    
    @Schema(description = "Unique account identifier (must be 11-digit numeric value)", 
            example = "12345678901", 
            required = true)
    @NotNull(message = "Account ID is required")
    @Min(value = 10000000000L, message = "Account ID must be an 11-digit number")
    @Max(value = 99999999999L, message = "Account ID must be an 11-digit number")
    private Long acctId;
    
    @Schema(description = "Account active status ('A' for active, 'I' for inactive)", 
            example = "A", 
            required = true)
    @NotBlank(message = "Account active status is required")
    @Pattern(regexp = "^[AI]$", message = "Account active status must be 'A' or 'I'")
    private String acctActiveStatus;
    
    @Schema(description = "Current balance of the account", 
            example = "1500.50", 
            required = true)
    @NotNull(message = "Current balance is required")
    @DecimalMin(value = "0.00", message = "Current balance cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Current balance must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCurrBal;
    
    @Schema(description = "Maximum credit limit for the account", 
            example = "5000.00", 
            required = true)
    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.00", message = "Credit limit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Credit limit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCreditLimit;
    
    @Schema(description = "Maximum cash credit limit for the account", 
            example = "1000.00", 
            required = true)
    @NotNull(message = "Cash credit limit is required")
    @DecimalMin(value = "0.00", message = "Cash credit limit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Cash credit limit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCashCreditLimit;
    
    @Schema(description = "Date when the account was opened", 
            example = "2024-01-15", 
            required = true)
    @NotNull(message = "Account open date is required")
    @PastOrPresent(message = "Account open date cannot be in the future")
    private LocalDate acctOpenDate;
    
    @Schema(description = "Date when the account expires", 
            example = "2027-01-15", 
            required = true)
    @NotNull(message = "Account expiration date is required")
    @Future(message = "Account expiration date must be in the future")
    private LocalDate acctExpirationDate;
    
    @Schema(description = "Date when the account was reissued (optional)", 
            example = "2025-06-01", 
            required = false)
    private LocalDate acctReissueDate;
    
    @Schema(description = "Current cycle credit amount", 
            example = "250.00", 
            required = true)
    @NotNull(message = "Current cycle credit is required")
    @DecimalMin(value = "0.00", message = "Current cycle credit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Current cycle credit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCurrCycCredit;
    
    @Schema(description = "Current cycle debit amount", 
            example = "150.00", 
            required = true)
    @NotNull(message = "Current cycle debit is required")
    @DecimalMin(value = "0.00", message = "Current cycle debit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Current cycle debit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCurrCycDebit;
    
    @Schema(description = "Identifier for the account group (optional)", 
            example = "GRP001", 
            required = false)
    @Size(max = 50, message = "Account group ID cannot exceed 50 characters")
    private String acctGroupId;
}
