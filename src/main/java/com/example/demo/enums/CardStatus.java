package com.example.demo.enums;

/**
 * Enum representing the status of a credit card
 */
public enum CardStatus {
    
    ACTIVE("A", "Active"),
    BLOCKED("B", "Blocked"),
    EXPIRED("E", "Expired"),
    SUSPENDED("S", "Suspended"),
    CANCELLED("C", "Cancelled");
    
    private final String code;
    private final String displayName;
    
    CardStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public boolean canPerformTransactions() {
        return this == ACTIVE;
    }
    
    public boolean isBlocked() {
        return this == BLOCKED || this == SUSPENDED || this == CANCELLED;
    }
    
    /**
     * Get CardStatus from code
     */
    public static CardStatus fromCode(String code) {
        for (CardStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid card status code: " + code);
    }
}
