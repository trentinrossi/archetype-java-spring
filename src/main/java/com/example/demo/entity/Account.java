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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
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

    @Column(name = "acct_open_date", nullable = false)
    private LocalDate acctOpenDate;

    @Column(name = "acct_expiration_date", nullable = false)
    private LocalDate acctExpirationDate;

    @Column(name = "acct_reissue_date")
    private LocalDate acctReissueDate;

    @Column(name = "acct_curr_cyc_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycCredit;

    @Column(name = "acct_curr_cyc_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycDebit;

    @Column(name = "acct_group_id", length = 10)
    private String acctGroupId;

    @Column(name = "account_id", nullable = false, unique = true, length = 11)
    private Long accountId;

    @Column(name = "active_status", nullable = false, length = 1)
    private String activeStatus;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "cash_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashCreditLimit;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "reissue_date", nullable = false)
    private LocalDate reissueDate;

    @Column(name = "current_cycle_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleCredit;

    @Column(name = "current_cycle_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleDebit;

    @Column(name = "group_id", length = 10)
    private String groupId;

    @Column(name = "account_status", nullable = false, length = 1)
    private String accountStatus;

    @Column(name = "account_data", nullable = false, length = 289)
    private String accountData;

    @Column(name = "xref_acct_id", nullable = false, length = 11)
    private Long xrefAcctId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(Long accountId, String activeStatus, BigDecimal currentBalance, 
                   BigDecimal creditLimit, BigDecimal cashCreditLimit) {
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
        this.currentCycleCredit = BigDecimal.ZERO;
        this.acctCurrCycCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
        this.acctCurrCycDebit = BigDecimal.ZERO;
    }

    @PrePersist
    @PreUpdate
    public void validateAndSync() {
        validateAccountId();
        validateActiveStatus();
        validateBalances();
        validateCreditLimits();
        validateDates();
        validateCycleAmounts();
        synchronizeFields();
        buildAccountData();
    }

    private void validateAccountId() {
        if (accountId == null || acctId == null || xrefAcctId == null) {
            throw new IllegalArgumentException("Account number is required");
        }
        
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("Account number must be 11 digits");
        }
        
        if (!accountIdStr.matches("\\d{11}")) {
            throw new IllegalArgumentException("Account number must contain only numbers");
        }
        
        if (accountId == 0L) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
    }

    private void validateActiveStatus() {
        if (activeStatus == null || activeStatus.isBlank()) {
            throw new IllegalArgumentException("Account status must be Y (active) or N (inactive)");
        }
        
        if (!activeStatus.equals("Y") && !activeStatus.equals("N")) {
            throw new IllegalArgumentException("Account status must be Y (active) or N (inactive)");
        }
    }

    private void validateBalances() {
        if (currentBalance == null || acctCurrBal == null) {
            throw new IllegalArgumentException("Invalid current balance format");
        }
        
        if (currentBalance.scale() > 2 || acctCurrBal.scale() > 2) {
            throw new IllegalArgumentException("Invalid current balance format");
        }
    }

    private void validateCreditLimits() {
        if (creditLimit == null || acctCreditLimit == null) {
            throw new IllegalArgumentException("Credit limit must be a valid positive amount");
        }
        
        if (creditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Credit limit must be a valid positive amount");
        }
        
        if (cashCreditLimit == null || acctCashCreditLimit == null) {
            throw new IllegalArgumentException("Cash credit limit must be a valid positive amount");
        }
        
        if (cashCreditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cash credit limit must be a valid positive amount");
        }
    }

    private void validateDates() {
        if (openDate == null || acctOpenDate == null) {
            throw new IllegalArgumentException("Invalid account open date");
        }
        
        if (openDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid account open date");
        }
        
        if (expirationDate == null || acctExpirationDate == null) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
    }

    private void validateCycleAmounts() {
        if (currentCycleCredit == null || acctCurrCycCredit == null) {
            throw new IllegalArgumentException("Invalid current cycle credit format");
        }
        
        if (currentCycleDebit == null || acctCurrCycDebit == null) {
            throw new IllegalArgumentException("Invalid current cycle debit format");
        }
    }

    private void buildAccountData() {
        StringBuilder data = new StringBuilder();
        data.append("ACCT-ID:").append(acctId).append("|");
        data.append("ACCT-ACTIVE-STATUS:").append(acctActiveStatus).append("|");
        data.append("ACCT-CURR-BAL:").append(acctCurrBal).append("|");
        data.append("ACCT-CREDIT-LIMIT:").append(acctCreditLimit).append("|");
        data.append("ACCT-CASH-CREDIT-LIMIT:").append(acctCashCreditLimit).append("|");
        data.append("ACCT-OPEN-DATE:").append(acctOpenDate).append("|");
        data.append("ACCT-EXPIRATION-DATE:").append(acctExpirationDate).append("|");
        data.append("ACCT-REISSUE-DATE:").append(acctReissueDate != null ? acctReissueDate : "").append("|");
        data.append("ACCT-CURR-CYC-CREDIT:").append(acctCurrCycCredit).append("|");
        data.append("ACCT-CURR-CYC-DEBIT:").append(acctCurrCycDebit).append("|");
        data.append("ACCT-GROUP-ID:").append(acctGroupId != null ? acctGroupId : "");
        
        this.accountData = data.toString();
    }

    public boolean isActive() {
        return "Y".equals(activeStatus) || "A".equals(acctActiveStatus);
    }

    public boolean hasBalanceToPay() {
        return currentBalance != null && currentBalance.compareTo(BigDecimal.ZERO) > 0;
    }

    public void resetCycleAmounts() {
        this.currentCycleCredit = BigDecimal.ZERO;
        this.acctCurrCycCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
        this.acctCurrCycDebit = BigDecimal.ZERO;
    }

    public void updateBalanceWithInterest(BigDecimal interestAmount) {
        if (interestAmount != null) {
            this.currentBalance = this.currentBalance.add(interestAmount);
            this.acctCurrBal = this.acctCurrBal.add(interestAmount);
        }
    }

    public String getFormattedAccountId() {
        return String.format("%011d", accountId);
    }

    public void synchronizeFields() {
        this.acctId = this.accountId;
        this.xrefAcctId = this.accountId;
        this.acctActiveStatus = this.activeStatus;
        this.accountStatus = this.activeStatus;
        this.acctCurrBal = this.currentBalance;
        this.acctCreditLimit = this.creditLimit;
        this.acctCashCreditLimit = this.cashCreditLimit;
        this.acctOpenDate = this.openDate;
        this.acctExpirationDate = this.expirationDate;
        this.acctReissueDate = this.reissueDate;
        this.acctCurrCycCredit = this.currentCycleCredit;
        this.acctCurrCycDebit = this.currentCycleDebit;
        this.acctGroupId = this.groupId;
    }
}
