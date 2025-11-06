package com.example.demo.dto;

import com.example.demo.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for updating an existing account
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {
    
    @Schema(description = "Credit limit", example = "10000.00", required = false)
    private BigDecimal creditLimit;
    
    @Schema(description = "Cash advance limit", example = "2000.00", required = false)
    private BigDecimal cashAdvanceLimit;
    
    @Schema(description = "Interest rate", example = "15.99", required = false)
    private BigDecimal interestRate;
    
    @Schema(description = "Annual fee", example = "0.00", required = false)
    private BigDecimal annualFee;
    
    @Schema(description = "Late fee", example = "35.00", required = false)
    private BigDecimal lateFee;
    
    @Schema(description = "Overlimit fee", example = "25.00", required = false)
    private BigDecimal overlimitFee;
    
    @Schema(description = "Account status", example = "ACTIVE", required = false)
    private AccountStatus status;
    
    @Schema(description = "Autopay enabled", example = "true", required = false)
    private Boolean autopayEnabled;
    
    @Schema(description = "Paperless statements", example = "true", required = false)
    private Boolean paperlessStatements;
    
    @Schema(description = "Fraud alert", example = "false", required = false)
    private Boolean fraudAlert;
    
    @Schema(description = "Temporary hold", example = "false", required = false)
    private Boolean temporaryHold;
    
    @Schema(description = "Payment due date", example = "2024-02-15", required = false)
    private LocalDate paymentDueDate;
}
