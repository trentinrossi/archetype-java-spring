package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    
    @Schema(description = "Account ID", example = "12345678901")
    private Long accountId;
    
    @Schema(description = "Current balance of the account", example = "1000.00")
    private BigDecimal currentBalance;
    
    @Schema(description = "Credit limit of the account", example = "5000.00")
    private BigDecimal creditLimit;
    
    @Schema(description = "Current cycle credit", example = "500.00")
    private BigDecimal currentCycleCredit;
    
    @Schema(description = "Current cycle debit", example = "200.00")
    private BigDecimal currentCycleDebit;
    
    @Schema(description = "Expiration date of the account", example = "2025-12-31")
    private LocalDate expirationDate;
    
    @Schema(description = "Timestamp when the account was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
