package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardListDTO {
    private String cardNumber;
    private String accountId;
    private String activeStatus;
}
