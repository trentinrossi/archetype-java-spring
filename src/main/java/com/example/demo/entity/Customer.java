package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false, length = 9)
    private Long customerId;

    @Column(name = "cust_id", nullable = false, length = 9)
    private Long custId;

    @Column(name = "ssn", nullable = false, length = 9)
    private String ssn;

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

    @Column(name = "address_line_3", nullable = false, length = 50)
    private String addressLine3;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "state_code", nullable = false, length = 2)
    private String stateCode;

    @Column(name = "country_code", nullable = false, length = 3)
    private String countryCode;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Column(name = "phone_number_1", nullable = false, length = 15)
    private String phoneNumber1;

    @Column(name = "phone_number_2", length = 15)
    private String phoneNumber2;

    @Column(name = "date_of_birth", nullable = false, length = 8)
    private String dateOfBirth;

    @Column(name = "government_issued_id", length = 20)
    private String governmentIssuedId;

    @Column(name = "eft_account_id", length = 10)
    private String eftAccountId;

    @Column(name = "primary_holder_indicator", nullable = false, length = 1)
    private String primaryHolderIndicator;

    @Column(name = "fico_score", nullable = false, length = 3)
    private Integer ficoScore;

    @Column(name = "cust_data", nullable = false, length = 491)
    private String custData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void validateCustomer() {
        validateCustomerId();
        validateSsn();
        validateNames();
        validateDateOfBirth();
        validatePhoneNumbers();
        validateFicoScore();
        validateStateCode();
        validateCountryCode();
        validateZipCode();
        syncFields();
    }

    private void validateCustomerId() {
        if (customerId == null) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        String customerIdStr = String.valueOf(customerId);
        if (customerIdStr.length() > 9) {
            throw new IllegalArgumentException("Customer ID exceeds maximum length");
        }
    }

    private void validateSsn() {
        if (ssn == null || !ssn.matches("^\\d{9}$")) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        String firstThree = ssn.substring(0, 3);
        String middleTwo = ssn.substring(3, 5);
        String lastFour = ssn.substring(5, 9);
        if (firstThree.equals("000") || firstThree.equals("666") || 
            (Integer.parseInt(firstThree) >= 900 && Integer.parseInt(firstThree) <= 999)) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        if (middleTwo.equals("00") || lastFour.equals("0000")) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
    }

    private void validateNames() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }
        if (!firstName.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("First name is required and must contain only letters");
        }
        if (!lastName.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Last name is required and must contain only letters");
        }
        if (middleName != null && !middleName.trim().isEmpty() && !middleName.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Middle name must contain only letters");
        }
    }

    private void validateDateOfBirth() {
        if (dateOfBirth == null || !dateOfBirth.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Invalid date of birth or customer is under 18 years old");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate dob = LocalDate.parse(dateOfBirth, formatter);
            if (dob.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Invalid date of birth or customer is under 18 years old");
            }
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 18) {
                throw new IllegalArgumentException("Invalid date of birth or customer is under 18 years old");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date of birth or customer is under 18 years old");
        }
    }

    private void validatePhoneNumbers() {
        validatePhoneNumber(phoneNumber1, true);
        if (phoneNumber2 != null && !phoneNumber2.trim().isEmpty()) {
            validatePhoneNumber(phoneNumber2, false);
        }
    }

    private void validatePhoneNumber(String phone, boolean isPrimary) {
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        if (cleanPhone.length() != 10) {
            throw new IllegalArgumentException("Invalid " + (isPrimary ? "primary" : "secondary") + " phone number format");
        }
        String areaCode = cleanPhone.substring(0, 3);
        String prefix = cleanPhone.substring(3, 6);
        String lineNumber = cleanPhone.substring(6, 10);
        if (areaCode.equals("000") || areaCode.equals("911") || prefix.equals("000") || lineNumber.equals("0000")) {
            throw new IllegalArgumentException("Invalid " + (isPrimary ? "primary" : "secondary") + " phone number format");
        }
    }

    private void validateFicoScore() {
        if (ficoScore == null || ficoScore < 300 || ficoScore > 850) {
            throw new IllegalArgumentException("FICO score must be between 300 and 850");
        }
    }

    private void validateStateCode() {
        if (stateCode == null || stateCode.length() != 2) {
            throw new IllegalArgumentException("Invalid state code");
        }
    }

    private void validateCountryCode() {
        if (countryCode == null || countryCode.length() != 3) {
            throw new IllegalArgumentException("Invalid country code");
        }
    }

    private void validateZipCode() {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid ZIP code");
        }
    }

    private void syncFields() {
        if (custId == null) {
            custId = customerId;
        }
        if (city == null && addressLine3 != null) {
            city = addressLine3;
        }
    }

    public String getFullName() {
        if (middleName != null && !middleName.trim().isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }
}
