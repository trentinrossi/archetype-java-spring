package com.example.demo.enums;

/**
 * UserType Enum
 * 
 * Represents the type of user in the credit card management system.
 * Implements business rules for user permission-based card access (BR001).
 * 
 * Field Specifications:
 * - user_type: alphanumeric (length: 10), required
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 */
public enum UserType {
    
    /**
     * Administrator user type with full access to all credit cards
     */
    ADMIN("ADMIN", "Administrator", true),
    
    /**
     * Regular user type with restricted access to own account cards only
     */
    REGULAR("REGULAR", "Regular User", false);
    
    private final String code;
    private final String displayName;
    private final boolean canViewAllCards;
    
    /**
     * Constructor for UserType enum
     * 
     * @param code The user type code (max 10 characters)
     * @param displayName The display name for the user type
     * @param canViewAllCards Whether this user type can view all cards
     */
    UserType(String code, String displayName, boolean canViewAllCards) {
        if (code == null || code.length() > 10) {
            throw new IllegalArgumentException("User type code must not be null and must be 10 characters or less");
        }
        this.code = code;
        this.displayName = displayName;
        this.canViewAllCards = canViewAllCards;
    }
    
    /**
     * Gets the user type code
     * 
     * @return The user type code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Gets the display name for the user type
     * 
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Checks if this user type is an administrator
     * Implements BR001: User Permission Based Card Access
     * 
     * @return true if user type is ADMIN, false otherwise
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    /**
     * Checks if this user type is a regular user
     * Implements BR001: User Permission Based Card Access
     * 
     * @return true if user type is REGULAR, false otherwise
     */
    public boolean isRegularUser() {
        return this == REGULAR;
    }
    
    /**
     * Checks if this user type can view all credit cards without context
     * Implements BR001: User Permission Based Card Access
     * 
     * Business Logic:
     * - Admin users can view all credit cards without context
     * - Regular users can only view cards associated with their specific account
     * 
     * @return true if user can view all cards, false if restricted to own account
     */
    public boolean canViewAllCards() {
        return canViewAllCards;
    }
    
    /**
     * Determines if account context is required for this user type
     * Implements BR001: User Permission Based Card Access
     * 
     * Business Logic:
     * - Admin users do not require account context
     * - Regular users require account context to filter cards
     * 
     * @return true if account context is required, false otherwise
     */
    public boolean requiresAccountContext() {
        return !canViewAllCards;
    }
    
    /**
     * Checks if this user type can access cards for a given account context
     * Implements BR001: User Permission Based Card Access
     * 
     * Business Logic:
     * - Admin users can access cards regardless of context
     * - Regular users can only access cards when account context is provided
     * 
     * @param hasAccountContext Whether account context is provided
     * @return true if user can access cards with given context, false otherwise
     */
    public boolean canAccessCards(boolean hasAccountContext) {
        if (isAdmin()) {
            return true;
        }
        return hasAccountContext;
    }
    
    /**
     * Parses a user type code string to UserType enum
     * 
     * @param code The user type code to parse
     * @return The corresponding UserType enum value
     * @throws IllegalArgumentException if code is invalid or not found
     */
    public static UserType fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("User type code cannot be null or empty");
        }
        
        String normalizedCode = code.trim().toUpperCase();
        
        for (UserType userType : UserType.values()) {
            if (userType.code.equals(normalizedCode)) {
                return userType;
            }
        }
        
        throw new IllegalArgumentException("Invalid user type code: " + code);
    }
    
    /**
     * Checks if a given code is a valid user type
     * 
     * @param code The user type code to validate
     * @return true if code is valid, false otherwise
     */
    public static boolean isValidCode(String code) {
        if (code == null || code.trim().isEmpty() || code.length() > 10) {
            return false;
        }
        
        String normalizedCode = code.trim().toUpperCase();
        
        for (UserType userType : UserType.values()) {
            if (userType.code.equals(normalizedCode)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        return code;
    }
}
