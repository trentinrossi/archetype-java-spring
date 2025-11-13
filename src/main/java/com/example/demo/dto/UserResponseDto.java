package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @Schema(description = "Unique identifier for the user", example = "USER001")
    private String userId;

    @Schema(description = "Type of user (A for admin, U for user)", example = "U")
    private String userType;

    @Schema(description = "Whether the user is authenticated", example = "true")
    private Boolean authenticated;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "Indicates if user is an admin", example = "false")
    private Boolean isAdmin;

    @Schema(description = "Indicates if user is a regular user", example = "true")
    private Boolean isRegularUser;

    @Schema(description = "Timestamp when the user was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the user was last updated")
    private LocalDateTime updatedAt;
}
