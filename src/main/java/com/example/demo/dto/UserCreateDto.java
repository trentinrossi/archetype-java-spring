package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "User ID is mandatory")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "Unique user identifier", example = "USR00001", required = true)
    private String userId;
    
    @NotBlank(message = "First name is mandatory")
    @Size(max = 20, message = "First name must not exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @NotBlank(message = "Password is mandatory")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "User password", example = "pass123", required = true)
    private String password;
    
    @NotBlank(message = "User type is mandatory")
    @Pattern(regexp = "^[AU]$", message = "User type must be A (Admin) or U (User)")
    @Schema(description = "User type (A=Admin, U=User)", example = "U", required = true)
    private String userType;
}