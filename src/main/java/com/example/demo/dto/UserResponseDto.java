package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UserResponseDto
 * 
 * Response DTO for user information.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @Schema(description = "Unique user identifier", example = "USR12345678901234567")
    private String userId;

    @Schema(description = "Type of user (ADMIN or REGULAR)", example = "ADMIN")
    private String userType;

    @Schema(description = "Display name for user type", example = "Administrator")
    private String userTypeDisplayName;

    @Schema(description = "Indicates if user has admin privileges", example = "true")
    private Boolean isAdmin;

    @Schema(description = "Indicates if user can view all credit cards without context", example = "true")
    private Boolean canViewAllCards;

    @Schema(description = "Indicates if user requires account context to view cards", example = "false")
    private Boolean requiresAccountContext;

    @Schema(description = "List of account IDs accessible to this user")
    private List<String> accessibleAccountIds;

    @Schema(description = "Number of accounts accessible to this user", example = "5")
    private Integer accessibleAccountCount;

    @Schema(description = "Timestamp when the user was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the user was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
