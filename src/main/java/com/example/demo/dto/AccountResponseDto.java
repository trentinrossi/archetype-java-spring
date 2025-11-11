package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    private Long accountId;
    private BigDecimal currentBalance;
    private BigDecimal creditLimit;
    private BigDecimal availableCredit;
    private Long customerId;
    private String customerName;
    private Boolean isOverLimit;
    private BigDecimal totalTransactionAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
