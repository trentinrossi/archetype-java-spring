package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    @Schema(description = "Unique identifier for the user", example = "USR001", required = true)
    private String userId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "Type of user - A for Admin, R for Regular", example = "A", required = true)
    private String userType;
    
    @Schema(description = "Display name for user type", example = "Administrator", required = true)
    private String userTypeDisplayName;
    
    @Schema(description = "Indicates if user has admin privileges", example = "true", required = true)
    private boolean isAdmin;
    
    @Schema(description = "Indicates if user has regular privileges", example = "false", required = true)
    private boolean isRegular;
    
    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}