package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponse {
    
    private boolean success;
    private String message;
    private String userId;
    private String userType;
    private String userTypeDisplayName;
    private String sessionToken;
    private LocalDateTime sessionExpiry;
    private boolean requiresPasswordChange;
    private boolean requiresTwoFactor;
    private boolean accountLocked;
    private boolean passwordExpired;
    private Integer failedAttempts;
    private String redirectUrl;
    private LocalDateTime lastLoginAt;
    private Long loginCount;
    
    // Factory methods for common responses
    public static UserAuthResponse success(String userId, String userType, String sessionToken, LocalDateTime sessionExpiry) {
        UserAuthResponse response = new UserAuthResponse();
        response.success = true;
        response.message = "Authentication successful";
        response.userId = userId;
        response.userType = userType;
        response.sessionToken = sessionToken;
        response.sessionExpiry = sessionExpiry;
        return response;
    }
    
    public static UserAuthResponse failure(String message) {
        UserAuthResponse response = new UserAuthResponse();
        response.success = false;
        response.message = message;
        return response;
    }
    
    public static UserAuthResponse accountLocked(String userId, Integer failedAttempts) {
        UserAuthResponse response = new UserAuthResponse();
        response.success = false;
        response.message = "Account is locked due to multiple failed login attempts";
        response.userId = userId;
        response.accountLocked = true;
        response.failedAttempts = failedAttempts;
        return response;
    }
    
    public static UserAuthResponse passwordExpired(String userId) {
        UserAuthResponse response = new UserAuthResponse();
        response.success = false;
        response.message = "Password has expired and must be changed";
        response.userId = userId;
        response.passwordExpired = true;
        response.requiresPasswordChange = true;
        return response;
    }
    
    public static UserAuthResponse requiresTwoFactor(String userId) {
        UserAuthResponse response = new UserAuthResponse();
        response.success = false;
        response.message = "Two-factor authentication required";
        response.userId = userId;
        response.requiresTwoFactor = true;
        return response;
    }
}