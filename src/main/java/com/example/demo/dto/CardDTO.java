package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    
    private String cardNumber;
    private String accountId;
    private String cvvCode;
    private String embossedName;
    private String expirationDate;
    private String activeStatus;
}
