package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReferenceDTO {
    
    private String cardNumber;
    private String customerId;
    private String accountId;
}
