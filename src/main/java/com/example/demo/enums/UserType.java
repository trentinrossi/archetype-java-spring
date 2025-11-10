package com.example.demo.enums;

/**
 * Enum representing the type of user in the system.
 * Determines user permissions and access rights.
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
    
    /**
     * Check if user is an administrator
     * Admin users can view all cards when no context is passed
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    /**
     * Check if user can view all cards
     * @return true if user has permission to view all cards
     */
    public boolean canViewAllCards() {
        return this == ADMIN;
    }
    
    /**
     * Check if user is restricted to their own account
     * Regular users can only view cards associated with their specific account
     * @return true if user is restricted to their account
     */
    public boolean isAccountRestricted() {
        return this == REGULAR;
    }
    
    /**
     * Get UserType from code
     * @param code the user type code
     * @return UserType enum value
     * @throws IllegalArgumentException if code is invalid
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
