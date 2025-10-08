package com.example.demo.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @Size(max = 20, message = "First name must not exceed 20 characters")
    private String firstName;
    
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    private String lastName;
    
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @Pattern(regexp = "^[AR]$", message = "User type must be 'A' (Admin) or 'R' (Regular)")
    private String userType;
    
    @Pattern(regexp = "^[AISD]$", message = "User status must be 'A' (Active), 'I' (Inactive), 'S' (Suspended), or 'D' (Deleted)")
    private String userStatus;
    
    private String updatedBy;
    
    private String securityQuestion;
    
    private String securityAnswer;
    
    private Boolean twoFactorEnabled;
    
    private Boolean forcePasswordChange;
    
    private Boolean accountLocked;
}