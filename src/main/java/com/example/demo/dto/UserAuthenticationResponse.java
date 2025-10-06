package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationResponse {
    
    @Schema(description = "Unique user identifier", example = "USER001", required = true)
    private String userId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "User type: A for Admin, U for User", example = "U", required = true)
    private String userType;
    
    @Schema(description = "Display name of the user type", example = "User", required = true)
    private String userTypeDisplayName;
    
    @Schema(description = "Indicates if user is an admin", example = "false", required = true)
    private boolean isAdmin;
    
    @Schema(description = "Authentication status", example = "true", required = true)
    private boolean authenticated;
    
    @Schema(description = "Authentication timestamp", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime authenticationTime;
}