package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    
    @Size(max = 20, message = "First name must not exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = false)
    private String firstName;
    
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String lastName;
    
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "User password", example = "password", required = false)
    private String password;
    
    @Size(max = 1, message = "User type must be a single character")
    @Schema(description = "User type (A=Admin, R=Regular)", example = "R", required = false)
    private String userType;
}