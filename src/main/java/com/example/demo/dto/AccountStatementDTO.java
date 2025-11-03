package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementDTO {
    
    private CustomerDTO customer;
    private AccountDTO account;
    private List<TransactionDTO> transactions;
    private BigDecimal totalAmount;
}
