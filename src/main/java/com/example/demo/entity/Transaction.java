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

    @Column(name = "transaction_date", nullable = false, length = 8)
    private String transactionDate;

    @Column(name = "transaction_description", nullable = false, length = 26)
    private String transactionDescription;

    @Column(name = "transaction_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "transaction_timestamp", nullable = false, length = 26)
    private String transactionTimestamp;

    @Column(name = "tran_id", nullable = false, unique = true)
    private Long tranId;

    @Column(name = "tran_type_cd", nullable = false)
    private Integer tranTypeCd;

    @Column(name = "tran_cat_cd", nullable = false)
    private Integer tranCatCd;

    @Column(name = "tran_source", nullable = false, length = 10)
    private String tranSource;

    @Column(name = "tran_desc", nullable = false, length = 60)
    private String tranDesc;

    @Column(name = "tran_amt", nullable = false, precision = 11, scale = 2)
    private BigDecimal tranAmt;

    @Column(name = "tran_card_num", nullable = false)
    private Long tranCardNum;

    @Column(name = "tran_merchant_id", nullable = false)
    private Long tranMerchantId;

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

    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;

    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private String transactionCategoryCode;

    @Column(name = "transaction_source", nullable = false, length = 10)
    private String transactionSource;

    @Column(name = "original_transaction_timestamp", nullable = false)
    private LocalDateTime originalTransactionTimestamp;

    @Column(name = "processed_transaction_timestamp", nullable = false)
    private LocalDateTime processedTransactionTimestamp;

    @Column(name = "merchant_id", nullable = false, length = 9)
    private String merchantId;

    @Column(name = "merchant_name", nullable = false, length = 30)
    private String merchantName;

    @Column(name = "merchant_city", nullable = false, length = 25)
    private String merchantCity;

    @Column(name = "merchant_zip", nullable = false, length = 10)
    private String merchantZip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "acct_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "card_num")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_entity_id", referencedColumnName = "merchant_id", insertable = false, updatable = false)
    private Merchant merchant;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Transaction(String transactionId, String transactionDate, String transactionDescription, BigDecimal transactionAmount) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactionDescription = transactionDescription;
        this.transactionAmount = transactionAmount;
    }

    @PrePersist
    @PreUpdate
    public void validateTransaction() {
        if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalStateException("Transaction ID cannot be empty");
        }
        if (transactionId.length() != 16) {
            throw new IllegalStateException("Transaction ID must be 16 characters");
        }
    }

    public String getFormattedTransactionDate() {
        if (transactionTimestamp != null && transactionTimestamp.length() >= 10) {
            String year = transactionTimestamp.substring(2, 4);
            String month = transactionTimestamp.substring(5, 7);
            String day = transactionTimestamp.substring(8, 10);
            return month + "/" + day + "/" + year;
        }
        return transactionDate;
    }

    public String getFormattedTransactionAmount() {
        if (transactionAmount != null) {
            String sign = transactionAmount.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
            return sign + String.format("%,.2f", transactionAmount);
        }
        return "+0.00";
    }

    public String getFormattedTranAmount() {
        if (tranAmt != null) {
            String sign = tranAmt.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
            return sign + String.format("%,.2f", tranAmt);
        }
        return "+0.00";
    }

    public boolean isValidForDisplay() {
        return transactionId != null && !transactionId.trim().isEmpty() 
            && transactionDate != null 
            && transactionDescription != null 
            && transactionAmount != null;
    }

    public String getTruncatedDescription(int maxLength) {
        if (transactionDescription == null) {
            return "";
        }
        if (transactionDescription.length() <= maxLength) {
            return transactionDescription;
        }
        return transactionDescription.substring(0, maxLength);
    }

    public boolean isDebit() {
        return transactionAmount != null && transactionAmount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isCredit() {
        return transactionAmount != null && transactionAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public String getMerchantFullAddress() {
        StringBuilder address = new StringBuilder();
        if (merchantCity != null && !merchantCity.trim().isEmpty()) {
            address.append(merchantCity.trim());
        }
        if (merchantZip != null && !merchantZip.trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(merchantZip.trim());
        }
        return address.toString();
    }

    public String getTranMerchantFullAddress() {
        StringBuilder address = new StringBuilder();
        if (tranMerchantCity != null && !tranMerchantCity.trim().isEmpty()) {
            address.append(tranMerchantCity.trim());
        }
        if (tranMerchantZip != null && !tranMerchantZip.trim().isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(tranMerchantZip.trim());
        }
        return address.toString();
    }

    public Long getNextTransactionId() {
        if (tranId != null) {
            return tranId + 1;
        }
        return 1L;
    }

    public boolean matchesTransactionId(String searchId) {
        if (searchId == null || transactionId == null) {
            return false;
        }
        return transactionId.equals(searchId) || (tranId != null && tranId.toString().equals(searchId));
    }
}
