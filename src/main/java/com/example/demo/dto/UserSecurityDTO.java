package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.entity.UserSecurity.UserType;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignonRequestDTO {
    
    @NotBlank(message = "Please enter User ID")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "User ID for authentication", example = "ADMIN001", required = true, maxLength = 8)
    private String userId;
    
    @NotBlank(message = "Please enter Password")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "Password for authentication", example = "PASSWORD", required = true, maxLength = 8)
    private String password;
    
    public void setUserId(String userId) {
        this.userId = userId != null ? userId.toUpperCase() : null;
    }
    
    public void setPassword(String password) {
        this.password = password != null ? password.toUpperCase() : null;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SignonResponseDTO {
    
    @Schema(description = "Authentication success flag", example = "true", required = true)
    private boolean success;
    
    @Schema(description = "Response message", example = "Authentication successful", required = true)
    private String message;
    
    @Schema(description = "User type", example = "ADMIN", required = false)
    private UserType userType;
    
    @Schema(description = "Redirect program name", example = "COADM01C", required = false)
    private String redirectProgram;
    
    public static SignonResponseDTO success(UserType userType, String redirectProgram) {
        return new SignonResponseDTO(true, "Authentication successful", userType, redirectProgram);
    }
    
    public static SignonResponseDTO failure(String message) {
        return new SignonResponseDTO(false, message, null, null);
    }
    
    public static SignonResponseDTO wrongPassword() {
        return new SignonResponseDTO(false, "Wrong Password", null, null);
    }
    
    public static SignonResponseDTO userNotFound() {
        return new SignonResponseDTO(false, "User not found", null, null);
    }
    
    public static SignonResponseDTO invalidKey() {
        return new SignonResponseDTO(false, "Invalid key pressed", null, null);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UserSecurityDTO {
    
    @Schema(description = "User ID", example = "ADMIN001", required = true, maxLength = 8)
    private String userId;
    
    @Schema(description = "User password", example = "PASSWORD", required = true, maxLength = 8)
    private String password;
    
    @Schema(description = "User type", example = "ADMIN", required = true)
    private UserType userType;
    
    @Schema(description = "Program name", example = "COSGN00C", required = true)
    private String programName;
    
    @Schema(description = "Transaction ID", example = "CC00", required = true)
    private String transactionId;
    
    @Schema(description = "Active status", example = "true", required = true)
    private Boolean active;
    
    @Schema(description = "Creation timestamp", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
    
    @Schema(description = "User type display name", example = "Admin", required = false)
    private String userTypeDisplayName;
    
    @Schema(description = "Redirect program for user type", example = "COADM01C", required = false)
    private String redirectProgram;
    
    @Schema(description = "Whether user can authenticate", example = "true", required = false)
    private Boolean canAuthenticate;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CreateUserSecurityRequest {
    
    @NotBlank(message = "Please enter User ID")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "User ID", example = "ADMIN001", required = true, maxLength = 8)
    private String userId;
    
    @NotBlank(message = "Please enter Password")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "Password", example = "PASSWORD", required = true, maxLength = 8)
    private String password;
    
    @Schema(description = "User type", example = "ADMIN", required = true)
    private UserType userType;
    
    @Schema(description = "Program name", example = "COSGN00C", required = false)
    private String programName;
    
    @Schema(description = "Transaction ID", example = "CC00", required = false)
    private String transactionId;
    
    @Schema(description = "Active status", example = "true", required = false)
    private Boolean active;
    
    public void setUserId(String userId) {
        this.userId = userId != null ? userId.toUpperCase() : null;
    }
    
    public void setPassword(String password) {
        this.password = password != null ? password.toUpperCase() : null;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateUserSecurityRequest {
    
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "Password", example = "PASSWORD", required = false, maxLength = 8)
    private String password;
    
    @Schema(description = "User type", example = "ADMIN", required = false)
    private UserType userType;
    
    @Schema(description = "Program name", example = "COSGN00C", required = false)
    private String programName;
    
    @Schema(description = "Transaction ID", example = "CC00", required = false)
    private String transactionId;
    
    @Schema(description = "Active status", example = "true", required = false)
    private Boolean active;
    
    public void setPassword(String password) {
        this.password = password != null ? password.toUpperCase() : null;
    }
}