package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @Schema(description = "Internal user ID", example = "1")
    private Long id;

    @Schema(description = "Unique user identifier", example = "USER001")
    private String userId;

    @Schema(description = "Type of user (ADMIN or REGULAR)", example = "REGULAR")
    private UserType userType;

    @Schema(description = "User type display name", example = "Regular")
    private String userTypeDisplayName;

    @Schema(description = "Username for login", example = "johndoe")
    private String username;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "Whether user is an admin", example = "false")
    private Boolean isAdmin;

    @Schema(description = "Whether user can view all cards", example = "false")
    private Boolean canViewAllCards;

    @Schema(description = "Number of accounts associated with this user", example = "2")
    private Integer accountCount;

    @Schema(description = "Timestamp when the user was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the user was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
