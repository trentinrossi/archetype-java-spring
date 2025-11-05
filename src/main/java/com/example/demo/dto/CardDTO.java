package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private String cardNumber;
    private String accountId;
    private String embossedName;
    private LocalDate expirationDate;
    private String activeStatus;
    private boolean active;
    private boolean expired;
}
