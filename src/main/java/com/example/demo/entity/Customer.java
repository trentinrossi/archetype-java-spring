package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false, length = 9)
    private Long customerId;

    @Column(name = "first_name", nullable = false, length = 25)
    private String firstName;

    @Column(name = "middle_name", length = 25)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 25)
    private String lastName;

    @Column(name = "address_line_1", nullable = false, length = 50)
    private String addressLine1;

    @Column(name = "address_line_2", length = 50)
    private String addressLine2;

    @Column(name = "address_line_3", length = 50)
    private String addressLine3;

    @Column(name = "state_code", nullable = false, length = 2)
    private String stateCode;

    @Column(name = "country_code", nullable = false, length = 3)
    private String countryCode;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "fico_credit_score", nullable = false, length = 3)
    private Integer ficoCreditScore;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Customer(Long customerId, String firstName, String middleName, String lastName,
                    String addressLine1, String addressLine2, String addressLine3,
                    String stateCode, String countryCode, String zipCode, Integer ficoCreditScore) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.stateCode = stateCode;
        this.countryCode = countryCode;
        this.zipCode = zipCode;
        this.ficoCreditScore = ficoCreditScore;
    }

    /**
     * BR006: Customer Name Composition
     * Composes full name from first name, middle name (if present), and last name
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        fullName.append(firstName);
        
        if (middleName != null && !middleName.trim().isEmpty()) {
            fullName.append(" ").append(middleName);
        }
        
        fullName.append(" ").append(lastName);
        
        return fullName.toString();
    }

    /**
     * BR007: Complete Address Display
     * Composes complete address including all available address lines, state, country, and ZIP code
     */
    public String getCompleteAddress() {
        StringBuilder completeAddress = new StringBuilder();
        
        completeAddress.append(addressLine1);
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            completeAddress.append(", ").append(addressLine2);
        }
        
        if (addressLine3 != null && !addressLine3.trim().isEmpty()) {
            completeAddress.append(", ").append(addressLine3);
        }
        
        completeAddress.append(", ").append(stateCode);
        completeAddress.append(", ").append(countryCode);
        completeAddress.append(" ").append(zipCode);
        
        return completeAddress.toString();
    }

    /**
     * Returns address as separate lines for display purposes
     */
    public List<String> getAddressLines() {
        List<String> addressLines = new ArrayList<>();
        
        addressLines.add(addressLine1);
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            addressLines.add(addressLine2);
        }
        
        if (addressLine3 != null && !addressLine3.trim().isEmpty()) {
            addressLines.add(addressLine3);
        }
        
        addressLines.add(stateCode + ", " + countryCode + " " + zipCode);
        
        return addressLines;
    }

    @PrePersist
    @PreUpdate
    private void validateCustomer() {
        if (customerId == null || customerId.toString().length() != 9) {
            throw new IllegalArgumentException("Customer ID must be 9 digits numeric");
        }

        if (firstName == null || firstName.trim().isEmpty() || firstName.length() > 25) {
            throw new IllegalArgumentException("First name must not be empty and maximum 25 characters");
        }

        if (middleName != null && middleName.length() > 25) {
            throw new IllegalArgumentException("Middle name maximum 25 characters");
        }

        if (lastName == null || lastName.trim().isEmpty() || lastName.length() > 25) {
            throw new IllegalArgumentException("Last name must not be empty and maximum 25 characters");
        }

        if (addressLine1 == null || addressLine1.trim().isEmpty() || addressLine1.length() > 50) {
            throw new IllegalArgumentException("Address line 1 must not be empty and maximum 50 characters");
        }

        if (addressLine2 != null && addressLine2.length() > 50) {
            throw new IllegalArgumentException("Address line 2 maximum 50 characters");
        }

        if (addressLine3 != null && addressLine3.length() > 50) {
            throw new IllegalArgumentException("Address line 3 maximum 50 characters");
        }

        if (stateCode == null || stateCode.length() != 2) {
            throw new IllegalArgumentException("State code must be 2 characters");
        }

        if (countryCode == null || countryCode.length() != 3) {
            throw new IllegalArgumentException("Country code must be 3 characters");
        }

        if (zipCode == null || zipCode.length() != 10) {
            throw new IllegalArgumentException("ZIP code must be 10 characters alphanumeric");
        }

        if (ficoCreditScore == null || ficoCreditScore.toString().length() != 3) {
            throw new IllegalArgumentException("FICO credit score must be 3 digits numeric");
        }

        if (ficoCreditScore < 0 || ficoCreditScore > 999) {
            throw new IllegalArgumentException("FICO credit score must be between 0 and 999");
        }
    }
}
