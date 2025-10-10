package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a new user")
public class CreateUserDTO {
    
    @NotBlank(message = "User ID can NOT be empty...")
    @Size(max = 8, message = "User ID cannot exceed 8 characters")
    @Schema(description = "User ID", example = "USR00001", required = true)
    private String userId;
    
    @NotBlank(message = "First Name can NOT be empty...")
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Schema(description = "First name", example = "John", required = true)
    private String firstName;
    
    @NotBlank(message = "Last Name can NOT be empty...")
    @Size(max = 20, message = "Last name cannot exceed 20 characters")
    @Schema(description = "Last name", example = "Doe", required = true)
    private String lastName;
    
    @NotBlank(message = "Password can NOT be empty...")
    @Size(max = 8, message = "Password cannot exceed 8 characters")
    @Schema(description = "Password", example = "password", required = true)
    private String password;
    
    @NotBlank(message = "User Type can NOT be empty...")
    @Size(max = 1, message = "User type must be 1 character")
    @Schema(description = "User type (A=Admin, U=User)", example = "A", required = true)
    private String userType;
}