package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.enums.StatementStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementResponseDto {

    private Long id;
    private String customerName;
    private String customerAddress;
    private Long accountId;
    private BigDecimal currentBalance;
    private Integer ficoScore;
    private BigDecimal totalTransactionAmount;
    private LocalDateTime statementPeriodStart;
    private LocalDateTime statementPeriodEnd;
    private String plainTextContent;
    private String htmlContent;
    private StatementStatus status;
    private String statusDisplayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
