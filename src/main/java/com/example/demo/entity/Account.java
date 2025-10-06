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
    @Column(name = "acct_id", length = 11, nullable = false)
    private String acctId;
    
    @Column(name = "acct_active_status", length = 1, nullable = false)
    private String acctActiveStatus;
    
    @Column(name = "acct_curr_bal", precision = 15, scale = 2, nullable = false)
    private BigDecimal acctCurrBal;
    
    @Column(name = "acct_credit_limit", precision = 15, scale = 2, nullable = false)
    private BigDecimal acctCreditLimit;
    
    @Column(name = "acct_cash_credit_limit", precision = 15, scale = 2, nullable = false)
    private BigDecimal acctCashCreditLimit;
    
    @Column(name = "acct_open_date", nullable = false)
    private LocalDate acctOpenDate;
    
    @Column(name = "acct_expiration_date", nullable = false)
    private LocalDate acctExpirationDate;
    
    @Column(name = "acct_reissue_date")
    private LocalDate acctReissueDate;
    
    @Column(name = "acct_curr_cyc_credit", precision = 15, scale = 2, nullable = false)
    private BigDecimal acctCurrCycCredit;
    
    @Column(name = "acct_curr_cyc_debit", precision = 15, scale = 2, nullable = false)
    private BigDecimal acctCurrCycDebit;
    
    @Column(name = "acct_group_id", length = 10, nullable = false)
    private String acctGroupId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Account(String acctId, String acctActiveStatus, BigDecimal acctCurrBal, 
                  BigDecimal acctCreditLimit, BigDecimal acctCashCreditLimit, 
                  LocalDate acctOpenDate, LocalDate acctExpirationDate, String acctGroupId) {
        this.acctId = acctId;
        this.acctActiveStatus = acctActiveStatus;
        this.acctCurrBal = acctCurrBal;
        this.acctCreditLimit = acctCreditLimit;
        this.acctCashCreditLimit = acctCashCreditLimit;
        this.acctOpenDate = acctOpenDate;
        this.acctExpirationDate = acctExpirationDate;
        this.acctGroupId = acctGroupId;
        this.acctCurrCycCredit = BigDecimal.ZERO;
        this.acctCurrCycDebit = BigDecimal.ZERO;
    }
    
    public boolean isActive() {
        return "Y".equals(acctActiveStatus) || "A".equals(acctActiveStatus);
    }
    
    public boolean isExpired() {
        return acctExpirationDate != null && acctExpirationDate.isBefore(LocalDate.now());
    }
    
    public BigDecimal getAvailableCredit() {
        return acctCreditLimit.subtract(acctCurrBal);
    }
    
    public BigDecimal getAvailableCashCredit() {
        return acctCashCreditLimit.subtract(acctCurrBal);
    }
    
    public BigDecimal getCycleNetAmount() {
        return acctCurrCycCredit.subtract(acctCurrCycDebit);
    }
    
    public boolean hasRecentReissue() {
        return acctReissueDate != null && acctReissueDate.isAfter(LocalDate.now().minusMonths(6));
    }
    
    public boolean isOverLimit() {
        return acctCurrBal.compareTo(acctCreditLimit) > 0;
    }
    
    public boolean isOverCashLimit() {
        return acctCurrBal.compareTo(acctCashCreditLimit) > 0;
    }
    
    public String getAccountStatusDescription() {
        if (isActive()) {
            return "Active";
        } else if ("N".equals(acctActiveStatus)) {
            return "Inactive";
        } else if ("S".equals(acctActiveStatus)) {
            return "Suspended";
        } else if ("C".equals(acctActiveStatus)) {
            return "Closed";
        } else {
            return "Unknown";
        }
    }
}