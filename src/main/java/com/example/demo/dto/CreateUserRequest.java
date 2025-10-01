package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    
    @NotBlank(message = "User ID is mandatory")
    @Size(max = 8, message = "User ID cannot exceed 8 characters")
    @Schema(description = "Unique identifier for the user", example = "USR001", required = true, maxLength = 8)
    private String id;
    
    @NotBlank(message = "First name is mandatory")
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = true, maxLength = 20)
    private String firstName;
    
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 20, message = "Last name cannot exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = true, maxLength = 20)
    private String lastName;
    
    @NotBlank(message = "Password is mandatory")
    @Size(max = 8, message = "Password cannot exceed 8 characters")
    @Schema(description = "Password for the user", example = "pass123", required = true, maxLength = 8)
    private String password;
    
    @NotBlank(message = "User type is mandatory")
    @Pattern(regexp = "^[AU]$", message = "User type must be 'A' for Admin or 'U' for User")
    @Schema(description = "Type of user - 'A' for Admin, 'U' for User", example = "U", required = true, allowableValues = {"A", "U"})
    private String userType;
}