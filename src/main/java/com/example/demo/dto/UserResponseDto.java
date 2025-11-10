package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user response data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;
    
    @Schema(description = "Username of the user", example = "john.doe")
    private String username;
    
    @Schema(description = "Type of user", example = "REGULAR")
    private UserType userType;
    
    @Schema(description = "Display name of user type", example = "Regular User")
    private String userTypeDisplayName;
    
    @Schema(description = "Account ID associated with the user", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "First name of the user", example = "John")
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;
    
    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;
    
    @Schema(description = "Whether the user is active", example = "true")
    private Boolean active;
    
    @Schema(description = "Whether the user is an administrator", example = "false")
    private Boolean isAdmin;
    
    @Schema(description = "Whether the user can view all cards", example = "false")
    private Boolean canViewAllCards;
    
    @Schema(description = "Timestamp when the user was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
