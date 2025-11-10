package com.example.demo.enums;

/**
 * Enum representing the status of a credit card.
 * Card status is stored as a single character in the database.
 */
public enum CardStatus {
    
    ACTIVE("A", "Active"),
    INACTIVE("I", "Inactive"),
    BLOCKED("B", "Blocked"),
    CANCELLED("C", "Cancelled"),
    SUSPENDED("S", "Suspended");
    
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
    
    /**
     * Check if the card can be used for transactions
     * @return true if card is active, false otherwise
     */
    public boolean isUsable() {
        return this == ACTIVE;
    }
    
    /**
     * Check if the card can be modified
     * @return true if card is not cancelled, false otherwise
     */
    public boolean canModify() {
        return this != CANCELLED;
    }
    
    /**
     * Get CardStatus from code
     * @param code the status code
     * @return CardStatus enum value
     * @throws IllegalArgumentException if code is invalid
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
