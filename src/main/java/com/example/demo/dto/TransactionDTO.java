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
    private String typeCode;
    private Integer categoryCode;
    private String source;
    private String description;
    private BigDecimal amount;
    private String merchantId;
    private String merchantName;
    private String merchantCity;
    private String merchantZip;
    private LocalDateTime originationTimestamp;
    private LocalDateTime processingTimestamp;
}
