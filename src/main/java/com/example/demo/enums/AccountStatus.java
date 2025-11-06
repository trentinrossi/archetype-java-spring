package com.example.demo.enums;

/**
 * Account status enumeration
 * Defines the various states an account can be in
 */
public enum AccountStatus {
    
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    CLOSED("Closed"),
    CHARGED_OFF("Charged Off"),
    FRAUD("Fraud"),
    PENDING_ACTIVATION("Pending Activation"),
    DELINQUENT("Delinquent"),
    OVERLIMIT("Over Limit");
    
    private final String displayName;
    
    AccountStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean canTransact() {
        return this == ACTIVE;
    }
    
    public boolean canModify() {
        return this == ACTIVE || this == SUSPENDED || this == PENDING_ACTIVATION;
    }
    
    public boolean isTerminal() {
        return this == CLOSED || this == CHARGED_OFF;
    }
    
    public boolean requiresAttention() {
        return this == DELINQUENT || this == OVERLIMIT || this == FRAUD;
    }
}
