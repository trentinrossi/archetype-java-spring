package com.example.demo.enums;

public enum StatementStatus {
    
    GENERATED("Generated"),
    SENT("Sent"),
    VIEWED("Viewed"),
    ARCHIVED("Archived");
    
    private final String displayName;
    
    StatementStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isActive() {
        return this == GENERATED || this == SENT;
    }
    
    public boolean canBeModified() {
        return this == GENERATED;
    }
}
