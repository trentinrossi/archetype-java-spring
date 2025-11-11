package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Column(name = "acct_id", nullable = false, length = 11)
    @NotNull(message = "Invalid Account ID format")
    @Pattern(regexp = "^\\d{11}$", message = "Invalid Account ID format")
    private String acctId;

    @Column(name = "acct_active_status", nullable = false, length = 1)
    @NotNull(message = "Account active status is required")
    @Size(min = 1, max = 1, message = "Account active status must be 1 character")
    @Pattern(regexp = "^[AI]$", message = "Account active status must be 'A' for active or 'I' for inactive")
    private String acctActiveStatus;

    @Column(name = "acct_curr_bal", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Current balance is required")
    private BigDecimal acctCurrBal;

    @Column(name = "acct_credit_limit", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Credit limit is required")
    private BigDecimal acctCreditLimit;

    @Column(name = "acct_cash_credit_limit", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Cash credit limit is required")
    private BigDecimal acctCashCreditLimit;

    @Column(name = "acct_open_date", nullable = false)
    @NotNull(message = "Account open date is required")
    private LocalDate acctOpenDate;

    @Column(name = "acct_expiration_date", nullable = false)
    @NotNull(message = "Account expiration date is required")
    private LocalDate acctExpirationDate;

    @Column(name = "acct_reissue_date")
    private LocalDate acctReissueDate;

    @Column(name = "acct_curr_cyc_credit", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Current cycle credit is required")
    private BigDecimal acctCurrCycCredit;

    @Column(name = "acct_curr_cyc_debit", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Current cycle debit is required")
    private BigDecimal acctCurrCycDebit;

    @Column(name = "acct_group_id", length = 10)
    private String acctGroupId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(String acctId, String acctActiveStatus, BigDecimal acctCurrBal, 
                   BigDecimal acctCreditLimit, BigDecimal acctCashCreditLimit,
                   LocalDate acctOpenDate, LocalDate acctExpirationDate,
                   BigDecimal acctCurrCycCredit, BigDecimal acctCurrCycDebit) {
        this.acctId = acctId;
        this.acctActiveStatus = acctActiveStatus;
        this.acctCurrBal = acctCurrBal;
        this.acctCreditLimit = acctCreditLimit;
        this.acctCashCreditLimit = acctCashCreditLimit;
        this.acctOpenDate = acctOpenDate;
        this.acctExpirationDate = acctExpirationDate;
        this.acctCurrCycCredit = acctCurrCycCredit;
        this.acctCurrCycDebit = acctCurrCycDebit;
    }

    public boolean isActive() {
        return "A".equals(this.acctActiveStatus);
    }

    public boolean isInactive() {
        return "I".equals(this.acctActiveStatus);
    }

    public BigDecimal getAvailableCredit() {
        return this.acctCreditLimit.subtract(this.acctCurrBal);
    }

    public BigDecimal getAvailableCashCredit() {
        return this.acctCashCreditLimit.subtract(this.acctCurrBal);
    }

    public BigDecimal getCurrentCycleNetAmount() {
        return this.acctCurrCycCredit.subtract(this.acctCurrCycDebit);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.acctExpirationDate);
    }

    public boolean hasBeenReissued() {
        return this.acctReissueDate != null;
    }

    public String getAccountDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account ID: ").append(this.acctId).append("\n");
        sb.append("Active Status: ").append(this.acctActiveStatus).append("\n");
        sb.append("Current Balance: ").append(this.acctCurrBal).append("\n");
        sb.append("Credit Limit: ").append(this.acctCreditLimit).append("\n");
        sb.append("Cash Credit Limit: ").append(this.acctCashCreditLimit).append("\n");
        sb.append("Open Date: ").append(this.acctOpenDate).append("\n");
        sb.append("Expiration Date: ").append(this.acctExpirationDate).append("\n");
        sb.append("Reissue Date: ").append(this.acctReissueDate != null ? this.acctReissueDate : "N/A").append("\n");
        sb.append("Current Cycle Credit: ").append(this.acctCurrCycCredit).append("\n");
        sb.append("Current Cycle Debit: ").append(this.acctCurrCycDebit).append("\n");
        sb.append("Group ID: ").append(this.acctGroupId != null ? this.acctGroupId : "N/A").append("\n");
        sb.append("-----------------------------------------------------------");
        return sb.toString();
    }

    @PrePersist
    @PreUpdate
    public void validateAccountData() {
        if (this.acctId != null && !this.acctId.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("Invalid Account ID format");
        }
        
        if (this.acctCurrBal != null && this.acctCurrBal.compareTo(BigDecimal.ZERO) < 0) {
            if (this.acctCreditLimit != null && this.acctCurrBal.abs().compareTo(this.acctCreditLimit) > 0) {
                throw new IllegalArgumentException("Current balance exceeds credit limit");
            }
        }
        
        if (this.acctOpenDate != null && this.acctExpirationDate != null) {
            if (this.acctExpirationDate.isBefore(this.acctOpenDate)) {
                throw new IllegalArgumentException("Expiration date cannot be before open date");
            }
        }
        
        if (this.acctReissueDate != null && this.acctOpenDate != null) {
            if (this.acctReissueDate.isBefore(this.acctOpenDate)) {
                throw new IllegalArgumentException("Reissue date cannot be before open date");
            }
        }
    }
}
