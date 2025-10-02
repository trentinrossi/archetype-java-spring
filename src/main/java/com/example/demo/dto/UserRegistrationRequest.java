package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @NotBlank(message = "User ID is required")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "Unique user ID", example = "USER001", required = true)
    private String userId;
    
    @NotBlank(message = "Password is required")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "User password", example = "password", required = true)
    private String password;
    
    @NotBlank(message = "User type is required")
    @Pattern(regexp = "^[AR]$", message = "User type must be A (Admin) or R (Regular)")
    @Schema(description = "User type (A=Admin, R=Regular)", example = "A", required = true)
    private String userType;
    
    public void setUserId(String userId) {
        this.userId = userId != null ? userId.toUpperCase() : null;
    }
    
    public void setUserType(String userType) {
        this.userType = userType != null ? userType.toUpperCase() : null;
    }
}