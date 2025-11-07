package com.example.demo.enums;

/**
 * Enum representing the type of user in the system
 */
public enum UserType {
    
    ADMIN("ADMIN", "Administrator"),
    REGULAR("REGULAR", "Regular User");
    
    private final String code;
    private final String displayName;
    
    UserType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    public boolean canViewAllCards() {
        return this == ADMIN;
    }
    
    /**
     * Get UserType from code
     */
    public static UserType fromCode(String code) {
        for (UserType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid user type code: " + code);
    }
}
