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

    @Column(name = "customer_id", nullable = false, length = 9)
    private Long customerId;

    @Column(name = "account_status", nullable = false, length = 1)
    private String accountStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CardCrossReference cardCrossReference;

    public Account(Long accountId, String activeStatus, BigDecimal currentBalance, 
                   BigDecimal creditLimit, BigDecimal cashCreditLimit, LocalDate openDate,
                   LocalDate expirationDate, LocalDate reissueDate, BigDecimal currentCycleCredit,
                   BigDecimal currentCycleDebit, Long customerId, String accountStatus) {
        this.accountId = accountId;
        this.activeStatus = activeStatus;
        this.currentBalance = currentBalance;
        this.creditLimit = creditLimit;
        this.cashCreditLimit = cashCreditLimit;
        this.openDate = openDate;
        this.expirationDate = expirationDate;
        this.reissueDate = reissueDate;
        this.currentCycleCredit = currentCycleCredit;
        this.currentCycleDebit = currentCycleDebit;
        this.customerId = customerId;
        this.accountStatus = accountStatus;
    }

    @PrePersist
    @PreUpdate
    public void validateAccountData() {
        validateAccountId();
        validateActiveStatus();
        validateBalanceFields();
        validateDateFields();
        validateAccountStatus();
    }

    private void validateAccountId() {
        if (accountId == null) {
            throw new IllegalArgumentException("Account number is required");
        }
        
        String accountIdStr = String.valueOf(accountId);
        
        if (accountIdStr.length() != 11) {
            throw new IllegalArgumentException("Account number must be 11 digits");
        }
        
        if (accountId == 0L) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
    }

    private void validateActiveStatus() {
        if (activeStatus == null || activeStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Active status is required");
        }
        
        if (!activeStatus.equals("Y") && !activeStatus.equals("N")) {
            throw new IllegalArgumentException("Account status must be Y or N");
        }
    }

    private void validateBalanceFields() {
        if (currentBalance == null || currentBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current balance must be a valid positive number");
        }
        
        if (creditLimit == null || creditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Credit limit must be a valid positive number");
        }
        
        if (cashCreditLimit == null || cashCreditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cash credit limit must be a valid positive number");
        }
        
        if (currentCycleCredit == null || currentCycleCredit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current cycle credit must be a valid positive number");
        }
        
        if (currentCycleDebit == null || currentCycleDebit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current cycle debit must be a valid positive number");
        }
    }

    private void validateDateFields() {
        if (openDate == null) {
            throw new IllegalArgumentException("Invalid open date format or value");
        }
        
        if (expirationDate == null) {
            throw new IllegalArgumentException("Invalid expiration date format or value");
        }
        
        if (reissueDate == null) {
            throw new IllegalArgumentException("Invalid reissue date format or value");
        }
        
        int openYear = openDate.getYear();
        int expirationYear = expirationDate.getYear();
        int reissueYear = reissueDate.getYear();
        
        if (openYear < 1900 || openYear > 2099) {
            throw new IllegalArgumentException("Invalid open date format or value");
        }
        
        if (expirationYear < 1900 || expirationYear > 2099) {
            throw new IllegalArgumentException("Invalid expiration date format or value");
        }
        
        if (reissueYear < 1900 || reissueYear > 2099) {
            throw new IllegalArgumentException("Invalid reissue date format or value");
        }
    }

    private void validateAccountStatus() {
        if (accountStatus == null || accountStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Account status is required");
        }
    }

    public boolean isActive() {
        return "Y".equals(activeStatus);
    }

    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(currentBalance);
    }

    public BigDecimal getAvailableCashCredit() {
        return cashCreditLimit.subtract(currentBalance);
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }

    public boolean hasGroupAssignment() {
        return groupId != null && !groupId.trim().isEmpty();
    }

    public BigDecimal getCurrentCycleNetActivity() {
        return currentCycleCredit.subtract(currentCycleDebit);
    }

    public boolean isAccountNumberValid() {
        if (accountId == null) {
            return false;
        }
        
        String accountIdStr = String.valueOf(accountId);
        
        if (accountIdStr.length() != 11) {
            return false;
        }
        
        if (accountId == 0L) {
            return false;
        }
        
        return true;
    }

    public void updateBalance(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        
        BigDecimal newBalance = currentBalance.add(amount);
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        this.currentBalance = newBalance;
    }

    public void updateCycleCredit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current cycle credit must be a valid positive number");
        }
        
        this.currentCycleCredit = amount;
    }

    public void updateCycleDebit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current cycle debit must be a valid positive number");
        }
        
        this.currentCycleDebit = amount;
    }

    public void setActiveStatus(String status) {
        if (status == null || (!status.equals("Y") && !status.equals("N"))) {
            throw new IllegalArgumentException("Account status must be Y or N");
        }
        this.activeStatus = status;
    }

    public void assignToGroup(String groupId) {
        if (groupId != null && groupId.length() > 10) {
            throw new IllegalArgumentException("Group ID cannot exceed 10 characters");
        }
        this.groupId = groupId;
    }

    public void removeFromGroup() {
        this.groupId = null;
    }
}
