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
    
    @Column(name = "active_status", length = 1, nullable = false)
    private String activeStatus;
    
    @Column(name = "current_balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal currentBalance;
    
    @Column(name = "credit_limit", precision = 19, scale = 2, nullable = false)
    private BigDecimal creditLimit;
    
    @Column(name = "cash_credit_limit", precision = 19, scale = 2, nullable = false)
    private BigDecimal cashCreditLimit;
    
    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;
    
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
    
    @Column(name = "reissue_date")
    private LocalDate reissueDate;
    
    @Column(name = "current_cycle_credit", precision = 19, scale = 2, nullable = false)
    private BigDecimal currentCycleCredit;
    
    @Column(name = "current_cycle_debit", precision = 19, scale = 2, nullable = false)
    private BigDecimal currentCycleDebit;
    
    @Column(name = "group_id", length = 10)
    private String groupId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Account(String accountId, String activeStatus, BigDecimal currentBalance, 
                   BigDecimal creditLimit, BigDecimal cashCreditLimit, LocalDate openDate, 
                   LocalDate expirationDate) {
        this.accountId = accountId;
        this.activeStatus = activeStatus;
        this.currentBalance = currentBalance;
        this.creditLimit = creditLimit;
        this.cashCreditLimit = cashCreditLimit;
        this.openDate = openDate;
        this.expirationDate = expirationDate;
        this.currentCycleCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
    }
    
    public boolean isActive() {
        return "Y".equalsIgnoreCase(activeStatus);
    }
    
    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(currentBalance);
    }
    
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }
}
