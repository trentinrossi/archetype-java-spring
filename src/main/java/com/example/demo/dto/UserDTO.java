package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    @NotBlank(message = "User ID cannot be empty")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    private String userId;
    
    @NotBlank(message = "First Name cannot be empty")
    @Size(max = 20, message = "First Name must not exceed 20 characters")
    private String firstName;
    
    @NotBlank(message = "Last Name cannot be empty")
    @Size(max = 20, message = "Last Name must not exceed 20 characters")
    private String lastName;
    
    @NotBlank(message = "Password cannot be empty")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    private String password;
    
    @NotBlank(message = "User Type cannot be empty")
    @Pattern(regexp = "[AR]", message = "User Type must be 'A' (Admin) or 'R' (Regular)")
    private String userType;
    
    private String createdAt;
    private String updatedAt;
}
