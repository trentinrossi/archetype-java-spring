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

    @Column(name = "xref_acct_id", nullable = false, length = 11)
    private Long xrefAcctId;

    @Column(name = "acct_active_status", nullable = false, length = 1)
    private String acctActiveStatus;

    @Column(name = "active_status", nullable = false, length = 1)
    private String activeStatus;

    @Column(name = "account_status", nullable = false, length = 1)
    private String accountStatus;

    @Column(name = "acct_curr_bal", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrBal;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "acct_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCreditLimit;

    @Column(name = "credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "acct_cash_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCashCreditLimit;

    @Column(name = "cash_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashCreditLimit;

    @Column(name = "acct_open_date", nullable = false)
    private LocalDate acctOpenDate;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "acct_expiraion_date", nullable = false)
    private LocalDate acctExpiraionDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "acct_reissue_date")
    private LocalDate acctReissueDate;

    @Column(name = "reissue_date", nullable = false)
    private LocalDate reissueDate;

    @Column(name = "acct_curr_cyc_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycCredit;

    @Column(name = "current_cycle_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleCredit;

    @Column(name = "acct_curr_cyc_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycDebit;

    @Column(name = "current_cycle_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleDebit;

    @Column(name = "acct_group_id", length = 10)
    private String acctGroupId;

    @Column(name = "group_id", length = 10)
    private String groupId;

    @Column(name = "account_data", nullable = false, length = 289)
    private String accountData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(Long accountId, String activeStatus, BigDecimal currentBalance, BigDecimal creditLimit, 
                   BigDecimal cashCreditLimit, LocalDate openDate, LocalDate expirationDate, LocalDate reissueDate,
                   BigDecimal currentCycleCredit, BigDecimal currentCycleDebit, String groupId) {
        this.accountId = accountId;
        this.acctId = accountId;
        this.xrefAcctId = accountId;
        this.activeStatus = activeStatus;
        this.acctActiveStatus = activeStatus;
        this.accountStatus = activeStatus;
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
        this.reissueDate = reissueDate;
        this.acctReissueDate = reissueDate;
        this.currentCycleCredit = currentCycleCredit;
        this.acctCurrCycCredit = currentCycleCredit;
        this.currentCycleDebit = currentCycleDebit;
        this.acctCurrCycDebit = currentCycleDebit;
        this.groupId = groupId;
        this.acctGroupId = groupId;
        this.accountData = buildAccountData();
    }

    @PrePersist
    @PreUpdate
    public void validateAccount() {
        validateAccountId();
        validateActiveStatus();
        validateBalances();
        validateCreditLimits();
        validateDates();
        validateCycleAmounts();
        syncAccountData();
    }

    private void validateAccountId() {
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Invalid account number. Must be 11 digits and not zero.");
        }
        
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("Account number must be 11 digits");
        }
        
        if (!accountIdStr.matches("\\d{11}")) {
            throw new IllegalArgumentException("Account number must contain only numbers");
        }
        
        if (accountIdStr.equals("00000000000")) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
    }

    private void validateActiveStatus() {
        if (activeStatus == null || activeStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Account status must be Y (active) or N (inactive)");
        }
        
        if (!activeStatus.equals("Y") && !activeStatus.equals("N") && 
            !activeStatus.equals("A") && !activeStatus.equals("I")) {
            throw new IllegalArgumentException("Account status must be Y (active) or N (inactive)");
        }
    }

    private void validateBalances() {
        if (currentBalance == null) {
            throw new IllegalArgumentException("Invalid current balance format");
        }
        
        if (currentBalance.scale() > 2) {
            throw new IllegalArgumentException("Invalid current balance format");
        }
    }

    private void validateCreditLimits() {
        if (creditLimit == null || creditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Credit limit must be a valid positive amount");
        }
        
        if (creditLimit.scale() > 2) {
            throw new IllegalArgumentException("Credit limit must be a valid positive amount");
        }
        
        if (cashCreditLimit == null || cashCreditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cash credit limit must be a valid positive amount");
        }
        
        if (cashCreditLimit.scale() > 2) {
            throw new IllegalArgumentException("Cash credit limit must be a valid positive amount");
        }
    }

    private void validateDates() {
        if (openDate == null) {
            throw new IllegalArgumentException("Invalid account open date");
        }
        
        if (openDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid account open date");
        }
        
        if (expirationDate == null) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
        
        validateDateFormat(expirationDate, "Invalid expiration date");
        
        if (reissueDate != null) {
            validateDateFormat(reissueDate, "Invalid reissue date");
        }
    }

    private void validateDateFormat(LocalDate date, String errorMessage) {
        if (date == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        
        if (year < 1000 || year > 9999) {
            throw new IllegalArgumentException(errorMessage);
        }
        
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(errorMessage);
        }
        
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateCycleAmounts() {
        if (currentCycleCredit == null || currentCycleCredit.scale() > 2) {
            throw new IllegalArgumentException("Invalid current cycle credit format");
        }
        
        if (currentCycleDebit == null || currentCycleDebit.scale() > 2) {
            throw new IllegalArgumentException("Invalid current cycle debit format");
        }
    }

    private void syncAccountData() {
        this.acctId = this.accountId;
        this.xrefAcctId = this.accountId;
        this.acctActiveStatus = this.activeStatus;
        this.accountStatus = this.activeStatus;
        this.acctCurrBal = this.currentBalance;
        this.acctCreditLimit = this.creditLimit;
        this.acctCashCreditLimit = this.cashCreditLimit;
        this.acctOpenDate = this.openDate;
        this.acctExpiraionDate = this.expirationDate;
        this.acctReissueDate = this.reissueDate;
        this.acctCurrCycCredit = this.currentCycleCredit;
        this.acctCurrCycDebit = this.currentCycleDebit;
        this.acctGroupId = this.groupId;
        this.accountData = buildAccountData();
    }

    private String buildAccountData() {
        StringBuilder data = new StringBuilder();
        data.append("ACCT-ID: ").append(accountId).append("|");
        data.append("ACTIVE-STATUS: ").append(activeStatus).append("|");
        data.append("CURR-BAL: ").append(currentBalance).append("|");
        data.append("CREDIT-LIMIT: ").append(creditLimit).append("|");
        data.append("CASH-CREDIT-LIMIT: ").append(cashCreditLimit).append("|");
        data.append("OPEN-DATE: ").append(openDate).append("|");
        data.append("EXPIRATION-DATE: ").append(expirationDate).append("|");
        data.append("REISSUE-DATE: ").append(reissueDate).append("|");
        data.append("CURR-CYC-CREDIT: ").append(currentCycleCredit).append("|");
        data.append("CURR-CYC-DEBIT: ").append(currentCycleDebit).append("|");
        data.append("GROUP-ID: ").append(groupId);
        return data.toString();
    }

    public void resetCycleAmounts() {
        this.currentCycleCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
        this.acctCurrCycCredit = BigDecimal.ZERO;
        this.acctCurrCycDebit = BigDecimal.ZERO;
    }

    public void updateBalanceWithInterest(BigDecimal interestAmount) {
        if (interestAmount != null) {
            this.currentBalance = this.currentBalance.add(interestAmount);
            this.acctCurrBal = this.currentBalance;
        }
    }

    public boolean isActive() {
        return "Y".equals(activeStatus) || "A".equals(activeStatus);
    }

    public boolean hasBalanceToPay() {
        return currentBalance != null && currentBalance.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getFormattedAccountId() {
        return String.format("%011d", accountId);
    }

    public String getFormattedOpenDate() {
        return openDate != null ? openDate.toString().replace("-", "") : "";
    }

    public String getFormattedExpirationDate() {
        return expirationDate != null ? expirationDate.toString().replace("-", "") : "";
    }

    public String getFormattedReissueDate() {
        return reissueDate != null ? reissueDate.toString().replace("-", "") : "";
    }

    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(currentBalance);
    }

    public BigDecimal getAvailableCashCredit() {
        return cashCreditLimit.subtract(currentBalance);
    }

    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    public String getAccountSummary() {
        return String.format("Account %s - Status: %s - Balance: %s - Credit Limit: %s",
                getFormattedAccountId(), activeStatus, currentBalance, creditLimit);
    }
}
