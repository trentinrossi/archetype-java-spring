package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Credit card transaction records containing transaction details, amounts, dates, and status information
 * Entity represents individual credit card transactions with complete details
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Transaction.TransactionId.class)
public class Transaction {
    
    @Id
    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;
    
    @Id
    @Column(name = "transaction_id", length = 16, nullable = false)
    private String transactionId;
    
    @Column(name = "transaction_type", length = 20, nullable = false)
    private String transactionType;
    
    @Column(name = "transaction_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal transactionAmount;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;
    
    @Column(name = "post_date")
    private LocalDate postDate;
    
    @Column(name = "merchant_name", length = 100)
    private String merchantName;
    
    @Column(name = "merchant_category", length = 50)
    private String merchantCategory;
    
    @Column(name = "merchant_id", length = 20)
    private String merchantId;
    
    @Column(name = "merchant_city", length = 50)
    private String merchantCity;
    
    @Column(name = "merchant_state", length = 2)
    private String merchantState;
    
    @Column(name = "merchant_country", length = 3)
    private String merchantCountry;
    
    @Column(name = "merchant_zip", length = 10)
    private String merchantZip;
    
    @Column(name = "authorization_code", length = 20)
    private String authorizationCode;
    
    @Column(name = "authorization_date")
    private LocalDate authorizationDate;
    
    @Column(name = "authorization_amount", precision = 15, scale = 2)
    private BigDecimal authorizationAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false, length = 20)
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;
    
    @Column(name = "currency_code", length = 3)
    private String currencyCode = "USD";
    
    @Column(name = "original_amount", precision = 15, scale = 2)
    private BigDecimal originalAmount;
    
    @Column(name = "original_currency", length = 3)
    private String originalCurrency;
    
    @Column(name = "exchange_rate", precision = 10, scale = 6)
    private BigDecimal exchangeRate;
    
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "is_international")
    private Boolean isInternational = false;
    
    @Column(name = "is_recurring")
    private Boolean isRecurring = false;
    
    @Column(name = "is_disputed")
    private Boolean isDisputed = false;
    
    @Column(name = "dispute_date")
    private LocalDate disputeDate;
    
    @Column(name = "dispute_reason", length = 255)
    private String disputeReason;
    
    @Column(name = "is_reversed")
    private Boolean isReversed = false;
    
    @Column(name = "reversal_date")
    private LocalDate reversalDate;
    
    @Column(name = "reversal_reason", length = 255)
    private String reversalReason;
    
    @Column(name = "reward_points_earned")
    private Integer rewardPointsEarned = 0;
    
    @Column(name = "cashback_amount", precision = 10, scale = 2)
    private BigDecimal cashbackAmount = BigDecimal.ZERO;
    
    @Column(name = "fee_amount", precision = 10, scale = 2)
    private BigDecimal feeAmount = BigDecimal.ZERO;
    
    @Column(name = "interest_amount", precision = 10, scale = 2)
    private BigDecimal interestAmount = BigDecimal.ZERO;
    
    @Column(name = "statement_date")
    private LocalDate statementDate;
    
    @Column(name = "billing_cycle", length = 6)
    private String billingCycle;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Composite key class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionId implements Serializable {
        private String cardNumber;
        private String transactionId;
    }
    
    // Helper methods
    public boolean isPending() {
        return TransactionStatus.PENDING.equals(transactionStatus);
    }
    
    public boolean isPosted() {
        return TransactionStatus.POSTED.equals(transactionStatus);
    }
    
    public boolean isAuthorized() {
        return TransactionStatus.AUTHORIZED.equals(transactionStatus);
    }
    
    public boolean canBeDisputed() {
        return isPosted() && !isDisputed && !isReversed;
    }
    
    public boolean canBeReversed() {
        return (isPending() || isPosted()) && !isReversed;
    }
    
    public BigDecimal getTotalAmount() {
        BigDecimal total = transactionAmount;
        if (feeAmount != null) {
            total = total.add(feeAmount);
        }
        if (interestAmount != null) {
            total = total.add(interestAmount);
        }
        return total;
    }
    
    public boolean isPurchase() {
        return "PURCHASE".equalsIgnoreCase(transactionType);
    }
    
    public boolean isCashAdvance() {
        return "CASH_ADVANCE".equalsIgnoreCase(transactionType);
    }
    
    public boolean isPayment() {
        return "PAYMENT".equalsIgnoreCase(transactionType);
    }
    
    public boolean isRefund() {
        return "REFUND".equalsIgnoreCase(transactionType);
    }
    
    public boolean isFee() {
        return "FEE".equalsIgnoreCase(transactionType);
    }
    
    public boolean isInterest() {
        return "INTEREST".equalsIgnoreCase(transactionType);
    }
}
