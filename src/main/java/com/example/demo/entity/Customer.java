package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
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
    
    @Column(name = "first_name", length = 25, nullable = false)
    private String firstName;
    
    @Column(name = "middle_name", length = 25)
    private String middleName;
    
    @Column(name = "last_name", length = 25, nullable = false)
    private String lastName;
    
    @Column(name = "address_line1", length = 50, nullable = false)
    private String addressLine1;
    
    @Column(name = "address_line2", length = 50)
    private String addressLine2;
    
    @Column(name = "city", length = 50, nullable = false)
    private String city;
    
    @Column(name = "state_code", length = 2, nullable = false)
    private String stateCode;
    
    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;
    
    @Column(name = "country_code", length = 3, nullable = false)
    private String countryCode;
    
    @Column(name = "phone_number1", length = 15)
    private String phoneNumber1;
    
    @Column(name = "phone_number2", length = 15)
    private String phoneNumber2;
    
    @Column(name = "ssn", length = 9, nullable = false)
    private String ssn;
    
    @Column(name = "government_issued_id", length = 20)
    private String governmentIssuedId;
    
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Column(name = "eft_account_id", length = 10, nullable = false)
    private String eftAccountId;
    
    @Column(name = "primary_card_holder_indicator", length = 1)
    private String primaryCardHolderIndicator;
    
    @Column(name = "fico_credit_score")
    private Integer ficoCreditScore;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public String getFullName() {
        if (middleName != null && !middleName.isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }
    
    public boolean isPrimaryCardHolder() {
        return "Y".equalsIgnoreCase(primaryCardHolderIndicator);
    }
}
