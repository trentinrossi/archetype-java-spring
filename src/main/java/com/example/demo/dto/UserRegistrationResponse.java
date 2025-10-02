package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse {
    
    @Schema(description = "Registration result", example = "true", required = true)
    private boolean success;
    
    @Schema(description = "Registered user ID", example = "USER001", required = false)
    private String userId;
    
    @Schema(description = "Full name of the registered user", example = "John Doe", required = false)
    private String fullName;
    
    @Schema(description = "User type", example = "A", required = false)
    private String userType;
    
    @Schema(description = "User type display name", example = "Admin", required = false)
    private String userTypeDisplayName;
    
    @Schema(description = "Success message", example = "User registered successfully", required = false)
    private String successMessage;
    
    @Schema(description = "Error message if registration failed", example = "User ID already exists", required = false)
    private String errorMessage;
    
    @Schema(description = "Error code", example = "USER_EXISTS", required = false)
    private String errorCode;
    
    @Schema(description = "Timestamp when the user was registered", example = "2023-10-01T12:00:00", required = false)
    private LocalDateTime registeredAt;
    
    public void setUserId(String userId) {
        this.userId = userId != null ? userId.toUpperCase() : null;
    }
    
    public void setUserType(String userType) {
        this.userType = userType != null ? userType.toUpperCase() : null;
        this.userTypeDisplayName = getUserTypeDisplayName(this.userType);
    }
    
    private String getUserTypeDisplayName(String userType) {
        if ("A".equals(userType)) {
            return "Admin";
        } else if ("R".equals(userType)) {
            return "Regular";
        }
        return "Unknown";
    }
}