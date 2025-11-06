package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_id", nullable = false, unique = true, length = 11)
    private Long accountId;
    
    @Column(name = "account_data", nullable = false, length = 289)
    private String accountData;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Account(Long accountId, String accountData) {
        this.accountId = accountId;
        this.accountData = accountData;
    }
    
    @PrePersist
    @PreUpdate
    private void validateAccountId() {
        if (accountId == null) {
            throw new IllegalArgumentException("Invalid account ID format");
        }
        
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() != 11 || !accountIdStr.matches("\\d{11}")) {
            throw new IllegalArgumentException("Invalid account ID format");
        }
    }
}