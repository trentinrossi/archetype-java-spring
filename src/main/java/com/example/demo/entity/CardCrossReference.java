package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_cross_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReference {
    
    @Id
    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;
    
    @Column(name = "cross_reference_data", length = 34, nullable = false)
    private String crossReferenceData;
    
    @Column(name = "account_number", length = 20)
    private String accountNumber;
    
    @Column(name = "account_type", length = 2)
    private String accountType;
    
    @Column(name = "customer_id", length = 12)
    private String customerId;
    
    @Column(name = "card_status", length = 1, nullable = false)
    private String cardStatus;
    
    @Column(name = "card_type", length = 2)
    private String cardType;
    
    @Column(name = "issue_date", length = 8)
    private String issueDate;
    
    @Column(name = "expiry_date", length = 8)
    private String expiryDate;
    
    @Column(name = "branch_code", length = 4)
    private String branchCode;
    
    @Column(name = "product_code", length = 3)
    private String productCode;
    
    @Column(name = "card_sequence", length = 3)
    private String cardSequence;
    
    @Column(name = "pin_offset", length = 4)
    private String pinOffset;
    
    @Column(name = "daily_limit", length = 10)
    private String dailyLimit;
    
    @Column(name = "monthly_limit", length = 10)
    private String monthlyLimit;
    
    @Column(name = "last_activity_date", length = 8)
    private String lastActivityDate;
    
    @Column(name = "activation_date", length = 8)
    private String activationDate;
    
    @Column(name = "block_code", length = 2)
    private String blockCode;
    
    @Column(name = "replacement_card", length = 16)
    private String replacementCard;
    
    @Column(name = "previous_card", length = 16)
    private String previousCard;
    
    @Column(name = "emboss_name", length = 26)
    private String embossName;
    
    @Column(name = "delivery_method", length = 1)
    private String deliveryMethod;
    
    @Column(name = "priority_flag", length = 1)
    private String priorityFlag;
    
    @Column(name = "international_flag", length = 1)
    private String internationalFlag;
    
    @Column(name = "contactless_flag", length = 1)
    private String contactlessFlag;
    
    @Column(name = "mobile_payment_flag", length = 1)
    private String mobilePaymentFlag;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public CardCrossReference(String cardNumber, String crossReferenceData) {
        this.cardNumber = cardNumber;
        this.crossReferenceData = crossReferenceData;
        this.cardStatus = "A";
    }
    
    public String getFormattedCardNumber() {
        if (cardNumber != null && cardNumber.length() >= 4) {
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
        return "**** **** **** ****";
    }
    
    public boolean isActive() {
        return "A".equals(cardStatus);
    }
    
    public boolean isBlocked() {
        return "B".equals(cardStatus);
    }
    
    public boolean isSuspended() {
        return "S".equals(cardStatus);
    }
    
    public boolean isCancelled() {
        return "C".equals(cardStatus);
    }
    
    public boolean isExpired() {
        return "E".equals(cardStatus);
    }
    
    public boolean isPending() {
        return "P".equals(cardStatus);
    }
    
    public String getCardStatusDescription() {
        if (isActive()) {
            return "Active";
        } else if (isBlocked()) {
            return "Blocked";
        } else if (isSuspended()) {
            return "Suspended";
        } else if (isCancelled()) {
            return "Cancelled";
        } else if (isExpired()) {
            return "Expired";
        } else if (isPending()) {
            return "Pending";
        } else {
            return "Unknown";
        }
    }
    
    public boolean isDebitCard() {
        return "01".equals(cardType) || "DB".equals(cardType);
    }
    
    public boolean isCreditCard() {
        return "02".equals(cardType) || "CR".equals(cardType);
    }
    
    public boolean isPrepaidCard() {
        return "03".equals(cardType) || "PP".equals(cardType);
    }
    
    public boolean isBusinessCard() {
        return "04".equals(cardType) || "BZ".equals(cardType);
    }
    
    public String getCardTypeDescription() {
        if (isDebitCard()) {
            return "Debit Card";
        } else if (isCreditCard()) {
            return "Credit Card";
        } else if (isPrepaidCard()) {
            return "Prepaid Card";
        } else if (isBusinessCard()) {
            return "Business Card";
        } else {
            return "Unknown Card Type";
        }
    }
    
    public boolean isInternationalEnabled() {
        return "Y".equals(internationalFlag) || "1".equals(internationalFlag);
    }
    
    public boolean isContactlessEnabled() {
        return "Y".equals(contactlessFlag) || "1".equals(contactlessFlag);
    }
    
    public boolean isMobilePaymentEnabled() {
        return "Y".equals(mobilePaymentFlag) || "1".equals(mobilePaymentFlag);
    }
    
    public boolean isPriorityCard() {
        return "Y".equals(priorityFlag) || "1".equals(priorityFlag);
    }
    
    public boolean hasBlockCode() {
        return blockCode != null && !blockCode.trim().isEmpty() && !"00".equals(blockCode);
    }
    
    public String getBlockCodeDescription() {
        if (!hasBlockCode()) {
            return "No Block";
        }
        switch (blockCode) {
            case "01": return "Lost Card";
            case "02": return "Stolen Card";
            case "03": return "Damaged Card";
            case "04": return "Fraud Suspected";
            case "05": return "Customer Request";
            case "06": return "Expired Card";
            case "07": return "Account Closed";
            case "08": return "Temporary Block";
            case "09": return "System Block";
            default: return "Other Block";
        }
    }
    
    public boolean isCardExpired() {
        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            try {
                String currentDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
                return expiryDate.compareTo(currentDate) < 0;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    public boolean isCardActive() {
        return isActive() && !isCardExpired() && !hasBlockCode();
    }
    
    public boolean hasReplacementCard() {
        return replacementCard != null && !replacementCard.trim().isEmpty() && !"0000000000000000".equals(replacementCard);
    }
    
    public boolean hasPreviousCard() {
        return previousCard != null && !previousCard.trim().isEmpty() && !"0000000000000000".equals(previousCard);
    }
    
    public String getFormattedExpiryDate() {
        if (expiryDate != null && expiryDate.length() >= 6) {
            return expiryDate.substring(4, 6) + "/" + expiryDate.substring(2, 4);
        }
        return "**/**";
    }
    
    public String getFormattedIssueDate() {
        if (issueDate != null && issueDate.length() == 8) {
            return issueDate.substring(6, 8) + "/" + issueDate.substring(4, 6) + "/" + issueDate.substring(0, 4);
        }
        return "**/**/****";
    }
    
    public boolean isHighValueCard() {
        if (dailyLimit != null && !dailyLimit.trim().isEmpty()) {
            try {
                double limit = Double.parseDouble(dailyLimit.trim());
                return limit >= 5000.00;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
    
    public String getFormattedDailyLimit() {
        if (dailyLimit != null && !dailyLimit.trim().isEmpty()) {
            try {
                double limit = Double.parseDouble(dailyLimit.trim()) / 100.0;
                return String.format("$%.2f", limit);
            } catch (NumberFormatException e) {
                return "$0.00";
            }
        }
        return "$0.00";
    }
    
    public String getFormattedMonthlyLimit() {
        if (monthlyLimit != null && !monthlyLimit.trim().isEmpty()) {
            try {
                double limit = Double.parseDouble(monthlyLimit.trim()) / 100.0;
                return String.format("$%.2f", limit);
            } catch (NumberFormatException e) {
                return "$0.00";
            }
        }
        return "$0.00";
    }
    
    public boolean isRecentlyActive() {
        if (lastActivityDate != null && !lastActivityDate.trim().isEmpty()) {
            try {
                String thirtyDaysAgo = java.time.LocalDate.now().minusDays(30).format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
                return lastActivityDate.compareTo(thirtyDaysAgo) >= 0;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    public String getDeliveryMethodDescription() {
        if (deliveryMethod == null || deliveryMethod.trim().isEmpty()) {
            return "Standard";
        }
        switch (deliveryMethod) {
            case "1": return "Standard Mail";
            case "2": return "Express Mail";
            case "3": return "Courier";
            case "4": return "Branch Pickup";
            case "5": return "Digital Delivery";
            default: return "Standard";
        }
    }
    
    public String getAccountTypeDescription() {
        if (accountType == null || accountType.trim().isEmpty()) {
            return "Unknown";
        }
        switch (accountType) {
            case "01": return "Checking Account";
            case "02": return "Savings Account";
            case "03": return "Credit Account";
            case "04": return "Money Market";
            case "05": return "Certificate of Deposit";
            case "06": return "Investment Account";
            case "07": return "Business Account";
            default: return "Other Account";
        }
    }
    
    public boolean canPerformTransactions() {
        return isCardActive() && !hasBlockCode() && !isCardExpired();
    }
    
    public String getCardSummary() {
        return String.format("%s - %s (%s) - %s", 
            getFormattedCardNumber(), 
            getCardTypeDescription(), 
            getCardStatusDescription(),
            getFormattedExpiryDate());
    }
}