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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false, unique = true, length = 9)
    private String customerId;
    
    @Column(name = "customer_data", nullable = false, length = 491)
    private String customerData;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Customer(String customerId, String customerData) {
        this.customerId = customerId;
        this.customerData = customerData;
    }
    
    @PrePersist
    @PreUpdate
    private void validateCustomerId() {
        if (customerId == null || customerId.length() != 9) {
            throw new IllegalArgumentException("Invalid customer ID format");
        }
        if (!customerId.matches("\\d{9}")) {
            throw new IllegalArgumentException("Invalid customer ID format");
        }
    }
}