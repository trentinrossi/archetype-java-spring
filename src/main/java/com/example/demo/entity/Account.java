package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "account_id", nullable = false, length = 11)
    private String accountId;
    
    @Column(name = "active_status", nullable = false, length = 1)
    private String activeStatus;
    
    @Column(name = "current_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentBalance;
    
    @Column(name = "credit_limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "cash_credit_limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal cashCreditLimit;
    
    @Column(name = "open_date", nullable = false, length = 10)
    private String openDate;
    
    @Column(name = "expiration_date", nullable = false, length = 10)
    private String expirationDate;
    
    @Column(name = "reissue_date", nullable = false, length = 10)
    private String reissueDate;
    
    @Column(name = "current_cycle_credit", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentCycleCredit;
    
    @Column(name = "current_cycle_debit", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentCycleDebit;
    
    @Column(name = "address_zip_code", nullable = false, length = 10)
    private String addressZipCode;
    
    @Column(name = "group_id", nullable = false, length = 10)
    private String groupId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
