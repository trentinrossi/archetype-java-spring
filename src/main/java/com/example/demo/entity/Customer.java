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

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false, length = 9)
    private String customerId;

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

    @Column(name = "government_id", length = 20)
    private String governmentId;

    @Column(name = "eft_account_id", length = 10)
    private String eftAccountId;

    @Column(name = "primary_card_holder_indicator", nullable = false, length = 1)
    private String primaryCardHolderIndicator;

    @Column(name = "fico_credit_score", nullable = false)
    private Integer ficoCreditScore;

    @Column(name = "customer_data", length = 491)
    private String customerData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public String getFullName() {
        if (middleName != null && !middleName.trim().isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }

    public String getFormattedSsn() {
        if (ssn != null && ssn.length() == 9) {
            return ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-" + ssn.substring(5);
        }
        return ssn;
    }

    public boolean isPrimaryCardHolder() {
        return "Y".equalsIgnoreCase(primaryCardHolderIndicator);
    }

    public Integer getAge() {
        if (dateOfBirth != null && dateOfBirth.length() == 8) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
                return Period.between(birthDate, LocalDate.now()).getYears();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
