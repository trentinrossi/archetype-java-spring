package com.example.demo.enums;

/**
 * Card status enumeration
 * Defines the various states a card can be in
 */
public enum CardStatus {
    
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    LOST("Lost"),
    STOLEN("Stolen"),
    DAMAGED("Damaged"),
    EXPIRED("Expired"),
    PENDING_ACTIVATION("Pending Activation"),
    CLOSED("Closed"),
    FRAUD("Fraud");
    
    private final String displayName;
    
    CardStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean canTransact() {
        return this == ACTIVE;
    }
    
    public boolean requiresReplacement() {
        return this == LOST || this == STOLEN || this == DAMAGED || this == EXPIRED;
    }
    
    public boolean isTerminal() {
        return this == CLOSED;
    }
    
    public boolean requiresAttention() {
        return this == FRAUD || this == LOST || this == STOLEN;
    }
}
