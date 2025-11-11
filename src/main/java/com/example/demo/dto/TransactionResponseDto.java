package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    private Long id;
    private String cardNumber;
    private String transactionId;
    private String transactionTypeCode;
    private String transactionCategoryCode;
    private String transactionSource;
    private String transactionDescription;
    private BigDecimal transactionAmount;
    private String formattedAmount;
    private Boolean isDebit;
    private Boolean isCredit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
