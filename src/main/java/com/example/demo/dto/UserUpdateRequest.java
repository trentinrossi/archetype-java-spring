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
public class UserUpdateRequest {
    
    @NotBlank(message = "User ID is required")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "User ID", example = "USER001", required = true)
    private String userId;
    
    @Size(max = 20, message = "First name must not exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = false)
    private String firstName;
    
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String lastName;
    
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "User password", example = "password", required = false)
    private String password;
    
    @Size(min = 1, max = 1, message = "User type must be exactly 1 character")
    @Pattern(regexp = "[AR]", message = "User type must be 'A' (Admin) or 'R' (Regular)")
    @Schema(description = "User type - A for Admin, R for Regular", example = "A", required = false)
    private String userType;
}