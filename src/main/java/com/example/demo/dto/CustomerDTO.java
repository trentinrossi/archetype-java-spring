package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String customerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateCode;
    private String zipCode;
    private String countryCode;
    private String phoneNumber1;
    private String phoneNumber2;
    private String governmentIssuedId;
    private LocalDate dateOfBirth;
    private String eftAccountId;
    private String primaryCardHolderIndicator;
    private Integer ficoCreditScore;
    private boolean primaryCardHolder;
}
