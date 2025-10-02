package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignonResponse {
    
    @Schema(description = "Authentication result", example = "true", required = true)
    private boolean success;
    
    @Schema(description = "User ID", example = "USER001", required = false)
    private String userId;
    
    @Schema(description = "User type (A=Admin, R=Regular)", example = "A", required = false)
    private String userType;
    
    @Schema(description = "User type display name", example = "Admin", required = false)
    private String userTypeDisplayName;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = false)
    private String fullName;
    
    @Schema(description = "Redirect URL after successful authentication", example = "/dashboard", required = false)
    private String redirectUrl;
    
    @Schema(description = "Error message if authentication failed", example = "Invalid credentials", required = false)
    private String errorMessage;
    
    @Schema(description = "Error code", example = "AUTH_FAILED", required = false)
    private String errorCode;
    
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