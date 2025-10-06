package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

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
    
    @Column(name = "transaction_data", length = 318, nullable = false)
    private String transactionData;
    
    @Column(name = "transaction_type", length = 4)
    private String transactionType;
    
    @Column(name = "transaction_amount", length = 15)
    private String transactionAmount;
    
    @Column(name = "transaction_date", length = 8)
    private String transactionDate;
    
    @Column(name = "transaction_time", length = 6)
    private String transactionTime;
    
    @Column(name = "merchant_id", length = 15)
    private String merchantId;
    
    @Column(name = "merchant_name", length = 50)
    private String merchantName;
    
    @Column(name = "merchant_category", length = 4)
    private String merchantCategory;
    
    @Column(name = "authorization_code", length = 6)
    private String authorizationCode;
    
    @Column(name = "response_code", length = 2)
    private String responseCode;
    
    @Column(name = "terminal_id", length = 8)
    private String terminalId;
    
    @Column(name = "currency_code", length = 3)
    private String currencyCode;
    
    @Column(name = "transaction_status", length = 1, nullable = false)
    private String transactionStatus;
    
    @Column(name = "batch_number", length = 6)
    private String batchNumber;
    
    @Column(name = "sequence_number", length = 6)
    private String sequenceNumber;
    
    @Column(name = "reference_number", length = 12)
    private String referenceNumber;
    
    @Column(name = "original_amount", length = 15)
    private String originalAmount;
    
    @Column(name = "cashback_amount", length = 15)
    private String cashbackAmount;
    
    @Column(name = "fee_amount", length = 15)
    private String feeAmount;
    
    @Column(name = "processing_code", length = 6)
    private String processingCode;
    
    @Column(name = "pos_entry_mode", length = 3)
    private String posEntryMode;
    
    @Column(name = "card_acceptor_id", length = 15)
    private String cardAcceptorId;
    
    @Column(name = "acquirer_id", length = 11)
    private String acquirerId;
    
    @Column(name = "retrieval_reference", length = 12)
    private String retrievalReference;
    
    @Column(name = "system_trace_number", length = 6)
    private String systemTraceNumber;
    
    @Column(name = "network_id", length = 3)
    private String networkId;
    
    @Column(name = "settlement_date", length = 8)
    private String settlementDate;
    
    @Column(name = "capture_date", length = 8)
    private String captureDate;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionId implements Serializable {
        private String cardNumber;
        private String transactionId;
    }
    
    public Transaction(String cardNumber, String transactionId, String transactionData) {
        this.cardNumber = cardNumber;
        this.transactionId = transactionId;
        this.transactionData = transactionData;
        this.transactionStatus = "P";
    }
    
    public String getFormattedCardNumber() {
        if (cardNumber != null && cardNumber.length() >= 4) {
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
        return "**** **** **** ****";
    }
    
    public String getTransactionDateTime() {
        StringBuilder dateTime = new StringBuilder();
        if (transactionDate != null && !transactionDate.trim().isEmpty()) {
            dateTime.append(transactionDate.trim());
        }
        if (transactionTime != null && !transactionTime.trim().isEmpty()) {
            if (dateTime.length() > 0) dateTime.append(" ");
            dateTime.append(transactionTime.trim());
        }
        return dateTime.toString();
    }
    
    public boolean isApproved() {
        return "A".equals(transactionStatus) || "00".equals(responseCode);
    }
    
    public boolean isDeclined() {
        return "D".equals(transactionStatus) || ("05".equals(responseCode) || "51".equals(responseCode) || "61".equals(responseCode));
    }
    
    public boolean isPending() {
        return "P".equals(transactionStatus);
    }
    
    public boolean isReversed() {
        return "R".equals(transactionStatus);
    }
    
    public boolean isSettled() {
        return "S".equals(transactionStatus);
    }
    
    public boolean isCancelled() {
        return "C".equals(transactionStatus);
    }
    
    public String getTransactionStatusDescription() {
        if (isApproved()) {
            return "Approved";
        } else if (isDeclined()) {
            return "Declined";
        } else if (isPending()) {
            return "Pending";
        } else if (isReversed()) {
            return "Reversed";
        } else if (isSettled()) {
            return "Settled";
        } else if (isCancelled()) {
            return "Cancelled";
        } else {
            return "Unknown";
        }
    }
    
    public boolean isPurchaseTransaction() {
        return "00".equals(processingCode) || "PURCH".equals(transactionType);
    }
    
    public boolean isWithdrawalTransaction() {
        return "01".equals(processingCode) || "CASH".equals(transactionType);
    }
    
    public boolean isRefundTransaction() {
        return "20".equals(processingCode) || "REFUND".equals(transactionType);
    }
    
    public boolean isBalanceInquiry() {
        return "31".equals(processingCode) || "BAL".equals(transactionType);
    }
    
    public boolean hasAuthorizationCode() {
        return authorizationCode != null && !authorizationCode.trim().isEmpty() && !"000000".equals(authorizationCode);
    }
    
    public boolean isContactlessTransaction() {
        return "07".equals(posEntryMode) || "91".equals(posEntryMode);
    }
    
    public boolean isChipTransaction() {
        return "05".equals(posEntryMode) || "95".equals(posEntryMode);
    }
    
    public boolean isMagStripeTransaction() {
        return "02".equals(posEntryMode) || "90".equals(posEntryMode);
    }
    
    public boolean isManualEntryTransaction() {
        return "01".equals(posEntryMode) || "10".equals(posEntryMode);
    }
    
    public boolean isECommerceTransaction() {
        return "81".equals(posEntryMode) || "10".equals(posEntryMode);
    }
    
    public String getEntryModeDescription() {
        if (isContactlessTransaction()) {
            return "Contactless";
        } else if (isChipTransaction()) {
            return "Chip";
        } else if (isMagStripeTransaction()) {
            return "Magnetic Stripe";
        } else if (isManualEntryTransaction()) {
            return "Manual Entry";
        } else if (isECommerceTransaction()) {
            return "E-Commerce";
        } else {
            return "Unknown";
        }
    }
    
    public boolean isHighValueTransaction() {
        if (transactionAmount != null && !transactionAmount.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(transactionAmount.trim());
                return amount >= 1000.00;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
    
    public boolean hasCashback() {
        return cashbackAmount != null && !cashbackAmount.trim().isEmpty() && !"0".equals(cashbackAmount.trim()) && !"000".equals(cashbackAmount.trim());
    }
    
    public boolean hasFee() {
        return feeAmount != null && !feeAmount.trim().isEmpty() && !"0".equals(feeAmount.trim()) && !"000".equals(feeAmount.trim());
    }
    
    public String getFormattedAmount() {
        if (transactionAmount != null && !transactionAmount.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(transactionAmount.trim()) / 100.0;
                return String.format("$%.2f", amount);
            } catch (NumberFormatException e) {
                return "$0.00";
            }
        }
        return "$0.00";
    }
    
    public boolean isInternationalTransaction() {
        return currencyCode != null && !currencyCode.trim().isEmpty() && !"840".equals(currencyCode) && !"USD".equals(currencyCode);
    }
    
    public boolean isSettlementReady() {
        return isApproved() && settlementDate != null && !settlementDate.trim().isEmpty();
    }
    
    public String getMerchantInfo() {
        StringBuilder info = new StringBuilder();
        if (merchantName != null && !merchantName.trim().isEmpty()) {
            info.append(merchantName.trim());
        }
        if (merchantId != null && !merchantId.trim().isEmpty()) {
            if (info.length() > 0) info.append(" (");
            info.append("ID: ").append(merchantId.trim());
            if (info.toString().contains("(")) info.append(")");
        }
        return info.toString();
    }
    
    public TransactionId getCompositeKey() {
        return new TransactionId(cardNumber, transactionId);
    }
}