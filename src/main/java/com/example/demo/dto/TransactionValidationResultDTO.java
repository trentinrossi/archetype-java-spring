package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionValidationResultDTO {
    
    private boolean valid;
    private String transactionId;
    private Integer validationFailureCode;
    private String validationFailureReason;
    private String cardNumber;
    private String accountId;
    private String customerId;
}
