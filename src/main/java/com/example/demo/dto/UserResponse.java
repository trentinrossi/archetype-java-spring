package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private String userId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String userType;
    private String userTypeDisplayName;
    private String userStatus;
    private String userStatusDisplayName;
    private Boolean accountLocked;
    private Boolean passwordExpired;
    private Boolean forcePasswordChange;
    private Boolean twoFactorEnabled;
    private Integer failedLoginAttempts;
    private Long loginCount;
    private LocalDateTime lastLoginAt;
    private LocalDateTime lastPasswordChange;
    private LocalDateTime passwordExpiryDate;
    private LocalDateTime accountExpiryDate;
    private String lastIpAddress;
    private Boolean hasSecurityQuestion;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Security status indicators
    private Boolean canLogin;
    private Boolean isActive;
    private Boolean isAdmin;
    private Boolean isHighRisk;
    private Boolean isPasswordNearExpiry;
    private Boolean isAccountExpired;
    private Boolean hasValidSession;
    
    // Calculated fields
    private Long daysSinceLastLogin;
    private Long daysSincePasswordChange;
    private Long daysUntilPasswordExpiry;
    private Long daysUntilAccountExpiry;
}