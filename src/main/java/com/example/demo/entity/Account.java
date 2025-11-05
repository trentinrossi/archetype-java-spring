package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "account_id", length = 11, nullable = false)
    private String accountId;
    
    @Column(name = "customer_id", length = 9, nullable = false)
    private String customerId;
    
    @Column(name = "current_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal currentBalance;
    
    @Column(name = "credit_limit", precision = 19, scale = 2, nullable = false)
    private BigDecimal creditLimit;
    
    @Column(name = "current_cycle_credit", precision = 19, scale = 2, nullable = false)
    private BigDecimal currentCycleCredit;
    
    @Column(name = "current_cycle_debit", precision = 19, scale = 2, nullable = false)
    private BigDecimal currentCycleDebit;
    
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
    
    @Column(name = "account_status", length = 10, nullable = false)
    private String accountStatus;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
