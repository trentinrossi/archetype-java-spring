package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {

    @Schema(description = "Customer Identification Number - Primary key", example = "123456789")
    private Long customerId;

    @Schema(description = "Customer First Name", example = "John")
    private String firstName;

    @Schema(description = "Customer Middle Name", example = "Michael")
    private String middleName;

    @Schema(description = "Customer Last Name", example = "Doe")
    private String lastName;

    @Schema(description = "Customer Full Name", example = "John Michael Doe")
    private String fullName;

    @Schema(description = "Customer Address Line 1", example = "123 Main Street")
    private String addressLine1;

    @Schema(description = "Customer Address Line 2", example = "Apt 4B")
    private String addressLine2;

    @Schema(description = "Customer Address Line 3 (City)", example = "New York")
    private String addressLine3;

    @Schema(description = "Customer State Code - Two-letter US state code", example = "NY")
    private String stateCode;

    @Schema(description = "Customer Country Code - Three-letter country code", example = "USA")
    private String countryCode;

    @Schema(description = "Customer ZIP Code - US ZIP code", example = "10001")
    private String zipCode;

    @Schema(description = "Customer Primary Phone Number in format (XXX)XXX-XXXX", example = "(212)555-1234")
    private String phoneNumber1;

    @Schema(description = "Customer Secondary Phone Number in format (XXX)XXX-XXXX", example = "(212)555-5678")
    private String phoneNumber2;

    @Schema(description = "Customer Social Security Number in XXX-XX-XXXX format", example = "123-45-6789")
    private String ssn;

    @Schema(description = "Customer Social Security Number stored without hyphens", example = "123456789")
    private Long ssnRaw;

    @Schema(description = "Customer Government Issued ID", example = "DL123456789")
    private String governmentIssuedId;

    @Schema(description = "Customer Date of Birth in YYYY-MM-DD format", example = "1985-06-15")
    private LocalDate dateOfBirth;

    @Schema(description = "Customer Age calculated from date of birth", example = "38")
    private Integer age;

    @Schema(description = "Customer EFT Account ID - Electronic Funds Transfer account", example = "1234567890")
    private String eftAccountId;

    @Schema(description = "Primary Cardholder Indicator - Y for primary, N for secondary", example = "Y")
    private String primaryCardholderIndicator;

    @Schema(description = "Primary Cardholder Status Display", example = "Primary Cardholder")
    private String primaryCardholderStatus;

    @Schema(description = "Customer FICO Credit Score - Range: 300-850", example = "720")
    private Integer ficoScore;

    @Schema(description = "Customer's FICO credit score", example = "720")
    private Integer ficoCreditScore;

    @Schema(description = "City of customer's address", example = "New York")
    private String city;

    @Schema(description = "Customer's primary phone number", example = "(212)555-1234")
    private String primaryPhoneNumber;

    @Schema(description = "Customer's secondary phone number", example = "(212)555-5678")
    private String secondaryPhoneNumber;

    @Schema(description = "Complete formatted address", example = "123 Main Street, Apt 4B, New York, NY 10001, USA")
    private String fullAddress;

    @Schema(description = "Indicates if customer meets minimum age requirement", example = "true")
    private Boolean meetsAgeRequirement;

    @Schema(description = "FICO Score Rating", example = "Good")
    private String ficoScoreRating;

    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp")
    private LocalDateTime updatedAt;
}
