package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.AccountStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account master data containing account balances, credit limits, dates, and status information
 * Entity represents credit card account with financial details and status
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "account_id", length = 11, nullable = false)
    private String accountId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(name = "account_number", length = 20, unique = true)
    private String accountNumber;
    
    @Column(name = "account_type", length = 20, nullable = false)
    private String accountType;
    
    @Column(name = "current_balance", precision = 15, scale = 2, nullable = false)
    private BigDecimal currentBalance = BigDecimal.ZERO;
    
    @Column(name = "available_balance", precision = 15, scale = 2)
    private BigDecimal availableBalance = BigDecimal.ZERO;
    
    @Column(name = "credit_limit", precision = 15, scale = 2, nullable = false)
    private BigDecimal creditLimit = BigDecimal.ZERO;
    
    @Column(name = "cash_advance_limit", precision = 15, scale = 2)
    private BigDecimal cashAdvanceLimit = BigDecimal.ZERO;
    
    @Column(name = "minimum_payment_due", precision = 15, scale = 2)
    private BigDecimal minimumPaymentDue = BigDecimal.ZERO;
    
    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;
    
    @Column(name = "last_payment_amount", precision = 15, scale = 2)
    private BigDecimal lastPaymentAmount;
    
    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;
    
    @Column(name = "last_statement_balance", precision = 15, scale = 2)
    private BigDecimal lastStatementBalance;
    
    @Column(name = "last_statement_date")
    private LocalDate lastStatementDate;
    
    @Column(name = "next_statement_date")
    private LocalDate nextStatementDate;
    
    @Column(name = "account_open_date", nullable = false)
    private LocalDate accountOpenDate;
    
    @Column(name = "account_close_date")
    private LocalDate accountCloseDate;
    
    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;
    
    @Column(name = "annual_fee", precision = 10, scale = 2)
    private BigDecimal annualFee;
    
    @Column(name = "late_fee", precision = 10, scale = 2)
    private BigDecimal lateFee;
    
    @Column(name = "overlimit_fee", precision = 10, scale = 2)
    private BigDecimal overlimitFee;
    
    @Column(name = "days_delinquent")
    private Integer daysDelinquent = 0;
    
    @Column(name = "cycle_to_date_purchases", precision = 15, scale = 2)
    private BigDecimal cycleToDatePurchases = BigDecimal.ZERO;
    
    @Column(name = "cycle_to_date_cash_advances", precision = 15, scale = 2)
    private BigDecimal cycleToDateCashAdvances = BigDecimal.ZERO;
    
    @Column(name = "cycle_to_date_payments", precision = 15, scale = 2)
    private BigDecimal cycleToDatePayments = BigDecimal.ZERO;
    
    @Column(name = "year_to_date_purchases", precision = 15, scale = 2)
    private BigDecimal yearToDatePurchases = BigDecimal.ZERO;
    
    @Column(name = "year_to_date_cash_advances", precision = 15, scale = 2)
    private BigDecimal yearToDateCashAdvances = BigDecimal.ZERO;
    
    @Column(name = "year_to_date_interest", precision = 15, scale = 2)
    private BigDecimal yearToDateInterest = BigDecimal.ZERO;
    
    @Column(name = "year_to_date_fees", precision = 15, scale = 2)
    private BigDecimal yearToDateFees = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @Column(name = "reward_points")
    private Integer rewardPoints = 0;
    
    @Column(name = "reward_points_balance")
    private Integer rewardPointsBalance = 0;
    
    @Column(name = "autopay_enabled")
    private Boolean autopayEnabled = false;
    
    @Column(name = "paperless_statements")
    private Boolean paperlessStatements = false;
    
    @Column(name = "fraud_alert")
    private Boolean fraudAlert = false;
    
    @Column(name = "temporary_hold")
    private Boolean temporaryHold = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardCrossReference> cardCrossReferences;
    
    // Helper methods
    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(currentBalance);
    }
    
    public boolean isOverLimit() {
        return currentBalance.compareTo(creditLimit) > 0;
    }
    
    public boolean isDelinquent() {
        return daysDelinquent != null && daysDelinquent > 0;
    }
    
    public boolean isPastDue() {
        return paymentDueDate != null && LocalDate.now().isAfter(paymentDueDate) 
               && minimumPaymentDue.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public BigDecimal getCreditUtilization() {
        if (creditLimit.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentBalance.divide(creditLimit, 4, java.math.RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
    }
    
    public boolean canTransact() {
        return AccountStatus.ACTIVE.equals(status) && !fraudAlert && !temporaryHold;
    }
    
    public boolean hasAvailableCredit(BigDecimal amount) {
        return getAvailableCredit().compareTo(amount) >= 0;
    }
    
    public void applyPayment(BigDecimal amount) {
        this.currentBalance = this.currentBalance.subtract(amount);
        this.availableBalance = this.availableBalance.add(amount);
        this.lastPaymentAmount = amount;
        this.lastPaymentDate = LocalDate.now();
        this.cycleToDatePayments = this.cycleToDatePayments.add(amount);
    }
    
    public void applyCharge(BigDecimal amount) {
        this.currentBalance = this.currentBalance.add(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
        this.cycleToDatePurchases = this.cycleToDatePurchases.add(amount);
        this.yearToDatePurchases = this.yearToDatePurchases.add(amount);
    }
    
    public void applyInterest(BigDecimal interestAmount) {
        this.currentBalance = this.currentBalance.add(interestAmount);
        this.yearToDateInterest = this.yearToDateInterest.add(interestAmount);
    }
    
    public void applyFee(BigDecimal feeAmount) {
        this.currentBalance = this.currentBalance.add(feeAmount);
        this.yearToDateFees = this.yearToDateFees.add(feeAmount);
    }
}
