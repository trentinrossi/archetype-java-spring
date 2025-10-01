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
public class CreateUserRequest {
    
    @NotBlank(message = "User ID is mandatory")
    @Size(max = 8, message = "User ID cannot exceed 8 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "User ID must be alphanumeric")
    @Schema(description = "Unique user identifier", example = "USR00001", required = true)
    private String secUsrId;
    
    @NotBlank(message = "First name is mandatory")
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = true)
    private String secUsrFname;
    
    @NotBlank(message = "Last name is mandatory")
    @Size(max = 20, message = "Last name cannot exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String secUsrLname;
    
    @NotBlank(message = "Password is mandatory")
    @Size(max = 8, message = "Password cannot exceed 8 characters")
    @Schema(description = "User password", example = "pass123", required = true)
    private String secUsrPwd;
    
    @NotBlank(message = "User type is mandatory")
    @Pattern(regexp = "[AU]", message = "User type must be 'A' for Admin or 'U' for User")
    @Schema(description = "User type - A for Admin, U for User", example = "U", required = true)
    private String secUsrType;
}