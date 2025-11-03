package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestCalculationDTO {
    
    private String accountId;
    private BigDecimal balance;
    private BigDecimal interestRate;
    private BigDecimal monthlyInterest;
    private String transactionTypeCode;
    private String transactionCategoryCode;
}
