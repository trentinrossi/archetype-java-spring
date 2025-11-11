package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDto {

    @NotNull(message = "Customer ID must be 9 digits numeric and exist in customer file")
    @Min(value = 100000000L, message = "Customer ID must be 9 digits numeric")
    @Max(value = 999999999L, message = "Customer ID must be 9 digits numeric")
    private Long customerId;

    @NotBlank(message = "First name must not be empty, maximum 25 characters")
    @Size(max = 25, message = "First name must not be empty, maximum 25 characters")
    private String firstName;

    @Size(max = 25, message = "Middle name maximum 25 characters")
    private String middleName;

    @NotBlank(message = "Last name must not be empty, maximum 25 characters")
    @Size(max = 25, message = "Last name must not be empty, maximum 25 characters")
    private String lastName;

    @NotBlank(message = "Address line 1 must not be empty, maximum 50 characters")
    @Size(max = 50, message = "Address line 1 must not be empty, maximum 50 characters")
    private String addressLine1;

    @Size(max = 50, message = "Address line 2 maximum 50 characters")
    private String addressLine2;

    @Size(max = 50, message = "Address line 3 maximum 50 characters")
    private String addressLine3;

    @NotBlank(message = "State code must be 2 characters")
    @Size(min = 2, max = 2, message = "State code must be 2 characters")
    private String stateCode;

    @NotBlank(message = "Country code must be 3 characters")
    @Size(min = 3, max = 3, message = "Country code must be 3 characters")
    private String countryCode;

    @NotBlank(message = "ZIP code must be 10 characters alphanumeric")
    @Size(min = 10, max = 10, message = "ZIP code must be 10 characters alphanumeric")
    @Pattern(regexp = "^[a-zA-Z0-9]{10}$", message = "ZIP code must be 10 characters alphanumeric")
    private String zipCode;

    @NotNull(message = "FICO credit score must be 3 digits numeric")
    @Min(value = 0, message = "FICO credit score must be between 0 and 999")
    @Max(value = 999, message = "FICO credit score must be between 0 and 999")
    private Integer ficoCreditScore;
}
