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
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false, length = 16)
    private String transactionId;

    @Column(name = "tran_id", nullable = false, unique = true)
    private Long tranId;

    @Column(name = "tran_type_cd", nullable = false, length = 2)
    private Integer tranTypeCd;

    @Column(name = "tran_cat_cd", nullable = false, length = 4)
    private Integer tranCatCd;

    @Column(name = "tran_source", nullable = false, length = 10)
    private String tranSource;

    @Column(name = "tran_desc", nullable = false, length = 60)
    private String tranDesc;

    @Column(name = "tran_amt", nullable = false, precision = 11, scale = 2)
    private BigDecimal tranAmt;

    @Column(name = "tran_card_num", nullable = false, length = 16)
    private String tranCardNum;

    @Column(name = "tran_merchant_id", nullable = false, length = 9)
    private String tranMerchantId;

    @Column(name = "tran_merchant_name", nullable = false, length = 30)
    private String tranMerchantName;

    @Column(name = "tran_merchant_city", nullable = false, length = 25)
    private String tranMerchantCity;

    @Column(name = "tran_merchant_zip", nullable = false, length = 10)
    private String tranMerchantZip;

    @Column(name = "tran_orig_ts", nullable = false)
    private LocalDate tranOrigTs;

    @Column(name = "tran_proc_ts", nullable = false)
    private LocalDate tranProcTs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "card_num")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "acct_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", referencedColumnName = "merchant_id")
    private Merchant merchant;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Transaction(String transactionId, Long tranId, BigDecimal tranAmt) {
        this.transactionId = transactionId;
        this.tranId = tranId;
        this.tranAmt = tranAmt;
    }

    /**
     * Format transaction date as MM/DD/YY
     */
    public String getFormattedTransactionDate() {
        if (tranOrigTs != null) {
            int year = tranOrigTs.getYear() % 100;
            int month = tranOrigTs.getMonthValue();
            int day = tranOrigTs.getDayOfMonth();
            return String.format("%02d/%02d/%02d", month, day, year);
        }
        return "";
    }

    /**
     * Format transaction amount with sign and decimal places
     */
    public String getFormattedTransactionAmount() {
        if (tranAmt != null) {
            String sign = tranAmt.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
            return sign + String.format("%.2f", tranAmt);
        }
        return "";
    }

    /**
     * Validate transaction ID format
     */
    public boolean isValidTransactionId() {
        return transactionId != null && 
               transactionId.length() == 16 && 
               !transactionId.trim().isEmpty();
    }

    /**
     * Validate amount is within acceptable range
     */
    public boolean isValidAmount() {
        if (tranAmt == null) {
            return false;
        }
        BigDecimal maxValue = new BigDecimal("99999999.99");
        BigDecimal minValue = new BigDecimal("-99999999.99");
        return tranAmt.compareTo(minValue) >= 0 && tranAmt.compareTo(maxValue) <= 0;
    }

    /**
     * Get transaction display line for list view
     */
    public String getTransactionDisplayLine() {
        return String.format("%s %s %s %s", 
            transactionId, 
            getFormattedTransactionDate(), 
            tranDesc != null ? tranDesc.substring(0, Math.min(26, tranDesc.length())) : "", 
            getFormattedTransactionAmount());
    }

    /**
     * Validate all required fields before persist/update
     */
    @PrePersist
    @PreUpdate
    public void validateTransaction() {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalStateException("Transaction ID cannot be empty");
        }
        
        if (transactionId.length() != 16) {
            throw new IllegalStateException("Transaction ID must be 16 characters");
        }
        
        if (tranTypeCd == null) {
            throw new IllegalStateException("Transaction Type Code must be entered");
        }
        
        if (tranCatCd == null) {
            throw new IllegalStateException("Transaction Category Code must be entered");
        }
        
        if (tranSource == null || tranSource.trim().isEmpty()) {
            throw new IllegalStateException("Transaction Source must be entered");
        }
        
        if (tranDesc == null || tranDesc.trim().isEmpty()) {
            throw new IllegalStateException("Transaction Description must be entered");
        }
        
        if (tranAmt == null) {
            throw new IllegalStateException("Transaction Amount must be entered");
        }
        
        if (!isValidAmount()) {
            throw new IllegalStateException("Amount must be in format -99999999.99 to +99999999.99");
        }
        
        if (tranMerchantId == null || tranMerchantId.trim().isEmpty()) {
            throw new IllegalStateException("Merchant ID must be entered");
        }
        
        if (tranMerchantName == null || tranMerchantName.trim().isEmpty()) {
            throw new IllegalStateException("Merchant Name must be entered");
        }
        
        if (tranMerchantCity == null || tranMerchantCity.trim().isEmpty()) {
            throw new IllegalStateException("Merchant City must be entered");
        }
        
        if (tranMerchantZip == null || tranMerchantZip.trim().isEmpty()) {
            throw new IllegalStateException("Merchant Zip must be entered");
        }
        
        if (tranOrigTs == null) {
            throw new IllegalStateException("Original Transaction Date must be entered");
        }
        
        if (tranProcTs == null) {
            throw new IllegalStateException("Processing Transaction Date must be entered");
        }
    }
}
