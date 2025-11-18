package com.example.demo.enums;

public enum UserType {
    
    ADMIN("Admin"),
    REGULAR("Regular");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * BR001: User Authorization for Card Viewing
     * Admin users can view all credit cards without restrictions
     */
    public boolean canViewAllCards() {
        return this == ADMIN;
    }
    
    /**
     * BR001: User Authorization for Card Viewing
     * Regular users can only view cards associated with their account
     */
    public boolean isRestricted() {
        return this == REGULAR;
    }
}
