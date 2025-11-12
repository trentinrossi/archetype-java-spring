package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {

    private String customerId;
    private String ssn;
    private String formattedSsn;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateCode;
    private String countryCode;
    private String zipCode;
    private String phoneNumber1;
    private String phoneNumber2;
    private String dateOfBirth;
    private Integer age;
    private String governmentId;
    private String eftAccountId;
    private String primaryCardHolderIndicator;
    private Boolean isPrimaryCardHolder;
    private Integer ficoCreditScore;
    private String customerData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
