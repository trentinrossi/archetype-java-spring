package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequestDto {

    @Size(max = 25)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters")
    private String firstName;

    @Size(max = 25)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Middle name must contain only letters")
    private String middleName;

    @Size(max = 25)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters")
    private String lastName;

    @Size(max = 50)
    private String addressLine1;

    @Size(max = 50)
    private String addressLine2;

    @Size(max = 50)
    private String city;

    @Size(min = 2, max = 2)
    private String stateCode;

    @Size(min = 3, max = 3)
    private String countryCode;

    @Size(max = 10)
    private String zipCode;

    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{4}$", message = "Invalid phone number format")
    private String phoneNumber1;

    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{4}$", message = "Invalid phone number format")
    private String phoneNumber2;

    @Size(max = 20)
    private String governmentId;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid EFT account ID format")
    private String eftAccountId;

    @Pattern(regexp = "^[YN]$", message = "Primary holder indicator must be Y or N")
    private String primaryCardHolderIndicator;

    @Min(value = 300, message = "FICO score must be between 300 and 850")
    @Max(value = 850, message = "FICO score must be between 300 and 850")
    private Integer ficoCreditScore;
}
