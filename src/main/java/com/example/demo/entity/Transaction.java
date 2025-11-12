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
    @Column(name = "tran_id", nullable = false, length = 16)
    private String tranId;

    @Column(name = "tran_type_cd", nullable = false, length = 2)
    private String tranTypeCd;

    @Column(name = "tran_cat_cd", nullable = false, length = 2)
    private String tranCatCd;

    @Column(name = "tran_source", nullable = false)
    private String tranSource;

    @Column(name = "tran_desc", nullable = false)
    private String tranDesc;

    @Column(name = "tran_amt", nullable = false, precision = 13, scale = 2)
    private BigDecimal tranAmt;

    @Column(name = "tran_card_num", nullable = false, length = 16)
    private String tranCardNum;

    @Column(name = "tran_merchant_id", nullable = false)
    private Long tranMerchantId;

    @Column(name = "tran_merchant_name")
    private String tranMerchantName;

    @Column(name = "tran_merchant_city")
    private String tranMerchantCity;

    @Column(name = "tran_merchant_zip")
    private String tranMerchantZip;

    @Column(name = "tran_orig_ts", nullable = false, length = 26)
    private LocalDateTime tranOrigTs;

    @Column(name = "tran_proc_ts", nullable = false, length = 26)
    private LocalDateTime tranProcTs;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateTransaction() {
        validateCardNumber();
        validateTransactionId();
        validateTransactionAmount();
        validateTimestamps();
    }

    private void validateCardNumber() {
        if (tranCardNum != null && !tranCardNum.matches("^[0-9]{16}$")) {
            throw new IllegalArgumentException("Invalid card number format");
        }
        if (tranCardNum == null || tranCardNum.trim().isEmpty() || tranCardNum.equals("0000000000000000")) {
            throw new IllegalArgumentException("Card number is required");
        }
    }

    private void validateTransactionId() {
        if (tranId == null || tranId.trim().isEmpty()) {
            throw new IllegalArgumentException("Tran ID can NOT be empty...");
        }
        if (tranId.length() > 16) {
            throw new IllegalArgumentException("Transaction ID must not exceed 16 characters");
        }
    }

    private void validateTransactionAmount() {
        if (tranAmt == null) {
            throw new IllegalArgumentException("Invalid transaction amount");
        }
        if (tranAmt.scale() > 2) {
            throw new IllegalArgumentException("Invalid transaction amount");
        }
    }

    private void validateTimestamps() {
        if (tranOrigTs != null && tranProcTs != null && tranOrigTs.isAfter(tranProcTs)) {
            throw new IllegalArgumentException("Original timestamp cannot be after processing timestamp");
        }
    }

    public String generateTransactionId(String dateParameter, int suffix) {
        return dateParameter + String.format("%06d", suffix);
    }

    public boolean isInterestTransaction() {
        return "01".equals(tranTypeCd) && "05".equals(tranCatCd);
    }

    public boolean isSystemGenerated() {
        return "System".equalsIgnoreCase(tranSource);
    }

    public String getFormattedTransactionDate() {
        if (transactionDate != null) {
            return String.format("%02d/%02d/%02d", 
                transactionDate.getMonthValue(), 
                transactionDate.getDayOfMonth(), 
                transactionDate.getYear() % 100);
        }
        return "";
    }

    public boolean isWithinDateRange(LocalDate startDate, LocalDate endDate) {
        if (transactionDate == null || startDate == null || endDate == null) {
            return false;
        }
        return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
    }
}
