package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    
    private String transactionId;
    private String transactionDate;
    private String transactionDescription;
    private BigDecimal transactionAmount;
    private String userId;
    private String userFullName;
    private String createdAt;
}
