package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    
    @NotBlank(message = "Customer ID is required")
    @Size(max = 9, message = "Customer ID must not exceed 9 characters")
    @Schema(description = "Customer identifier", example = "CUST12345", required = true)
    private String customerId;
    
    @NotBlank(message = "Customer data is required")
    @Size(max = 491, message = "Customer data must not exceed 491 characters")
    @Schema(description = "Customer data", example = "Customer data content...", required = true)
    private String customerData;
    
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Schema(description = "Customer first name", example = "John", required = false)
    private String firstName;
    
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Schema(description = "Customer last name", example = "Doe", required = false)
    private String lastName;
    
    @Size(max = 1, message = "Middle initial must be 1 character")
    @Schema(description = "Customer middle initial", example = "M", required = false)
    private String middleInitial;
    
    @Size(max = 100, message = "Address line 1 must not exceed 100 characters")
    @Schema(description = "Customer address line 1", example = "123 Main Street", required = false)
    private String addressLine1;
    
    @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
    @Schema(description = "Customer address line 2", example = "Apt 4B", required = false)
    private String addressLine2;
    
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Schema(description = "Customer city", example = "New York", required = false)
    private String city;
    
    @Size(max = 2, message = "State must not exceed 2 characters")
    @Schema(description = "Customer state", example = "NY", required = false)
    private String state;
    
    @Size(max = 10, message = "Zip code must not exceed 10 characters")
    @Schema(description = "Customer zip code", example = "10001", required = false)
    private String zipCode;
    
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    @Schema(description = "Customer phone number", example = "+1234567890", required = false)
    private String phoneNumber;
    
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Customer email address", example = "john.doe@example.com", required = false)
    private String emailAddress;
    
    @NotBlank(message = "Customer status is required")
    @Size(max = 1, message = "Customer status must be 1 character")
    @Pattern(regexp = "[AINSC]", message = "Customer status must be A, I, N, S, or C")
    @Schema(description = "Customer status", example = "A", required = true)
    private String customerStatus;
    
    @Size(max = 2, message = "Customer type must not exceed 2 characters")
    @Schema(description = "Customer type code", example = "P", required = false)
    private String customerType;
    
    @Size(max = 3, message = "Credit rating must not exceed 3 characters")
    @Schema(description = "Customer credit rating", example = "AAA", required = false)
    private String creditRating;
    
    @Size(max = 8, message = "Date of birth must not exceed 8 characters")
    @Schema(description = "Customer date of birth", example = "19850615", required = false)
    private String dateOfBirth;
    
    @Size(max = 11, message = "Social security number must not exceed 11 characters")
    @Schema(description = "Customer social security number", example = "123-45-6789", required = false)
    private String socialSecurityNumber;
    
    @Size(max = 50, message = "Employer name must not exceed 50 characters")
    @Schema(description = "Customer employer name", example = "ABC Corporation", required = false)
    private String employerName;
    
    @Size(max = 15, message = "Annual income must not exceed 15 characters")
    @Schema(description = "Customer annual income", example = "75000", required = false)
    private String annualIncome;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateCustomerRequest {
    
    @Size(max = 491, message = "Customer data must not exceed 491 characters")
    @Schema(description = "Customer data", example = "Updated customer data content...", required = false)
    private String customerData;
    
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Schema(description = "Customer first name", example = "John", required = false)
    private String firstName;
    
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Schema(description = "Customer last name", example = "Doe", required = false)
    private String lastName;
    
    @Size(max = 1, message = "Middle initial must be 1 character")
    @Schema(description = "Customer middle initial", example = "M", required = false)
    private String middleInitial;
    
    @Size(max = 100, message = "Address line 1 must not exceed 100 characters")
    @Schema(description = "Customer address line 1", example = "123 Main Street", required = false)
    private String addressLine1;
    
    @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
    @Schema(description = "Customer address line 2", example = "Apt 4B", required = false)
    private String addressLine2;
    
    @Size(max = 50, message = "City must not exceed 50 characters")
    @Schema(description = "Customer city", example = "New York", required = false)
    private String city;
    
    @Size(max = 2, message = "State must not exceed 2 characters")
    @Schema(description = "Customer state", example = "NY", required = false)
    private String state;
    
    @Size(max = 10, message = "Zip code must not exceed 10 characters")
    @Schema(description = "Customer zip code", example = "10001", required = false)
    private String zipCode;
    
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    @Schema(description = "Customer phone number", example = "+1234567890", required = false)
    private String phoneNumber;
    
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Customer email address", example = "john.doe@example.com", required = false)
    private String emailAddress;
    
    @Size(max = 1, message = "Customer status must be 1 character")
    @Pattern(regexp = "[AINSC]", message = "Customer status must be A, I, N, S, or C")
    @Schema(description = "Customer status", example = "A", required = false)
    private String customerStatus;
    
    @Size(max = 2, message = "Customer type must not exceed 2 characters")
    @Schema(description = "Customer type code", example = "P", required = false)
    private String customerType;
    
    @Size(max = 3, message = "Credit rating must not exceed 3 characters")
    @Schema(description = "Customer credit rating", example = "AAA", required = false)
    private String creditRating;
    
    @Size(max = 50, message = "Employer name must not exceed 50 characters")
    @Schema(description = "Customer employer name", example = "ABC Corporation", required = false)
    private String employerName;
    
    @Size(max = 15, message = "Annual income must not exceed 15 characters")
    @Schema(description = "Customer annual income", example = "75000", required = false)
    private String annualIncome;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CustomerResponse {
    
    @Schema(description = "Customer identifier", example = "CUST12345", required = true)
    private String customerId;
    
    @Schema(description = "Customer data", example = "Customer data content...", required = true)
    private String customerData;
    
    @Schema(description = "Customer first name", example = "John", required = false)
    private String firstName;
    
    @Schema(description = "Customer last name", example = "Doe", required = false)
    private String lastName;
    
    @Schema(description = "Customer middle initial", example = "M", required = false)
    private String middleInitial;
    
    @Schema(description = "Customer full name", example = "John M Doe", required = true)
    private String fullName;
    
    @Schema(description = "Customer address line 1", example = "123 Main Street", required = false)
    private String addressLine1;
    
    @Schema(description = "Customer address line 2", example = "Apt 4B", required = false)
    private String addressLine2;
    
    @Schema(description = "Customer city", example = "New York", required = false)
    private String city;
    
    @Schema(description = "Customer state", example = "NY", required = false)
    private String state;
    
    @Schema(description = "Customer zip code", example = "10001", required = false)
    private String zipCode;
    
    @Schema(description = "Customer full address", example = "123 Main Street, Apt 4B, New York, NY 10001", required = false)
    private String fullAddress;
    
    @Schema(description = "Customer phone number", example = "+1234567890", required = false)
    private String phoneNumber;
    
    @Schema(description = "Customer email address", example = "john.doe@example.com", required = false)
    private String emailAddress;
    
    @Schema(description = "Customer status", example = "A", required = true)
    private String customerStatus;
    
    @Schema(description = "Customer status description", example = "Active", required = true)
    private String customerStatusDescription;
    
    @Schema(description = "Customer type code", example = "P", required = false)
    private String customerType;
    
    @Schema(description = "Customer credit rating", example = "AAA", required = false)
    private String creditRating;
    
    @Schema(description = "Customer date of birth", example = "19850615", required = false)
    private String dateOfBirth;
    
    @Schema(description = "Masked social security number", example = "***-**-6789", required = false)
    private String maskedSSN;
    
    @Schema(description = "Customer employer name", example = "ABC Corporation", required = false)
    private String employerName;
    
    @Schema(description = "Customer annual income", example = "75000", required = false)
    private String annualIncome;
    
    @Schema(description = "Customer is active", example = "true", required = true)
    private Boolean isActive;
    
    @Schema(description = "Customer is inactive", example = "false", required = true)
    private Boolean isInactive;
    
    @Schema(description = "Customer is suspended", example = "false", required = true)
    private Boolean isSuspended;
    
    @Schema(description = "Customer is closed", example = "false", required = true)
    private Boolean isClosed;
    
    @Schema(description = "Customer has valid email", example = "true", required = true)
    private Boolean hasValidEmail;
    
    @Schema(description = "Customer has valid phone", example = "true", required = true)
    private Boolean hasValidPhone;
    
    @Schema(description = "Customer is personal customer", example = "true", required = true)
    private Boolean isPersonalCustomer;
    
    @Schema(description = "Customer is business customer", example = "false", required = true)
    private Boolean isBusinessCustomer;
    
    @Schema(description = "Customer has good credit rating", example = "true", required = true)
    private Boolean hasGoodCreditRating;
    
    @Schema(description = "Customer has poor credit rating", example = "false", required = true)
    private Boolean hasPoorCreditRating;
    
    @Schema(description = "Timestamp when the customer was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the customer was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CustomerSequentialReadRequest {
    
    @Schema(description = "Starting customer ID for sequential read", example = "CUST12345", required = false)
    private String startingCustomerId;
    
    @Schema(description = "Number of records to read", example = "10", required = false)
    private Integer recordCount;
    
    @Size(max = 1, message = "Status filter must be 1 character")
    @Schema(description = "Customer status filter", example = "A", required = false)
    private String statusFilter;
    
    @Size(max = 2, message = "Customer type filter must not exceed 2 characters")
    @Schema(description = "Customer type filter", example = "P", required = false)
    private String customerTypeFilter;
    
    @Size(max = 3, message = "Credit rating filter must not exceed 3 characters")
    @Schema(description = "Credit rating filter", example = "AAA", required = false)
    private String creditRatingFilter;
    
    @Schema(description = "Include inactive customers", example = "false", required = false)
    private Boolean includeInactive;
    
    @Schema(description = "Include only personal customers", example = "true", required = false)
    private Boolean personalCustomersOnly;
    
    @Schema(description = "Include only business customers", example = "false", required = false)
    private Boolean businessCustomersOnly;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CustomerSequentialReadResponse {
    
    @Schema(description = "Starting customer ID used", example = "CUST12345", required = true)
    private String startingCustomerId;
    
    @Schema(description = "Number of records requested", example = "10", required = true)
    private Integer recordsRequested;
    
    @Schema(description = "Number of records returned", example = "8", required = true)
    private Integer recordsReturned;
    
    @Schema(description = "Has more records available", example = "true", required = true)
    private Boolean hasMoreRecords;
    
    @Schema(description = "Next customer ID for continuation", example = "CUST12353", required = false)
    private String nextCustomerId;
    
    @Schema(description = "List of customer responses", required = true)
    private List<CustomerResponse> customers;
    
    @Schema(description = "Total customers matching criteria", example = "25", required = true)
    private Long totalMatchingCustomers;
    
    @Schema(description = "Applied filters summary", example = "Status: A, Type: P, Rating: AAA", required = false)
    private String appliedFilters;
}