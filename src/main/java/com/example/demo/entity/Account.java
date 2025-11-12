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
    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "acct_id", nullable = false, length = 11)
    private Long acctId;

    @Column(name = "acct_active_status", nullable = false, length = 1)
    private String acctActiveStatus;

    @Column(name = "acct_curr_bal", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrBal;

    @Column(name = "acct_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCreditLimit;

    @Column(name = "acct_cash_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCashCreditLimit;

    @Column(name = "acct_open_date", nullable = false, length = 8)
    private LocalDate acctOpenDate;

    @Column(name = "acct_expiraion_date", nullable = false, length = 8)
    private LocalDate acctExpiraionDate;

    @Column(name = "acct_reissue_date", length = 8)
    private LocalDate acctReissueDate;

    @Column(name = "acct_curr_cyc_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycCredit;

    @Column(name = "acct_curr_cyc_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycDebit;

    @Column(name = "acct_group_id", length = 10)
    private String acctGroupId;

    @Column(name = "active_status", nullable = false, length = 1)
    private String activeStatus;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "cash_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashCreditLimit;

    @Column(name = "open_date", nullable = false, length = 8)
    private LocalDate openDate;

    @Column(name = "expiration_date", nullable = false, length = 8)
    private LocalDate expirationDate;

    @Column(name = "reissue_date", nullable = false, length = 8)
    private LocalDate reissueDate;

    @Column(name = "current_cycle_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleCredit;

    @Column(name = "current_cycle_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleDebit;

    @Column(name = "group_id", length = 10)
    private String groupId;

    @Column(name = "account_status", nullable = false, length = 1)
    private String accountStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(Long accountId, String activeStatus, BigDecimal currentBalance, BigDecimal creditLimit, 
                   BigDecimal cashCreditLimit, LocalDate openDate, LocalDate expirationDate) {
        this.accountId = accountId;
        this.acctId = accountId;
        this.activeStatus = activeStatus;
        this.acctActiveStatus = activeStatus;
        this.currentBalance = currentBalance;
        this.acctCurrBal = currentBalance;
        this.creditLimit = creditLimit;
        this.acctCreditLimit = creditLimit;
        this.cashCreditLimit = cashCreditLimit;
        this.acctCashCreditLimit = cashCreditLimit;
        this.openDate = openDate;
        this.acctOpenDate = openDate;
        this.expirationDate = expirationDate;
        this.acctExpiraionDate = expirationDate;
        this.currentCycleCredit = BigDecimal.ZERO;
        this.acctCurrCycCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
        this.acctCurrCycDebit = BigDecimal.ZERO;
        this.accountStatus = activeStatus;
    }

    public boolean isActive() {
        return "Y".equalsIgnoreCase(this.activeStatus) || "A".equalsIgnoreCase(this.acctActiveStatus);
    }

    public void addInterestToBalance(BigDecimal interestAmount) {
        if (interestAmount != null && interestAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.currentBalance = this.currentBalance.add(interestAmount);
            this.acctCurrBal = this.acctCurrBal.add(interestAmount);
        }
    }

    public void resetCycleAmounts() {
        this.currentCycleCredit = BigDecimal.ZERO;
        this.acctCurrCycCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
        this.acctCurrCycDebit = BigDecimal.ZERO;
    }

    public BigDecimal calculateMonthlyInterest(BigDecimal annualRate) {
        if (annualRate == null || annualRate.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal balance = this.currentBalance != null ? this.currentBalance : BigDecimal.ZERO;
        return balance.multiply(annualRate).divide(new BigDecimal("1200"), 2, BigDecimal.ROUND_HALF_UP);
    }

    public boolean isAccountIdValid() {
        if (this.accountId == null) {
            return false;
        }
        String accountIdStr = String.valueOf(this.accountId);
        if (accountIdStr.length() != 11) {
            return false;
        }
        if (this.accountId == 0L) {
            return false;
        }
        return true;
    }

    public boolean isActiveStatusValid() {
        return "Y".equalsIgnoreCase(this.activeStatus) || "N".equalsIgnoreCase(this.activeStatus);
    }

    public boolean isCreditLimitValid() {
        return this.creditLimit != null && this.creditLimit.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isCashCreditLimitValid() {
        return this.cashCreditLimit != null && this.cashCreditLimit.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isOpenDateValid() {
        if (this.openDate == null) {
            return false;
        }
        return !this.openDate.isAfter(LocalDate.now());
    }

    public boolean isExpirationDateValid() {
        if (this.expirationDate == null) {
            return false;
        }
        try {
            int year = this.expirationDate.getYear();
            int month = this.expirationDate.getMonthValue();
            int day = this.expirationDate.getDayOfMonth();
            return year >= 1000 && year <= 9999 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReissueDateValid() {
        if (this.reissueDate == null) {
            return true;
        }
        try {
            int year = this.reissueDate.getYear();
            int month = this.reissueDate.getMonthValue();
            int day = this.reissueDate.getDayOfMonth();
            return year >= 1000 && year <= 9999 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAccountDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account ID: ").append(this.accountId).append("\n");
        sb.append("Active Status: ").append(this.activeStatus).append("\n");
        sb.append("Current Balance: ").append(this.currentBalance).append("\n");
        sb.append("Credit Limit: ").append(this.creditLimit).append("\n");
        sb.append("Cash Credit Limit: ").append(this.cashCreditLimit).append("\n");
        sb.append("Open Date: ").append(this.openDate).append("\n");
        sb.append("Expiration Date: ").append(this.expirationDate).append("\n");
        sb.append("Reissue Date: ").append(this.reissueDate).append("\n");
        sb.append("Current Cycle Credit: ").append(this.currentCycleCredit).append("\n");
        sb.append("Current Cycle Debit: ").append(this.currentCycleDebit).append("\n");
        sb.append("Group ID: ").append(this.groupId).append("\n");
        sb.append("----------------------------------------");
        return sb.toString();
    }

    @PrePersist
    @PreUpdate
    public void validateAccount() {
        if (!isAccountIdValid()) {
            throw new IllegalStateException("Invalid Account ID format");
        }
        if (!isActiveStatusValid()) {
            throw new IllegalStateException("Account status must be Y (active) or N (inactive)");
        }
        if (this.currentBalance == null) {
            throw new IllegalStateException("Invalid current balance format");
        }
        if (!isCreditLimitValid()) {
            throw new IllegalStateException("Credit limit must be a valid positive amount");
        }
        if (!isCashCreditLimitValid()) {
            throw new IllegalStateException("Cash credit limit must be a valid positive amount");
        }
        if (!isOpenDateValid()) {
            throw new IllegalStateException("Invalid account open date");
        }
        if (!isExpirationDateValid()) {
            throw new IllegalStateException("Invalid expiration date");
        }
        if (!isReissueDateValid()) {
            throw new IllegalStateException("Invalid reissue date");
        }
        if (this.currentCycleCredit == null) {
            throw new IllegalStateException("Invalid current cycle credit format");
        }
        if (this.currentCycleDebit == null) {
            throw new IllegalStateException("Invalid current cycle debit format");
        }
    }
}
