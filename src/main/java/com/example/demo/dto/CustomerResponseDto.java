package com.example.demo.dto;

import com.example.demo.enums.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for customer data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    
    @Schema(description = "Customer ID", example = "123456789")
    private String customerId;
    
    @Schema(description = "First name", example = "John")
    private String firstName;
    
    @Schema(description = "Middle initial", example = "M")
    private String middleInitial;
    
    @Schema(description = "Last name", example = "Doe")
    private String lastName;
    
    @Schema(description = "Full name", example = "John M. Doe")
    private String fullName;
    
    @Schema(description = "Address line 1", example = "123 Main Street")
    private String addressLine1;
    
    @Schema(description = "Address line 2", example = "Apt 4B")
    private String addressLine2;
    
    @Schema(description = "City", example = "New York")
    private String city;
    
    @Schema(description = "State code", example = "NY")
    private String state;
    
    @Schema(description = "ZIP code", example = "10001")
    private String zipCode;
    
    @Schema(description = "Country", example = "USA")
    private String country;
    
    @Schema(description = "Formatted address", example = "123 Main Street, Apt 4B, New York, NY 10001")
    private String formattedAddress;
    
    @Schema(description = "Home phone number", example = "212-555-1234")
    private String homePhone;
    
    @Schema(description = "Work phone number", example = "212-555-5678")
    private String workPhone;
    
    @Schema(description = "Mobile phone number", example = "917-555-9012")
    private String mobilePhone;
    
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "Social Security Number (masked)", example = "***-**-6789")
    private String ssn;
    
    @Schema(description = "Date of birth", example = "1980-01-15")
    private LocalDate dateOfBirth;
    
    @Schema(description = "Age", example = "43")
    private Integer age;
    
    @Schema(description = "Credit score", example = "720")
    private Integer creditScore;
    
    @Schema(description = "FICO score", example = "750")
    private Integer ficoScore;
    
    @Schema(description = "Government ID number", example = "DL123456")
    private String governmentId;
    
    @Schema(description = "Government ID type", example = "DRIVERS_LICENSE")
    private String governmentIdType;
    
    @Schema(description = "Customer since date", example = "2020-01-01")
    private LocalDate customerSince;
    
    @Schema(description = "Customer status", example = "ACTIVE")
    private CustomerStatus status;
    
    @Schema(description = "Customer status display name", example = "Active")
    private String statusDisplayName;
    
    @Schema(description = "VIP status", example = "false")
    private Boolean vipStatus;
    
    @Schema(description = "Preferred contact method", example = "EMAIL")
    private String preferredContactMethod;
    
    @Schema(description = "Occupation", example = "Software Engineer")
    private String occupation;
    
    @Schema(description = "Annual income", example = "75000.00")
    private BigDecimal annualIncome;
    
    @Schema(description = "Employer name", example = "Tech Corp")
    private String employerName;
    
    @Schema(description = "Years at employer", example = "5")
    private Integer yearsAtEmployer;
    
    @Schema(description = "Number of accounts", example = "2")
    private Integer accountCount;
    
    @Schema(description = "Created timestamp", example = "2020-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last updated timestamp", example = "2023-12-01T15:30:00")
    private LocalDateTime updatedAt;
}
