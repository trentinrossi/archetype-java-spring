package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDto {

    @NotBlank(message = "Invalid customer ID")
    @Pattern(regexp = "^\\d{9}$", message = "Invalid customer ID")
    private String customerId;

    @NotBlank(message = "Invalid Social Security Number")
    @Pattern(regexp = "^\\d{9}$", message = "Invalid Social Security Number")
    private String ssn;

    @NotBlank(message = "First Name can NOT be empty...")
    @Size(max = 25, message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name is required and must contain only letters")
    private String firstName;

    @Size(max = 25)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Middle name must contain only letters")
    private String middleName;

    @NotBlank(message = "Last Name can NOT be empty...")
    @Size(max = 25, message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name is required and must contain only letters")
    private String lastName;

    @NotBlank(message = "Street address is required")
    @Size(max = 50)
    private String addressLine1;

    @Size(max = 50)
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 50)
    private String city;

    @NotBlank(message = "Invalid state code")
    @Size(min = 2, max = 2)
    private String stateCode;

    @NotBlank(message = "Invalid country code")
    @Size(min = 3, max = 3)
    private String countryCode;

    @NotBlank(message = "Invalid ZIP code")
    @Size(max = 10)
    private String zipCode;

    @NotBlank(message = "Invalid primary phone number format")
    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{4}$", message = "Invalid primary phone number format")
    private String phoneNumber1;

    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{4}$", message = "Invalid secondary phone number format")
    private String phoneNumber2;

    @NotBlank(message = "Invalid date of birth or customer is under 18 years old")
    @Pattern(regexp = "^\\d{8}$", message = "Invalid date of birth or customer is under 18 years old")
    private String dateOfBirth;

    @Size(max = 20)
    private String governmentId;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid EFT account ID format")
    private String eftAccountId;

    @NotBlank(message = "Primary holder indicator must be Y or N")
    @Pattern(regexp = "^[YN]$", message = "Primary holder indicator must be Y or N")
    private String primaryCardHolderIndicator;

    @NotNull(message = "FICO score must be between 300 and 850")
    @Min(value = 300, message = "FICO score must be between 300 and 850")
    @Max(value = 850, message = "FICO score must be between 300 and 850")
    private Integer ficoCreditScore;
}
