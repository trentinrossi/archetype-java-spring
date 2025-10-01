package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {
    @Schema(description = "User ID", example = "USR00001", required = true)
    private String userId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "User type (A=Admin, U=User)", example = "U", required = true)
    private String userType;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserCreateDto {
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

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserUpdateDto {
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

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserDetailDto {
    @Schema(description = "User ID", example = "USR00001", required = true)
    private String userId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "User type (A=Admin, U=User)", example = "U", required = true)
    private String userType;
    
    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserDeleteDto {
    @Schema(description = "User ID", example = "USR00001", required = true)
    private String userId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "User type (A=Admin, U=User)", example = "U", required = true)
    private String userType;
}