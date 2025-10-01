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
@Schema(description = "User creation/update request")
public class UserRequest {
    
    @NotBlank(message = "User ID can NOT be empty...")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "User ID", example = "USR001", required = true)
    private String secUsrId;
    
    @NotBlank(message = "First Name can NOT be empty...")
    @Size(max = 20, message = "First Name must not exceed 20 characters")
    @Schema(description = "User first name", example = "John", required = true)
    private String secUsrFname;
    
    @NotBlank(message = "Last Name can NOT be empty...")
    @Size(max = 20, message = "Last Name must not exceed 20 characters")
    @Schema(description = "User last name", example = "Doe", required = true)
    private String secUsrLname;
    
    @NotBlank(message = "Password can NOT be empty...")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "User password", example = "pass123", required = true)
    private String secUsrPwd;
    
    @NotBlank(message = "User Type can NOT be empty...")
    @Size(max = 1, message = "User Type must be exactly 1 character")
    @Schema(description = "User type (A=Admin, U=User)", example = "U", required = true)
    private String secUsrType;
}