package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    @Schema(description = "Unique identifier of the user", example = "USR001", required = true)
    private String id;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "Type of user - 'A' for Admin, 'U' for User", example = "U", required = true)
    private String userType;
    
    @Schema(description = "Display name of the user type", example = "User", required = true)
    private String userTypeDisplayName;
    
    @Schema(description = "Indicates if the user is an admin", example = "false", required = true)
    private boolean isAdmin;
    
    @Schema(description = "Indicates if the user is a regular user", example = "true", required = true)
    private boolean isRegularUser;
    
    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}