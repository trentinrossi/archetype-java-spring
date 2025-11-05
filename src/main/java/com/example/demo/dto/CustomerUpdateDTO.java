package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateDTO {
    
    @NotBlank(message = "Customer ID is required")
    @Pattern(regexp = "^\\d{9}$", message = "Customer ID must be 9 digits")
    private String customerId;
    
    @NotBlank(message = "First name is required")
    @Size(max = 25, message = "First name must not exceed 25 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "First name can only contain alphabets and spaces")
    private String firstName;
    
    @Size(max = 25, message = "Middle name must not exceed 25 characters")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Middle name can only contain alphabets and spaces")
    private String middleName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 25, message = "Last name must not exceed 25 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name can only contain alphabets and spaces")
    private String lastName;
    
    @NotBlank(message = "Address line 1 is required")
    @Size(max = 50, message = "Address line 1 must not exceed 50 characters")
    private String addressLine1;
    
    @Size(max = 50, message = "Address line 2 must not exceed 50 characters")
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "City can only contain alphabets and spaces")
    private String city;
    
    @NotBlank(message = "State code is required")
    @Size(min = 2, max = 2, message = "State code must be exactly 2 characters")
    private String stateCode;
    
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^\\d{5}$", message = "ZIP code must be 5 digits")
    private String zipCode;
    
    @NotBlank(message = "Country code is required")
    @Size(min = 3, max = 3, message = "Country code must be exactly 3 characters")
    private String countryCode;
    
    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{4}$", message = "Phone number must be in format (XXX)XXX-XXXX")
    private String phoneNumber1;
    
    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{4}$", message = "Phone number must be in format (XXX)XXX-XXXX")
    private String phoneNumber2;
    
    @NotBlank(message = "SSN is required")
    @Pattern(regexp = "^\\d{9}$", message = "SSN must be 9 digits")
    private String ssn;
    
    @Size(max = 20, message = "Government issued ID must not exceed 20 characters")
    private String governmentIssuedId;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "EFT account ID is required")
    @Pattern(regexp = "^\\d{10}$", message = "EFT account ID must be 10 digits")
    private String eftAccountId;
    
    @Pattern(regexp = "^[YN]$", message = "Primary card holder indicator must be Y or N")
    private String primaryCardHolderIndicator;
    
    @Min(value = 300, message = "FICO score must be at least 300")
    @Max(value = 850, message = "FICO score must not exceed 850")
    private Integer ficoCreditScore;
}
