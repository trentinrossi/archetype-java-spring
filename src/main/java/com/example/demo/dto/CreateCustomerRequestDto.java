package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a new customer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDto {
    
    @Schema(description = "Customer ID - must be 9 numeric characters", example = "123456789", required = true)
    private String customerId;
    
    @Schema(description = "First name of the customer", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Middle initial", example = "M", required = false)
    private String middleInitial;
    
    @Schema(description = "Last name of the customer", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Address line 1", example = "123 Main Street", required = false)
    private String addressLine1;
    
    @Schema(description = "Address line 2", example = "Apt 4B", required = false)
    private String addressLine2;
    
    @Schema(description = "City", example = "New York", required = false)
    private String city;
    
    @Schema(description = "State code", example = "NY", required = false)
    private String state;
    
    @Schema(description = "ZIP code", example = "10001", required = false)
    private String zipCode;
    
    @Schema(description = "Country", example = "USA", required = false)
    private String country;
    
    @Schema(description = "Home phone number", example = "212-555-1234", required = false)
    private String homePhone;
    
    @Schema(description = "Work phone number", example = "212-555-5678", required = false)
    private String workPhone;
    
    @Schema(description = "Mobile phone number", example = "917-555-9012", required = false)
    private String mobilePhone;
    
    @Schema(description = "Email address", example = "john.doe@example.com", required = false)
    private String email;
    
    @Schema(description = "Social Security Number", example = "123-45-6789", required = false)
    private String ssn;
    
    @Schema(description = "Date of birth", example = "1980-01-15", required = false)
    private LocalDate dateOfBirth;
    
    @Schema(description = "Credit score", example = "720", required = false)
    private Integer creditScore;
    
    @Schema(description = "FICO score", example = "750", required = false)
    private Integer ficoScore;
    
    @Schema(description = "Government ID number", example = "DL123456", required = false)
    private String governmentId;
    
    @Schema(description = "Government ID type", example = "DRIVERS_LICENSE", required = false)
    private String governmentIdType;
    
    @Schema(description = "Customer since date", example = "2020-01-01", required = false)
    private LocalDate customerSince;
    
    @Schema(description = "VIP status", example = "false", required = false)
    private Boolean vipStatus;
    
    @Schema(description = "Preferred contact method", example = "EMAIL", required = false)
    private String preferredContactMethod;
    
    @Schema(description = "Occupation", example = "Software Engineer", required = false)
    private String occupation;
    
    @Schema(description = "Annual income", example = "75000.00", required = false)
    private BigDecimal annualIncome;
    
    @Schema(description = "Employer name", example = "Tech Corp", required = false)
    private String employerName;
    
    @Schema(description = "Years at employer", example = "5", required = false)
    private Integer yearsAtEmployer;
}
