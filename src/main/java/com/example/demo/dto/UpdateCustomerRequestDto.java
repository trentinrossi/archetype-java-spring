package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequestDto {

    @Schema(description = "Customer First Name", example = "John", required = false)
    private String firstName;

    @Schema(description = "Customer Middle Name", example = "Michael", required = false)
    private String middleName;

    @Schema(description = "Customer Last Name", example = "Doe", required = false)
    private String lastName;

    @Schema(description = "Customer Address Line 1", example = "123 Main Street", required = false)
    private String addressLine1;

    @Schema(description = "Customer Address Line 2", example = "Apt 4B", required = false)
    private String addressLine2;

    @Schema(description = "Customer Address Line 3 (City)", example = "New York", required = false)
    private String addressLine3;

    @Schema(description = "Customer State Code - Two-letter US state code", example = "NY", required = false)
    private String stateCode;

    @Schema(description = "Customer Country Code - Three-letter country code", example = "USA", required = false)
    private String countryCode;

    @Schema(description = "Customer ZIP Code - US ZIP code", example = "10001", required = false)
    private String zipCode;

    @Schema(description = "Customer Primary Phone Number in format (XXX)XXX-XXXX", example = "(212)555-1234", required = false)
    private String phoneNumber1;

    @Schema(description = "Customer Secondary Phone Number in format (XXX)XXX-XXXX", example = "(212)555-5678", required = false)
    private String phoneNumber2;

    @Schema(description = "Customer Social Security Number stored without hyphens", example = "123456789", required = false)
    private Long ssn;

    @Schema(description = "Customer Government Issued ID", example = "DL123456789", required = false)
    private String governmentIssuedId;

    @Schema(description = "Customer Date of Birth in YYYY-MM-DD format", example = "1985-06-15", required = false)
    private LocalDate dateOfBirth;

    @Schema(description = "Customer EFT Account ID - Electronic Funds Transfer account", example = "1234567890", required = false)
    private String eftAccountId;

    @Schema(description = "Primary Cardholder Indicator - Y for primary, N for secondary", example = "Y", required = false)
    private String primaryCardholderIndicator;

    @Schema(description = "Customer FICO Credit Score - Range: 300-850", example = "720", required = false)
    private Integer ficoScore;

    @Schema(description = "Customer's FICO credit score", example = "720", required = false)
    private Integer ficoCreditScore;

    @Schema(description = "City of customer's address", example = "New York", required = false)
    private String city;

    @Schema(description = "Customer's primary phone number", example = "(212)555-1234", required = false)
    private String primaryPhoneNumber;

    @Schema(description = "Customer's secondary phone number", example = "(212)555-5678", required = false)
    private String secondaryPhoneNumber;
}
