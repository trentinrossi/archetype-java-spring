package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

    private String cardNumber;
    private Long customerId;
    private String customerName;
    private Long accountId;
    private Integer transactionCount;
    private Boolean canAddTransaction;
    private Boolean hasValidLinkage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
