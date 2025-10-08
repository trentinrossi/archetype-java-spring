package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    
    private String userId;
    
    private String firstName;
    
    private String lastName;
    
    private String userType;
    
    private String userStatus;
    
    private Boolean accountLocked;
    
    private Boolean passwordExpired;
    
    private Integer failedLoginAttempts;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private Long loginCount;
    
    private Boolean twoFactorEnabled;
    
    // Computed fields for display
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getUserTypeDisplayName() {
        return switch (userType) {
            case "A" -> "Administrator";
            case "R" -> "Regular User";
            default -> "Unknown";
        };
    }
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getUserStatusDisplayName() {
        return switch (userStatus) {
            case "A" -> "Active";
            case "I" -> "Inactive";
            case "S" -> "Suspended";
            case "D" -> "Deleted";
            default -> "Unknown";
        };
    }
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Boolean getCanLogin() {
        return "A".equals(userStatus) && 
               (accountLocked == null || !accountLocked);
    }
}