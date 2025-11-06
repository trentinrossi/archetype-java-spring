package com.example.demo.dto;

import com.example.demo.enums.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for updating an existing transaction
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {
    
    @Schema(description = "Transaction status", example = "POSTED", required = false)
    private TransactionStatus transactionStatus;
    
    @Schema(description = "Post date", example = "2024-01-21", required = false)
    private LocalDate postDate;
    
    @Schema(description = "Description", example = "Updated description", required = false)
    private String description;
    
    @Schema(description = "Is disputed", example = "false", required = false)
    private Boolean isDisputed;
    
    @Schema(description = "Dispute date", example = "2024-01-25", required = false)
    private LocalDate disputeDate;
    
    @Schema(description = "Dispute reason", example = "Unauthorized charge", required = false)
    private String disputeReason;
    
    @Schema(description = "Is reversed", example = "false", required = false)
    private Boolean isReversed;
    
    @Schema(description = "Reversal date", example = "2024-01-26", required = false)
    private LocalDate reversalDate;
    
    @Schema(description = "Reversal reason", example = "Customer request", required = false)
    private String reversalReason;
    
    @Schema(description = "Reward points earned", example = "125", required = false)
    private Integer rewardPointsEarned;
    
    @Schema(description = "Cashback amount", example = "1.25", required = false)
    private BigDecimal cashbackAmount;
    
    @Schema(description = "Fee amount", example = "0.00", required = false)
    private BigDecimal feeAmount;
    
    @Schema(description = "Interest amount", example = "0.00", required = false)
    private BigDecimal interestAmount;
    
    @Schema(description = "Statement date", example = "2024-02-01", required = false)
    private LocalDate statementDate;
    
    @Schema(description = "Billing cycle", example = "202401", required = false)
    private String billingCycle;
}
