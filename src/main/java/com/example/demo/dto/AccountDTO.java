package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String accountId;
    private String activeStatus;
    private BigDecimal currentBalance;
    private BigDecimal creditLimit;
    private BigDecimal cashCreditLimit;
    private LocalDate openDate;
    private LocalDate expirationDate;
    private LocalDate reissueDate;
    private BigDecimal currentCycleCredit;
    private BigDecimal currentCycleDebit;
    private String groupId;
    private BigDecimal availableCredit;
    private boolean active;
    private boolean expired;
}
