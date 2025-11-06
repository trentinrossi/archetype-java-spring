package com.example.demo.enums;

/**
 * Customer status enumeration
 * Defines the various states a customer account can be in
 */
public enum CustomerStatus {
    
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    PENDING("Pending Approval"),
    CLOSED("Closed"),
    DECEASED("Deceased"),
    FRAUD("Fraud Alert");
    
    private final String displayName;
    
    CustomerStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean canTransact() {
        return this == ACTIVE;
    }
    
    public boolean canModify() {
        return this == ACTIVE || this == SUSPENDED || this == PENDING;
    }
    
    public boolean isTerminal() {
        return this == CLOSED || this == DECEASED;
    }
}
