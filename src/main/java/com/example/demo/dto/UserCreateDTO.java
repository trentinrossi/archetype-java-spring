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
public class UserCreateDTO {
    
    @NotBlank(message = "User ID is required")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "Unique identifier for the user", example = "USR001", required = true)
    private String userId;
    
    @NotBlank(message = "First name is required")
    @Size(max = 20, message = "First name must not exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @NotBlank(message = "Password is required")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "Password for the user", example = "pass123", required = true)
    private String password;
    
    @NotBlank(message = "User type is required")
    @Pattern(regexp = "^[AR]$", message = "User type must be either 'A' (Admin) or 'R' (Regular)")
    @Schema(description = "Type of user - A for Admin, R for Regular", example = "A", required = true, allowableValues = {"A", "R"})
    private String userType;
}