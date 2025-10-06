package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "First name of the user", example = "John", required = false)
    private String firstName;
    
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String lastName;
    
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "User password", example = "pass123", required = false)
    private String password;
    
    @Pattern(regexp = "^[AU]$", message = "User type must be 'A' (Admin) or 'U' (User)")
    @Schema(description = "User type: A for Admin, U for User", example = "U", required = false)
    private String userType;
}