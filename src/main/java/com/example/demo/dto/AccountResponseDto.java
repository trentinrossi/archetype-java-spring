package com.example.demo.dto;

import com.example.demo.enums.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for account data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    
    @Schema(description = "Account ID", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Customer ID", example = "123456789")
    private String customerId;
    
    @Schema(description = "Account number", example = "4111111111111111")
    private String accountNumber;
    
    @Schema(description = "Account type", example = "CREDIT_CARD")
    private String accountType;
    
    @Schema(description = "Current balance", example = "1234.56")
    private BigDecimal currentBalance;
    
    @Schema(description = "Available balance", example = "3765.44")
    private BigDecimal availableBalance;
    
    @Schema(description = "Credit limit", example = "5000.00")
    private BigDecimal creditLimit;
    
    @Schema(description = "Available credit", example = "3765.44")
    private BigDecimal availableCredit;
    
    @Schema(description = "Cash advance limit", example = "1000.00")
    private BigDecimal cashAdvanceLimit;
    
    @Schema(description = "Minimum payment due", example = "25.00")
    private BigDecimal minimumPaymentDue;
    
    @Schema(description = "Payment due date", example = "2024-02-15")
    private LocalDate paymentDueDate;
    
    @Schema(description = "Last payment amount", example = "100.00")
    private BigDecimal lastPaymentAmount;
    
    @Schema(description = "Last payment date", example = "2024-01-15")
    private LocalDate lastPaymentDate;
    
    @Schema(description = "Last statement balance", example = "1150.00")
    private BigDecimal lastStatementBalance;
    
    @Schema(description = "Last statement date", example = "2024-01-01")
    private LocalDate lastStatementDate;
    
    @Schema(description = "Next statement date", example = "2024-02-01")
    private LocalDate nextStatementDate;
    
    @Schema(description = "Account open date", example = "2023-01-15")
    private LocalDate accountOpenDate;
    
    @Schema(description = "Account close date", example = "null")
    private LocalDate accountCloseDate;
    
    @Schema(description = "Interest rate", example = "18.99")
    private BigDecimal interestRate;
    
    @Schema(description = "Annual fee", example = "95.00")
    private BigDecimal annualFee;
    
    @Schema(description = "Late fee", example = "35.00")
    private BigDecimal lateFee;
    
    @Schema(description = "Overlimit fee", example = "25.00")
    private BigDecimal overlimitFee;
    
    @Schema(description = "Days delinquent", example = "0")
    private Integer daysDelinquent;
    
    @Schema(description = "Cycle to date purchases", example = "234.56")
    private BigDecimal cycleToDatePurchases;
    
    @Schema(description = "Cycle to date cash advances", example = "0.00")
    private BigDecimal cycleToDateCashAdvances;
    
    @Schema(description = "Cycle to date payments", example = "100.00")
    private BigDecimal cycleToDatePayments;
    
    @Schema(description = "Year to date purchases", example = "5234.56")
    private BigDecimal yearToDatePurchases;
    
    @Schema(description = "Year to date cash advances", example = "500.00")
    private BigDecimal yearToDateCashAdvances;
    
    @Schema(description = "Year to date interest", example = "234.50")
    private BigDecimal yearToDateInterest;
    
    @Schema(description = "Year to date fees", example = "95.00")
    private BigDecimal yearToDateFees;
    
    @Schema(description = "Account status", example = "ACTIVE")
    private AccountStatus status;
    
    @Schema(description = "Account status display name", example = "Active")
    private String statusDisplayName;
    
    @Schema(description = "Reward points", example = "5234")
    private Integer rewardPoints;
    
    @Schema(description = "Reward points balance", example = "5234")
    private Integer rewardPointsBalance;
    
    @Schema(description = "Autopay enabled", example = "false")
    private Boolean autopayEnabled;
    
    @Schema(description = "Paperless statements", example = "true")
    private Boolean paperlessStatements;
    
    @Schema(description = "Fraud alert", example = "false")
    private Boolean fraudAlert;
    
    @Schema(description = "Temporary hold", example = "false")
    private Boolean temporaryHold;
    
    @Schema(description = "Credit utilization percentage", example = "24.69")
    private BigDecimal creditUtilization;
    
    @Schema(description = "Is over limit", example = "false")
    private Boolean isOverLimit;
    
    @Schema(description = "Is delinquent", example = "false")
    private Boolean isDelinquent;
    
    @Schema(description = "Is past due", example = "false")
    private Boolean isPastDue;
    
    @Schema(description = "Can transact", example = "true")
    private Boolean canTransact;
    
    @Schema(description = "Created timestamp", example = "2023-01-15T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last updated timestamp", example = "2024-01-20T15:30:00")
    private LocalDateTime updatedAt;
}
