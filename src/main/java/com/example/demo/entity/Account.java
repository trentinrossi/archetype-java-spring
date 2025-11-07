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

/**
 * Represents a customer account with financial and status information.
 * This entity maps to the accounts table and contains all account-related data
 * including balances, limits, and important dates.
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    /**
     * Unique account identifier used as the record key.
     * Must be an 11-digit numeric value.
     */
    @Id
    @Column(name = "acct_id", nullable = false, unique = true)
    private Long acctId;
    
    /**
     * Indicates if the account is active.
     * Possible values: 'A' for active, 'I' for inactive.
     */
    @Column(name = "acct_active_status", nullable = false, length = 1)
    private String acctActiveStatus;
    
    /**
     * Current balance of the account.
     * Includes decimal places for cents.
     */
    @Column(name = "acct_curr_bal", nullable = false, precision = 15, scale = 2)
    private BigDecimal acctCurrBal;
    
    /**
     * Maximum credit limit for the account.
     * Includes decimal places for cents.
     */
    @Column(name = "acct_credit_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal acctCreditLimit;
    
    /**
     * Maximum cash credit limit for the account.
     * Includes decimal places for cents.
     */
    @Column(name = "acct_cash_credit_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal acctCashCreditLimit;
    
    /**
     * Date when the account was opened.
     */
    @Column(name = "acct_open_date", nullable = false)
    private LocalDate acctOpenDate;
    
    /**
     * Date when the account expires.
     */
    @Column(name = "acct_expiration_date", nullable = false)
    private LocalDate acctExpirationDate;
    
    /**
     * Date when the account was reissued.
     * This field is optional.
     */
    @Column(name = "acct_reissue_date")
    private LocalDate acctReissueDate;
    
    /**
     * Current cycle credit amount.
     * Includes decimal places for cents.
     */
    @Column(name = "acct_curr_cyc_credit", nullable = false, precision = 15, scale = 2)
    private BigDecimal acctCurrCycCredit;
    
    /**
     * Current cycle debit amount.
     * Includes decimal places for cents.
     */
    @Column(name = "acct_curr_cyc_debit", nullable = false, precision = 15, scale = 2)
    private BigDecimal acctCurrCycDebit;
    
    /**
     * Identifier for the account group.
     * May be used for categorizing accounts. This field is optional.
     */
    @Column(name = "acct_group_id")
    private String acctGroupId;
    
    /**
     * Timestamp when the record was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the record was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Constructor for creating a new account with required fields.
     */
    public Account(Long acctId, String acctActiveStatus, BigDecimal acctCurrBal, 
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
    
    /**
     * Checks if the account is active.
     * @return true if account status is 'A', false otherwise
     */
    public boolean isActive() {
        return "A".equalsIgnoreCase(acctActiveStatus);
    }
    
    /**
     * Checks if the account is expired.
     * @return true if expiration date is before today, false otherwise
     */
    public boolean isExpired() {
        return acctExpirationDate != null && acctExpirationDate.isBefore(LocalDate.now());
    }
    
    /**
     * Calculates the available credit.
     * @return credit limit minus current balance
     */
    public BigDecimal getAvailableCredit() {
        return acctCreditLimit.subtract(acctCurrBal);
    }
    
    /**
     * Calculates the available cash credit.
     * @return cash credit limit minus current balance
     */
    public BigDecimal getAvailableCashCredit() {
        return acctCashCreditLimit.subtract(acctCurrBal);
    }
    
    /**
     * Calculates the net cycle amount (credit minus debit).
     * @return current cycle credit minus current cycle debit
     */
    public BigDecimal getNetCycleAmount() {
        return acctCurrCycCredit.subtract(acctCurrCycDebit);
    }
    
    /**
     * Validates that the account ID is an 11-digit numeric value.
     * @return true if valid, false otherwise
     */
    public boolean isValidAccountId() {
        if (acctId == null) {
            return false;
        }
        String idStr = String.valueOf(acctId);
        return idStr.length() == 11 && acctId > 0;
    }
}
