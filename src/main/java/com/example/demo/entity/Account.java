package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Account number must be 11 digits")
    private String acctId;

    @Column(name = "acct_active_status", nullable = false, length = 1)
    @NotNull(message = "Account status must be Y (active) or N (inactive)")
    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    private String acctActiveStatus;

    @Column(name = "acct_curr_bal", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Invalid current balance format")
    private BigDecimal acctCurrBal;

    @Column(name = "acct_credit_limit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Credit limit must be a valid positive amount")
    @DecimalMin(value = "0.00", message = "Credit limit must be a valid positive amount")
    private BigDecimal acctCreditLimit;

    @Column(name = "acct_cash_credit_limit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Cash credit limit must be a valid positive amount")
    @DecimalMin(value = "0.00", message = "Cash credit limit must be a valid positive amount")
    private BigDecimal acctCashCreditLimit;

    @Column(name = "acct_open_date", nullable = false, length = 8)
    @NotNull(message = "Invalid account open date")
    private String acctOpenDate;

    @Column(name = "acct_expiration_date", nullable = false, length = 8)
    @NotNull(message = "Invalid expiration date")
    private String acctExpirationDate;

    @Column(name = "acct_reissue_date", length = 8)
    private String acctReissueDate;

    @Column(name = "acct_curr_cyc_credit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Invalid current cycle credit format")
    private BigDecimal acctCurrCycCredit;

    @Column(name = "acct_curr_cyc_debit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Invalid current cycle debit format")
    private BigDecimal acctCurrCycDebit;

    @Column(name = "acct_group_id", length = 10)
    private String acctGroupId;

    @Column(name = "account_id", nullable = false, length = 11)
    @NotNull(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "Account number must be 11 digits")
    private String accountId;

    @Column(name = "active_status", nullable = false, length = 1)
    @NotNull(message = "Account status must be Y (active) or N (inactive)")
    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    private String activeStatus;

    @Column(name = "current_balance", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Invalid current balance format")
    private BigDecimal currentBalance;

    @Column(name = "credit_limit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Credit limit must be a valid positive amount")
    @DecimalMin(value = "0.00", message = "Credit limit must be a valid positive amount")
    private BigDecimal creditLimit;

    @Column(name = "cash_credit_limit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Cash credit limit must be a valid positive amount")
    @DecimalMin(value = "0.00", message = "Cash credit limit must be a valid positive amount")
    private BigDecimal cashCreditLimit;

    @Column(name = "open_date", nullable = false, length = 8)
    @NotNull(message = "Invalid account open date")
    private String openDate;

    @Column(name = "expiration_date", nullable = false, length = 8)
    @NotNull(message = "Invalid expiration date")
    private String expirationDate;

    @Column(name = "reissue_date", nullable = false, length = 8)
    @NotNull(message = "Invalid reissue date")
    private String reissueDate;

    @Column(name = "current_cycle_credit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Invalid current cycle credit format")
    private BigDecimal currentCycleCredit;

    @Column(name = "current_cycle_debit", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Invalid current cycle debit format")
    private BigDecimal currentCycleDebit;

    @Column(name = "group_id", length = 10)
    private String groupId;

    @Column(name = "account_status", nullable = false, length = 1)
    @NotNull(message = "Account status must be Y (active) or N (inactive)")
    @Pattern(regexp = "^[YN]$", message = "Account status must be Y (active) or N (inactive)")
    private String accountStatus;

    @Column(name = "account_data", nullable = false, length = 289)
    @NotNull
    private String accountData;

    @Column(name = "xref_acct_id", nullable = false, length = 11)
    @NotNull(message = "Invalid Account ID format")
    @Pattern(regexp = "^[0-9]{11}$", message = "Invalid Account ID format")
    private String xrefAcctId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(String accountId, String activeStatus, BigDecimal currentBalance, BigDecimal creditLimit, BigDecimal cashCreditLimit) {
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
    public void validateAccount() {
        validateAccountId(this.accountId);
        validateAccountId(this.acctId);
        validateAccountId(this.xrefAcctId);
        validateActiveStatus(this.activeStatus);
        validateActiveStatus(this.acctActiveStatus);
        validateActiveStatus(this.accountStatus);
        validateCreditLimit(this.creditLimit);
        validateCreditLimit(this.acctCreditLimit);
        validateCashCreditLimit(this.cashCreditLimit);
        validateCashCreditLimit(this.acctCashCreditLimit);
        validateOpenDate(this.openDate);
        validateOpenDate(this.acctOpenDate);
        validateExpirationDate(this.expirationDate);
        validateExpirationDate(this.acctExpirationDate);
        if (this.reissueDate != null && !this.reissueDate.isEmpty()) {
            validateReissueDate(this.reissueDate);
        }
        if (this.acctReissueDate != null && !this.acctReissueDate.isEmpty()) {
            validateReissueDate(this.acctReissueDate);
        }
    }

    private void validateAccountId(String accountIdValue) {
        if (accountIdValue == null || accountIdValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (!accountIdValue.matches("^[0-9]{11}$")) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
        if (accountIdValue.equals("00000000000")) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
    }

    private void validateActiveStatus(String status) {
        if (status == null || (!status.equals("Y") && !status.equals("N"))) {
            throw new IllegalArgumentException("Account status must be Y (active) or N (inactive)");
        }
    }

    private void validateCreditLimit(BigDecimal limit) {
        if (limit == null || limit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Credit limit must be a valid positive amount");
        }
    }

    private void validateCashCreditLimit(BigDecimal limit) {
        if (limit == null || limit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cash credit limit must be a valid positive amount");
        }
    }

    private void validateOpenDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid account open date");
        }
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid account open date");
        }
        LocalDate openDateParsed = parseDate(date);
        if (openDateParsed.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid account open date");
        }
    }

    private void validateExpirationDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid expiration date");
        }
    }

    private void validateReissueDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid reissue date");
        }
        if (!isValidDate(date)) {
            throw new IllegalArgumentException("Invalid reissue date");
        }
    }

    private boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 8) {
            return false;
        }
        try {
            int year = Integer.parseInt(dateStr.substring(0, 4));
            int month = Integer.parseInt(dateStr.substring(4, 6));
            int day = Integer.parseInt(dateStr.substring(6, 8));
            
            if (year < 1900 || year > 9999) {
                return false;
            }
            if (month < 1 || month > 12) {
                return false;
            }
            if (day < 1 || day > 31) {
                return false;
            }
            
            LocalDate.of(year, month, day);
            return true;
        } catch (NumberFormatException | DateTimeParseException e) {
            return false;
        }
    }

    private LocalDate parseDate(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(4, 6));
        int day = Integer.parseInt(dateStr.substring(6, 8));
        return LocalDate.of(year, month, day);
    }

    public boolean isActive() {
        return "Y".equals(this.activeStatus) || "Y".equals(this.acctActiveStatus) || "Y".equals(this.accountStatus);
    }

    public boolean hasBalanceToPay() {
        BigDecimal balance = this.currentBalance != null ? this.currentBalance : this.acctCurrBal;
        return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
    }

    public void updateBalanceWithInterest(BigDecimal totalInterest) {
        if (totalInterest != null && totalInterest.compareTo(BigDecimal.ZERO) > 0) {
            this.currentBalance = this.currentBalance.add(totalInterest);
            this.acctCurrBal = this.acctCurrBal.add(totalInterest);
        }
    }

    public void resetCycleAmounts() {
        this.currentCycleCredit = BigDecimal.ZERO;
        this.acctCurrCycCredit = BigDecimal.ZERO;
        this.currentCycleDebit = BigDecimal.ZERO;
        this.acctCurrCycDebit = BigDecimal.ZERO;
    }

    public BigDecimal calculateMonthlyInterest(BigDecimal categoryBalance, BigDecimal annualRate) {
        if (categoryBalance == null || annualRate == null || annualRate.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("1200"), 6, BigDecimal.ROUND_HALF_UP);
        return categoryBalance.multiply(monthlyRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getAccountDisplayData() {
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
        sb.append("----------------------------------------\n");
        return sb.toString();
    }

    public String formatAccountData() {
        return String.format("ACCT-ID: %s | STATUS: %s | BAL: %s | CREDIT: %s | CASH: %s | OPEN: %s | EXP: %s | REISSUE: %s | CYC-CR: %s | CYC-DR: %s | GROUP: %s",
                this.acctId,
                this.acctActiveStatus,
                this.acctCurrBal,
                this.acctCreditLimit,
                this.acctCashCreditLimit,
                this.acctOpenDate,
                this.acctExpirationDate,
                this.acctReissueDate,
                this.acctCurrCycCredit,
                this.acctCurrCycDebit,
                this.acctGroupId);
    }
}
