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
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    
    @Column(name = "current_balance", nullable = false, precision = 11, scale = 2)
    private BigDecimal currentBalance;
    
    @Column(name = "credit_limit", nullable = false, precision = 11, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "current_cycle_credit", nullable = false, precision = 11, scale = 2)
    private BigDecimal currentCycleCredit;
    
    @Column(name = "current_cycle_debit", nullable = false, precision = 11, scale = 2)
    private BigDecimal currentCycleDebit;
    
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
