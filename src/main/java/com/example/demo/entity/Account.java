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
    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "acct_id", nullable = false, length = 11)
    private Long acctId;

    @Column(name = "xref_acct_id", nullable = false, length = 11)
    private Long xrefAcctId;

    @Column(name = "active_status", nullable = false, length = 1)
    private String activeStatus;

    @Column(name = "acct_active_status", nullable = false, length = 1)
    private String acctActiveStatus;

    @Column(name = "account_status", nullable = false, length = 1)
    private String accountStatus;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "acct_curr_bal", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrBal;

    @Column(name = "credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "acct_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCreditLimit;

    @Column(name = "cash_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashCreditLimit;

    @Column(name = "acct_cash_credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCashCreditLimit;

    @Column(name = "open_date", nullable = false, length = 8)
    private String openDate;

    @Column(name = "acct_open_date", nullable = false, length = 8)
    private String acctOpenDate;

    @Column(name = "expiration_date", nullable = false, length = 8)
    private String expirationDate;

    @Column(name = "acct_expiraion_date", nullable = false, length = 8)
    private String acctExpiraionDate;

    @Column(name = "reissue_date", nullable = false, length = 8)
    private String reissueDate;

    @Column(name = "acct_reissue_date", nullable = true, length = 8)
    private String acctReissueDate;

    @Column(name = "current_cycle_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleCredit;

    @Column(name = "acct_curr_cyc_credit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycCredit;

    @Column(name = "current_cycle_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentCycleDebit;

    @Column(name = "acct_curr_cyc_debit", nullable = false, precision = 12, scale = 2)
    private BigDecimal acctCurrCycDebit;

    @Column(name = "group_id", nullable = true, length = 10)
    private String groupId;

    @Column(name = "acct_group_id", nullable = true, length = 10)
    private String acctGroupId;

    @Column(name = "account_data", nullable = false, length = 289)
    private String accountData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(Long accountId, String activeStatus, BigDecimal currentBalance, BigDecimal creditLimit, 
                   BigDecimal cashCreditLimit, String openDate, String expirationDate, String reissueDate,
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
        buildAccountData();
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
        validateDateFormat(openDate, "Invalid account open date");
        validateDateNotInFuture(openDate, "Invalid account open date");
        validateDateFormat(expirationDate, "Invalid expiration date");
        validateDateFormat(reissueDate, "Invalid reissue date");
    }

    private void validateDateFormat(String date, String errorMessage) {
        if (date == null || date.trim().isEmpty()) {
            return;
        }
        
        if (date.length() != 8) {
            throw new IllegalArgumentException(errorMessage);
        }
        
        if (!date.matches("\\d{8}")) {
            throw new IllegalArgumentException(errorMessage);
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            
            int year = parsedDate.getYear();
            int month = parsedDate.getMonthValue();
            int day = parsedDate.getDayOfMonth();
            
            if (year < 1000 || year > 9999) {
                throw new IllegalArgumentException(errorMessage);
            }
            
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException(errorMessage);
            }
            
            if (day < 1 || day > parsedDate.lengthOfMonth()) {
                throw new IllegalArgumentException(errorMessage);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateDateNotInFuture(String date, String errorMessage) {
        if (date == null || date.trim().isEmpty()) {
            return;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            
            if (parsedDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(errorMessage);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateCycleAmounts() {
        if (currentCycleCredit == null) {
            throw new IllegalArgumentException("Invalid current cycle credit format");
        }
        
        if (currentCycleCredit.scale() > 2) {
            throw new IllegalArgumentException("Invalid current cycle credit format");
        }
        
        if (currentCycleDebit == null) {
            throw new IllegalArgumentException("Invalid current cycle debit format");
        }
        
        if (currentCycleDebit.scale() > 2) {
            throw new IllegalArgumentException("Invalid current cycle debit format");
        }
    }

    private String buildAccountData() {
        StringBuilder data = new StringBuilder();
        data.append("ACCT-ID:").append(accountId).append("|");
        data.append("STATUS:").append(activeStatus).append("|");
        data.append("CURR-BAL:").append(currentBalance).append("|");
        data.append("CREDIT-LIMIT:").append(creditLimit).append("|");
        data.append("CASH-LIMIT:").append(cashCreditLimit).append("|");
        data.append("OPEN-DATE:").append(openDate).append("|");
        data.append("EXP-DATE:").append(expirationDate).append("|");
        data.append("REISSUE-DATE:").append(reissueDate != null ? reissueDate : "").append("|");
        data.append("CYC-CREDIT:").append(currentCycleCredit).append("|");
        data.append("CYC-DEBIT:").append(currentCycleDebit).append("|");
        data.append("GROUP-ID:").append(groupId != null ? groupId : "");
        
        this.accountData = data.toString();
        return this.accountData;
    }

    public void displayAccountInformation() {
        System.out.println("========================================");
        System.out.println("ACCT-ID: " + acctId);
        System.out.println("ACCT-ACTIVE-STATUS: " + acctActiveStatus);
        System.out.println("ACCT-CURR-BAL: " + acctCurrBal);
        System.out.println("ACCT-CREDIT-LIMIT: " + acctCreditLimit);
        System.out.println("ACCT-CASH-CREDIT-LIMIT: " + acctCashCreditLimit);
        System.out.println("ACCT-OPEN-DATE: " + acctOpenDate);
        System.out.println("ACCT-EXPIRAION-DATE: " + acctExpiraionDate);
        System.out.println("ACCT-REISSUE-DATE: " + (acctReissueDate != null ? acctReissueDate : "N/A"));
        System.out.println("ACCT-CURR-CYC-CREDIT: " + acctCurrCycCredit);
        System.out.println("ACCT-CURR-CYC-DEBIT: " + acctCurrCycDebit);
        System.out.println("ACCT-GROUP-ID: " + (acctGroupId != null ? acctGroupId : "N/A"));
        System.out.println("========================================");
    }

    public boolean isActive() {
        return "Y".equals(activeStatus) || "A".equals(activeStatus);
    }

    public boolean hasBalanceToPay() {
        return currentBalance != null && currentBalance.compareTo(BigDecimal.ZERO) > 0;
    }

    public void updateBalanceWithInterest(BigDecimal interestAmount) {
        if (interestAmount != null && interestAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.currentBalance = this.currentBalance.add(interestAmount);
            this.acctCurrBal = this.currentBalance;
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
        
        BigDecimal divisor = new BigDecimal("1200");
        return currentBalance.multiply(annualRate).divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
    }

    public boolean isAccountIdValid() {
        if (accountId == null || accountId == 0) {
            return false;
        }
        
        String accountIdStr = String.valueOf(accountId);
        if (accountIdStr.length() != 11) {
            return false;
        }
        
        if (!accountIdStr.matches("\\d{11}")) {
            return false;
        }
        
        if (accountIdStr.equals("00000000000")) {
            return false;
        }
        
        return true;
    }

    public String getFormattedBalance() {
        return String.format("$%,.2f", currentBalance);
    }

    public String getFormattedCreditLimit() {
        return String.format("$%,.2f", creditLimit);
    }

    public String getFormattedCashCreditLimit() {
        return String.format("$%,.2f", cashCreditLimit);
    }

    public LocalDate getOpenDateAsLocalDate() {
        if (openDate == null || openDate.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(openDate, formatter);
    }

    public LocalDate getExpirationDateAsLocalDate() {
        if (expirationDate == null || expirationDate.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(expirationDate, formatter);
    }

    public LocalDate getReissueDateAsLocalDate() {
        if (reissueDate == null || reissueDate.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(reissueDate, formatter);
    }
}
