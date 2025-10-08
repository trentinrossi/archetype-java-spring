package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    
    @NotBlank(message = "User ID cannot be blank")
    @Size(min = 8, max = 8, message = "User ID must be exactly 8 characters")
    @Pattern(regexp = "^[A-Za-z0-9]{8}$", message = "User ID must contain only alphanumeric characters")
    private String userId;
    
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 20, message = "First name must not exceed 20 characters")
    private String firstName;
    
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    private String lastName;
    
    @NotBlank(message = "User type cannot be blank")
    @Pattern(regexp = "^[AR]$", message = "User type must be 'A' (Admin) or 'R' (Regular)")
    private String userType;
    
    @Pattern(regexp = "^[AISD]$", message = "User status must be 'A' (Active), 'I' (Inactive), 'S' (Suspended), or 'D' (Deleted)")
    private String userStatus;
    
    private Boolean accountLocked;
    
    private Boolean passwordExpired;
    
    private Boolean forcePasswordChange;
    
    private Integer failedLoginAttempts;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastPasswordChange;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime passwordExpiryDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accountExpiryDate;
    
    private String createdBy;
    
    private String updatedBy;
    
    private Long loginCount;
    
    private String lastIpAddress;
    
    private String securityQuestion;
    
    private Boolean twoFactorEnabled;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Computed fields
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
    public Boolean getIsAdmin() {
        return "A".equals(userType);
    }
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Boolean getIsActive() {
        return "A".equals(userStatus);
    }
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Boolean getCanLogin() {
        return "A".equals(userStatus) && 
               (accountLocked == null || !accountLocked) && 
               (accountExpiryDate == null || accountExpiryDate.isAfter(LocalDateTime.now()));
    }
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Boolean getIsPasswordNearExpiry() {
        return passwordExpiryDate != null && 
               passwordExpiryDate.isBefore(LocalDateTime.now().plusDays(7));
    }
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Boolean getIsHighRiskAccount() {
        return (failedLoginAttempts != null && failedLoginAttempts >= 3) ||
               (lastLoginAt != null && lastLoginAt.isBefore(LocalDateTime.now().minusDays(90))) ||
               getIsPasswordNearExpiry() ||
               (accountExpiryDate != null && accountExpiryDate.isBefore(LocalDateTime.now().plusDays(30)));
    }
}