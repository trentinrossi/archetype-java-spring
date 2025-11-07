package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for updating an existing account.
 * All fields are optional to allow partial updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {
    
    @Schema(description = "Account active status ('A' for active, 'I' for inactive)", 
            example = "A", 
            required = false)
    @Pattern(regexp = "^[AI]$", message = "Account active status must be 'A' or 'I'")
    private String acctActiveStatus;
    
    @Schema(description = "Current balance of the account", 
            example = "1500.50", 
            required = false)
    @DecimalMin(value = "0.00", message = "Current balance cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Current balance must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCurrBal;
    
    @Schema(description = "Maximum credit limit for the account", 
            example = "5000.00", 
            required = false)
    @DecimalMin(value = "0.00", message = "Credit limit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Credit limit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCreditLimit;
    
    @Schema(description = "Maximum cash credit limit for the account", 
            example = "1000.00", 
            required = false)
    @DecimalMin(value = "0.00", message = "Cash credit limit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Cash credit limit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCashCreditLimit;
    
    @Schema(description = "Date when the account was opened", 
            example = "2024-01-15", 
            required = false)
    @PastOrPresent(message = "Account open date cannot be in the future")
    private LocalDate acctOpenDate;
    
    @Schema(description = "Date when the account expires", 
            example = "2027-01-15", 
            required = false)
    @Future(message = "Account expiration date must be in the future")
    private LocalDate acctExpirationDate;
    
    @Schema(description = "Date when the account was reissued (optional)", 
            example = "2025-06-01", 
            required = false)
    private LocalDate acctReissueDate;
    
    @Schema(description = "Current cycle credit amount", 
            example = "250.00", 
            required = false)
    @DecimalMin(value = "0.00", message = "Current cycle credit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Current cycle credit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCurrCycCredit;
    
    @Schema(description = "Current cycle debit amount", 
            example = "150.00", 
            required = false)
    @DecimalMin(value = "0.00", message = "Current cycle debit cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Current cycle debit must have at most 13 integer digits and 2 decimal places")
    private BigDecimal acctCurrCycDebit;
    
    @Schema(description = "Identifier for the account group (optional)", 
            example = "GRP001", 
            required = false)
    @Size(max = 50, message = "Account group ID cannot exceed 50 characters")
    private String acctGroupId;
}
