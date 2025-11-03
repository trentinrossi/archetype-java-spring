package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    
    private String transactionId;
    private String cardNumber;
    private String accountId;
    private String transactionTypeCode;
    private String transactionCategoryCode;
    private String transactionSource;
    private String transactionDescription;
    private BigDecimal transactionAmount;
    private Long merchantId;
    private String merchantName;
    private String merchantCity;
    private String merchantZip;
    private LocalDateTime originalTimestamp;
    private LocalDateTime processingTimestamp;
}
