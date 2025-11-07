package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for user response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    
    @Schema(description = "Unique user identifier", example = "user123")
    private String userId;
    
    @Schema(description = "Type of user", example = "REGULAR")
    private UserType userType;
    
    @Schema(description = "Display name for user type", example = "Regular User")
    private String userTypeDisplayName;
    
    @Schema(description = "Whether the user is an administrator", example = "false")
    private Boolean isAdmin;
    
    @Schema(description = "Whether the user can view all cards", example = "false")
    private Boolean canViewAllCards;
    
    @Schema(description = "Set of account IDs this user has access to")
    private Set<String> accountIds;
    
    @Schema(description = "Number of accounts this user has access to", example = "2")
    private Integer accountCount;
    
    @Schema(description = "Timestamp when the user was created")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated")
    private LocalDateTime updatedAt;
}
