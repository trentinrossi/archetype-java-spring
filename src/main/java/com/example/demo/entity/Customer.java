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
    @Column(name = "customer_id", nullable = false, length = 9)
    private Long customerId;

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

    @Column(name = "government_issued_id", length = 20)
    private String governmentIssuedId;

    @Column(name = "eft_account_id", length = 10)
    private String eftAccountId;

    @Column(name = "primary_holder_indicator", nullable = false, length = 1)
    private String primaryHolderIndicator;

    @Column(name = "fico_score", nullable = false)
    private Integer ficoScore;

    @Column(name = "customer_data", nullable = false, length = 491)
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

    public String getFormattedSSN() {
        if (ssn != null && ssn.length() == 9) {
            return ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-" + ssn.substring(5);
        }
        return ssn;
    }

    public boolean isPrimaryCardHolder() {
        return "Y".equals(primaryHolderIndicator);
    }
}
