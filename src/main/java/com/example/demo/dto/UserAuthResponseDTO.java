package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponseDTO {
    
    @Schema(description = "Unique identifier for the user", example = "USR001", required = true)
    private String userId;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "Type of user - A for Admin, R for Regular", example = "A", required = true)
    private String userType;
    
    @Schema(description = "Display name for user type", example = "Administrator", required = true)
    private String userTypeDisplayName;
    
    @Schema(description = "Indicates if user has admin privileges", example = "true", required = true)
    private boolean isAdmin;
    
    @Schema(description = "Indicates if authentication was successful", example = "true", required = true)
    private boolean authenticated;
    
    @Schema(description = "Authentication timestamp", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime authenticatedAt;
}