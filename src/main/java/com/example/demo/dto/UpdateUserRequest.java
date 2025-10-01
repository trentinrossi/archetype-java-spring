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
public class UpdateUserRequest {
    
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = false)
    private String secUsrFname;
    
    @Size(max = 20, message = "Last name cannot exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String secUsrLname;
    
    @Size(max = 8, message = "Password cannot exceed 8 characters")
    @Schema(description = "User password", example = "pass123", required = false)
    private String secUsrPwd;
    
    @Pattern(regexp = "[AU]", message = "User type must be 'A' for Admin or 'U' for User")
    @Schema(description = "User type - A for Admin, U for User", example = "U", required = false)
    private String secUsrType;
}