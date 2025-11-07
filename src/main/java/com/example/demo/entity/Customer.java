package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.CustomerStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer master data including demographic information, addresses, phone numbers, SSN, and credit scores
 * Entity represents customer profile with complete demographic and contact information
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @Column(name = "customer_id", length = 9, nullable = false)
    private String customerId;
    
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;
    
    @Column(name = "middle_initial", length = 1)
    private String middleInitial;
    
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;
    
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
    
    @Column(name = "country", length = 50)
    private String country;
    
    @Column(name = "home_phone", length = 20)
    private String homePhone;
    
    @Column(name = "work_phone", length = 20)
    private String workPhone;
    
    @Column(name = "mobile_phone", length = 20)
    private String mobilePhone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "ssn", length = 11)
    private String ssn;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "credit_score")
    private Integer creditScore;
    
    @Column(name = "fico_score")
    private Integer ficoScore;
    
    @Column(name = "government_id", length = 50)
    private String governmentId;
    
    @Column(name = "government_id_type", length = 20)
    private String governmentIdType;
    
    @Column(name = "customer_since")
    private LocalDate customerSince;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CustomerStatus status = CustomerStatus.ACTIVE;
    
    @Column(name = "vip_status")
    private Boolean vipStatus = false;
    
    @Column(name = "preferred_contact_method", length = 20)
    private String preferredContactMethod;
    
    @Column(name = "occupation", length = 100)
    private String occupation;
    
    @Column(name = "annual_income")
    private java.math.BigDecimal annualIncome;
    
    @Column(name = "employer_name", length = 100)
    private String employerName;
    
    @Column(name = "years_at_employer")
    private Integer yearsAtEmployer;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardCrossReference> cardCrossReferences;
    
    // Helper methods
    public String getFullName() {
        StringBuilder fullName = new StringBuilder(firstName);
        if (middleInitial != null && !middleInitial.isEmpty()) {
            fullName.append(" ").append(middleInitial).append(".");
        }
        fullName.append(" ").append(lastName);
        return fullName.toString();
    }
    
    public String getFormattedAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null) address.append(addressLine1);
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            address.append(", ").append(addressLine2);
        }
        if (city != null) address.append(", ").append(city);
        if (state != null) address.append(", ").append(state);
        if (zipCode != null) address.append(" ").append(zipCode);
        return address.toString();
    }
    
    public boolean isActive() {
        return CustomerStatus.ACTIVE.equals(status);
    }
    
    public boolean hasGoodCredit() {
        return creditScore != null && creditScore >= 650;
    }
    
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
