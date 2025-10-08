package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    
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
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @NotBlank(message = "User type cannot be blank")
    @Pattern(regexp = "^[AR]$", message = "User type must be 'A' (Admin) or 'R' (Regular)")
    private String userType;
    
    private String createdBy;
    
    private String securityQuestion;
    
    private String securityAnswer;
    
    private Boolean twoFactorEnabled = false;
}