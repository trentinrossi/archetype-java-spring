package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    
    @Size(max = 20, message = "First name must not exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = false)
    private String firstName;
    
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String lastName;
    
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "Password for the user", example = "newpass", required = false)
    private String password;
    
    @Pattern(regexp = "^[AR]$", message = "User type must be either 'A' (Admin) or 'R' (Regular)")
    @Schema(description = "Type of user - A for Admin, R for Regular", example = "A", required = false, allowableValues = {"A", "R"})
    private String userType;
}