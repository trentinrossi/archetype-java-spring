package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @Column(name = "customer_id", length = 9, nullable = false)
    private String customerId;
    
    @Column(name = "customer_data", length = 491, nullable = false)
    private String customerData;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Column(name = "middle_initial", length = 1)
    private String middleInitial;
    
    @Column(name = "address_line_1", length = 100)
    private String addressLine1;
    
    @Column(name = "address_line_2", length = 100)
    private String addressLine2;
    
    @Column(name = "city", length = 50)
    private String city;
    
    @Column(name = "state", length = 2)
    private String state;
    
    @Column(name = "zip_code", length = 10)
    private String zipCode;
    
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;
    
    @Column(name = "email_address", length = 100)
    private String emailAddress;
    
    @Column(name = "customer_status", length = 1, nullable = false)
    private String customerStatus;
    
    @Column(name = "customer_type", length = 2)
    private String customerType;
    
    @Column(name = "credit_rating", length = 3)
    private String creditRating;
    
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    
    @Column(name = "social_security_number", length = 11)
    private String socialSecurityNumber;
    
    @Column(name = "employer_name", length = 50)
    private String employerName;
    
    @Column(name = "annual_income", length = 15)
    private String annualIncome;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Customer(String customerId, String customerData) {
        this.customerId = customerId;
        this.customerData = customerData;
        this.customerStatus = "A";
    }
    
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName.trim());
        }
        if (middleInitial != null && !middleInitial.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(middleInitial.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(lastName.trim());
        }
        return fullName.toString();
    }
    
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null && !addressLine1.trim().isEmpty()) {
            address.append(addressLine1.trim());
        }
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            if (address.length() > 0) address.append(", ");
            address.append(addressLine2.trim());
        }
        if (city != null && !city.trim().isEmpty()) {
            if (address.length() > 0) address.append(", ");
            address.append(city.trim());
        }
        if (state != null && !state.trim().isEmpty()) {
            if (address.length() > 0) address.append(", ");
            address.append(state.trim());
        }
        if (zipCode != null && !zipCode.trim().isEmpty()) {
            if (address.length() > 0) address.append(" ");
            address.append(zipCode.trim());
        }
        return address.toString();
    }
    
    public boolean isActive() {
        return "A".equals(customerStatus) || "Y".equals(customerStatus);
    }
    
    public boolean isInactive() {
        return "I".equals(customerStatus) || "N".equals(customerStatus);
    }
    
    public boolean isSuspended() {
        return "S".equals(customerStatus);
    }
    
    public boolean isClosed() {
        return "C".equals(customerStatus);
    }
    
    public String getCustomerStatusDescription() {
        if (isActive()) {
            return "Active";
        } else if (isInactive()) {
            return "Inactive";
        } else if (isSuspended()) {
            return "Suspended";
        } else if (isClosed()) {
            return "Closed";
        } else {
            return "Unknown";
        }
    }
    
    public boolean hasValidEmail() {
        return emailAddress != null && !emailAddress.trim().isEmpty() && emailAddress.contains("@");
    }
    
    public boolean hasValidPhone() {
        return phoneNumber != null && !phoneNumber.trim().isEmpty() && phoneNumber.length() >= 10;
    }
    
    public boolean isPersonalCustomer() {
        return "P".equals(customerType) || "01".equals(customerType);
    }
    
    public boolean isBusinessCustomer() {
        return "B".equals(customerType) || "02".equals(customerType);
    }
    
    public boolean hasGoodCreditRating() {
        return "AAA".equals(creditRating) || "AA".equals(creditRating) || "A".equals(creditRating);
    }
    
    public boolean hasPoorCreditRating() {
        return "D".equals(creditRating) || "F".equals(creditRating);
    }
    
    public String getMaskedSSN() {
        if (socialSecurityNumber != null && socialSecurityNumber.length() >= 4) {
            return "***-**-" + socialSecurityNumber.substring(socialSecurityNumber.length() - 4);
        }
        return "***-**-****";
    }
}