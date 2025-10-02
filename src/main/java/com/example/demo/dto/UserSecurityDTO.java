package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDTO {
    
    @Schema(description = "User ID", example = "USER001", required = true)
    private String userId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "User type (A=Admin, R=Regular)", example = "A", required = true)
    private String userType;
    
    @Schema(description = "User type display name", example = "Admin", required = true)
    private String userTypeDisplayName;
    
    @Schema(description = "Indicates if user is admin", example = "true", required = true)
    private boolean isAdmin;
    
    @Schema(description = "Indicates if user is regular user", example = "false", required = true)
    private boolean isRegular;
    
    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
    
    public void setUserId(String userId) {
        this.userId = userId != null ? userId.toUpperCase() : null;
    }
    
    public void setUserType(String userType) {
        this.userType = userType != null ? userType.toUpperCase() : null;
        this.userTypeDisplayName = getUserTypeDisplayName(this.userType);
        this.isAdmin = "A".equals(this.userType);
        this.isRegular = "R".equals(this.userType);
    }
    
    private String getUserTypeDisplayName(String userType) {
        if ("A".equals(userType)) {
            return "Admin";
        } else if ("R".equals(userType)) {
            return "Regular";
        }
        return "Unknown";
    }
    
    public void setFullName(String firstName, String lastName) {
        this.fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}