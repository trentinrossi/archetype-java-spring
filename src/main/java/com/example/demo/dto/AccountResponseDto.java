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

    private String accountId;
    private String activeStatus;
    private String activeStatusDisplay;
    private BigDecimal currentBalance;
    private BigDecimal creditLimit;
    private BigDecimal cashCreditLimit;
    private String openDate;
    private String expirationDate;
    private String reissueDate;
    private BigDecimal currentCycleCredit;
    private BigDecimal currentCycleDebit;
    private String groupId;
    private String accountData;
    private BigDecimal availableCredit;
    private BigDecimal availableCashCredit;
    private BigDecimal creditUtilizationPercentage;
    private BigDecimal netCycleAmount;
    private Boolean isExpired;
    private Long daysUntilExpiration;
    private Boolean hasOutstandingBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
