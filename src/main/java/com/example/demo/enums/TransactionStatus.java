package com.example.demo.enums;

/**
 * Transaction status enumeration
 * Defines the various states a transaction can be in
 */
public enum TransactionStatus {
    
    PENDING("Pending"),
    AUTHORIZED("Authorized"),
    POSTED("Posted"),
    DECLINED("Declined"),
    REVERSED("Reversed"),
    DISPUTED("Disputed"),
    SETTLED("Settled"),
    CANCELLED("Cancelled"),
    FAILED("Failed");
    
    private final String displayName;
    
    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isFinal() {
        return this == POSTED || this == SETTLED || this == REVERSED || this == CANCELLED;
    }
    
    public boolean canBeModified() {
        return this == PENDING || this == AUTHORIZED;
    }
    
    public boolean isSuccessful() {
        return this == AUTHORIZED || this == POSTED || this == SETTLED;
    }
    
    public boolean requiresAttention() {
        return this == DISPUTED || this == FAILED;
    }
}
