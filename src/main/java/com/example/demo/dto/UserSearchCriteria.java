package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {
    
    // Search filters
    private String userId;
    private String firstName;
    private String lastName;
    private String userType;
    private String userStatus;
    private Boolean accountLocked;
    private Boolean passwordExpired;
    private Boolean twoFactorEnabled;
    private Boolean isHighRisk;
    
    // Date range filters
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private LocalDateTime lastLoginAfter;
    private LocalDateTime lastLoginBefore;
    private LocalDateTime updatedAfter;
    private LocalDateTime updatedBefore;
    
    // Numeric filters
    private Integer minFailedAttempts;
    private Integer maxFailedAttempts;
    private Long minLoginCount;
    private Long maxLoginCount;
    
    // Pagination
    private int page = 0;
    private int size = 10;
    private String sortBy = "userId";
    private String sortDirection = "ASC";
    
    // Additional filters
    private Boolean includeDeleted = false;
    private Boolean activeOnly = false;
    private Boolean adminOnly = false;
    private Boolean regularOnly = false;
    private Boolean lockedOnly = false;
    private Boolean expiredPasswordOnly = false;
    private Boolean recentlyCreated = false;
    private Boolean recentlyLoggedIn = false;
    private Boolean neverLoggedIn = false;
    private Boolean dormantAccounts = false;
    
    // Search text (for full-text search across multiple fields)
    private String searchText;
    
    // IP address filter
    private String lastIpAddress;
    
    // Created/Updated by filters
    private String createdBy;
    private String updatedBy;
    
    // Helper methods
    public boolean hasUserIdFilter() {
        return userId != null && !userId.trim().isEmpty();
    }
    
    public boolean hasNameFilter() {
        return (firstName != null && !firstName.trim().isEmpty()) ||
               (lastName != null && !lastName.trim().isEmpty());
    }
    
    public boolean hasDateRangeFilter() {
        return createdAfter != null || createdBefore != null ||
               lastLoginAfter != null || lastLoginBefore != null ||
               updatedAfter != null || updatedBefore != null;
    }
    
    public boolean hasStatusFilter() {
        return userType != null || userStatus != null ||
               accountLocked != null || passwordExpired != null;
    }
    
    public boolean hasSecurityFilter() {
        return twoFactorEnabled != null || isHighRisk != null ||
               minFailedAttempts != null || maxFailedAttempts != null;
    }
    
    public boolean hasActivityFilter() {
        return minLoginCount != null || maxLoginCount != null ||
               recentlyLoggedIn != null || neverLoggedIn != null ||
               dormantAccounts != null;
    }
    
    public boolean hasSearchText() {
        return searchText != null && !searchText.trim().isEmpty();
    }
    
    public boolean isValidSortBy() {
        return sortBy != null && (
            "userId".equals(sortBy) ||
            "firstName".equals(sortBy) ||
            "lastName".equals(sortBy) ||
            "userType".equals(sortBy) ||
            "userStatus".equals(sortBy) ||
            "createdAt".equals(sortBy) ||
            "updatedAt".equals(sortBy) ||
            "lastLoginAt".equals(sortBy) ||
            "loginCount".equals(sortBy) ||
            "failedLoginAttempts".equals(sortBy)
        );
    }
    
    public boolean isValidSortDirection() {
        return "ASC".equalsIgnoreCase(sortDirection) || "DESC".equalsIgnoreCase(sortDirection);
    }
    
    public void validatePagination() {
        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        if (!isValidSortBy()) sortBy = "userId";
        if (!isValidSortDirection()) sortDirection = "ASC";
    }
}