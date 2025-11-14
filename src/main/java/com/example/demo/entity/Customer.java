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
import java.util.List;
import java.util.regex.Pattern;

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

    @Column(name = "address_line_3", nullable = false, length = 50)
    private String addressLine3;

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

    @Column(name = "ssn", nullable = false, length = 9)
    private Long ssn;

    @Column(name = "government_issued_id", length = 20)
    private String governmentIssuedId;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "eft_account_id", length = 10)
    private String eftAccountId;

    @Column(name = "primary_cardholder_indicator", nullable = false, length = 1)
    private String primaryCardholderIndicator;

    @Column(name = "fico_score", nullable = false, precision = 3)
    private Integer ficoScore;

    @Column(name = "fico_credit_score", precision = 3)
    private Integer ficoCreditScore;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "primary_phone_number", nullable = false, length = 13)
    private String primaryPhoneNumber;

    @Column(name = "secondary_phone_number", length = 13)
    private String secondaryPhoneNumber;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards;

    private static final Pattern ALPHABETIC_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\(\\d{3}\\)\\d{3}-\\d{4}$");
    private static final Pattern STATE_CODE_PATTERN = Pattern.compile("^[A-Z]{2}$");
    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("^[A-Z]{3}$");
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^\\d{5}$");
    private static final Pattern EFT_ACCOUNT_PATTERN = Pattern.compile("^\\d{10}$");

    public Customer(Long customerId, String firstName, String lastName, String addressLine1, 
                   String addressLine3, String stateCode, String countryCode, String zipCode,
                   String phoneNumber1, Long ssn, LocalDate dateOfBirth, 
                   String primaryCardholderIndicator, Integer ficoScore, String city,
                   String primaryPhoneNumber) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine3 = addressLine3;
        this.stateCode = stateCode;
        this.countryCode = countryCode;
        this.zipCode = zipCode;
        this.phoneNumber1 = phoneNumber1;
        this.ssn = ssn;
        this.dateOfBirth = dateOfBirth;
        this.primaryCardholderIndicator = primaryCardholderIndicator;
        this.ficoScore = ficoScore;
        this.city = city;
        this.primaryPhoneNumber = primaryPhoneNumber;
    }

    public String getFullName() {
        if (middleName != null && !middleName.trim().isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }

    public String getFormattedSsn() {
        if (ssn == null) {
            return null;
        }
        String ssnStr = String.format("%09d", ssn);
        return ssnStr.substring(0, 3) + "-" + ssnStr.substring(3, 5) + "-" + ssnStr.substring(5);
    }

    public Integer calculateAge() {
        if (dateOfBirth == null) {
            return null;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public boolean meetsAgeRequirement() {
        Integer age = calculateAge();
        if (age == null) {
            return false;
        }
        return age >= 18 && age <= 120;
    }

    public boolean isPrimaryCardholder() {
        return "Y".equalsIgnoreCase(primaryCardholderIndicator);
    }

    @PrePersist
    @PreUpdate
    private void validateCustomer() {
        validateCustomerId();
        validateFirstName();
        validateMiddleName();
        validateLastName();
        validateAddressLine1();
        validateAddressLine3();
        validateStateCode();
        validateCountryCode();
        validateZipCode();
        validatePhoneNumber1();
        validatePhoneNumber2();
        validateSsn();
        validateDateOfBirth();
        validateEftAccountId();
        validatePrimaryCardholderIndicator();
        validateFicoScore();
        validateCity();
        validatePrimaryPhoneNumber();
        validateSecondaryPhoneNumber();
    }

    private void validateCustomerId() {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        String customerIdStr = String.valueOf(customerId);
        if (customerIdStr.length() != 9) {
            throw new IllegalArgumentException("Customer ID must be 9 digits");
        }
    }

    private void validateFirstName() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name must contain only letters");
        }
        if (!ALPHABETIC_PATTERN.matcher(firstName).matches()) {
            throw new IllegalArgumentException("First name must contain only letters");
        }
    }

    private void validateMiddleName() {
        if (middleName != null && !middleName.trim().isEmpty()) {
            if (!ALPHABETIC_PATTERN.matcher(middleName).matches()) {
                throw new IllegalArgumentException("Middle name must contain only letters");
            }
        }
    }

    private void validateLastName() {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name must contain only letters");
        }
        if (!ALPHABETIC_PATTERN.matcher(lastName).matches()) {
            throw new IllegalArgumentException("Last name must contain only letters");
        }
    }

    private void validateAddressLine1() {
        if (addressLine1 == null || addressLine1.trim().isEmpty()) {
            throw new IllegalArgumentException("Address line 1 is required");
        }
    }

    private void validateAddressLine3() {
        if (addressLine3 == null || addressLine3.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required and must contain only letters");
        }
        if (!ALPHABETIC_PATTERN.matcher(addressLine3).matches()) {
            throw new IllegalArgumentException("City is required and must contain only letters");
        }
    }

    private void validateStateCode() {
        if (stateCode == null || !STATE_CODE_PATTERN.matcher(stateCode).matches()) {
            throw new IllegalArgumentException("Invalid state code");
        }
    }

    private void validateCountryCode() {
        if (countryCode == null || !COUNTRY_CODE_PATTERN.matcher(countryCode).matches()) {
            throw new IllegalArgumentException("Invalid country code");
        }
    }

    private void validateZipCode() {
        if (zipCode == null || !ZIP_CODE_PATTERN.matcher(zipCode).matches()) {
            throw new IllegalArgumentException("Invalid zip code for state");
        }
    }

    private void validatePhoneNumber1() {
        if (phoneNumber1 == null || !PHONE_PATTERN.matcher(phoneNumber1).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        String digits = phoneNumber1.replaceAll("[^0-9]", "");
        if (digits.substring(0, 3).equals("000") || 
            digits.substring(3, 6).equals("000") || 
            digits.substring(6).equals("0000")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }

    private void validatePhoneNumber2() {
        if (phoneNumber2 != null && !phoneNumber2.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phoneNumber2).matches()) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
            String digits = phoneNumber2.replaceAll("[^0-9]", "");
            if (digits.substring(0, 3).equals("000") || 
                digits.substring(3, 6).equals("000") || 
                digits.substring(6).equals("0000")) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }

    private void validateSsn() {
        if (ssn == null) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        String ssnStr = String.format("%09d", ssn);
        if (ssnStr.length() != 9) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        int areaNumber = Integer.parseInt(ssnStr.substring(0, 3));
        int groupNumber = Integer.parseInt(ssnStr.substring(3, 5));
        int serialNumber = Integer.parseInt(ssnStr.substring(5));
        
        if (areaNumber == 0 || areaNumber == 666 || (areaNumber >= 900 && areaNumber <= 999)) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        if (groupNumber == 0) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
        if (serialNumber == 0) {
            throw new IllegalArgumentException("Invalid Social Security Number");
        }
    }

    private void validateDateOfBirth() {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        LocalDate now = LocalDate.now();
        if (dateOfBirth.isAfter(now)) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
        int age = Period.between(dateOfBirth, now).getYears();
        if (age < 18 || age > 120) {
            throw new IllegalArgumentException("Invalid date of birth or customer does not meet age requirement");
        }
    }

    private void validateEftAccountId() {
        if (eftAccountId != null && !eftAccountId.trim().isEmpty()) {
            if (!EFT_ACCOUNT_PATTERN.matcher(eftAccountId).matches()) {
                throw new IllegalArgumentException("EFT Account ID must be 10 digits");
            }
        }
    }

    private void validatePrimaryCardholderIndicator() {
        if (primaryCardholderIndicator == null || 
            (!primaryCardholderIndicator.equals("Y") && !primaryCardholderIndicator.equals("N"))) {
            throw new IllegalArgumentException("Primary cardholder indicator must be Y or N");
        }
    }

    private void validateFicoScore() {
        if (ficoScore == null || ficoScore < 300 || ficoScore > 850) {
            throw new IllegalArgumentException("FICO Score should be between 300 and 850");
        }
    }

    private void validateCity() {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required and must contain only letters");
        }
        if (!ALPHABETIC_PATTERN.matcher(city).matches()) {
            throw new IllegalArgumentException("City is required and must contain only letters");
        }
    }

    private void validatePrimaryPhoneNumber() {
        if (primaryPhoneNumber == null || primaryPhoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }

    private void validateSecondaryPhoneNumber() {
        if (secondaryPhoneNumber != null && !secondaryPhoneNumber.trim().isEmpty()) {
            if (secondaryPhoneNumber.length() > 13) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
        }
    }
}
