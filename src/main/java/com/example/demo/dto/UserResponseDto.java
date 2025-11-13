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

    @Schema(description = "Internal database ID", example = "1")
    private Long id;

    @Schema(description = "Type of user (regular or admin)", example = "A")
    private String userType;

    @Schema(description = "Type of user display name", example = "Admin")
    private String userTypeDisplay;

    @Schema(description = "Whether the user is authenticated", example = "true")
    private Boolean authenticated;

    @Schema(description = "Authentication status display", example = "Authenticated")
    private String authenticatedDisplay;

    @Schema(description = "Unique identifier for the user", example = "USR00001")
    private String userId;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "Indicates if user has admin access", example = "true")
    private Boolean isAdmin;

    @Schema(description = "Timestamp when user was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when user was last updated")
    private LocalDateTime updatedAt;

    public String getUserTypeDisplay() {
        if (userType == null) {
            return null;
        }
        switch (userType.toUpperCase()) {
            case "A":
                return "Admin";
            case "R":
                return "Regular";
            default:
                return "Unknown";
        }
    }

    public String getAuthenticatedDisplay() {
        if (authenticated == null) {
            return "Not Authenticated";
        }
        return authenticated ? "Authenticated" : "Not Authenticated";
    }

    public String getFullName() {
        if (firstName == null && lastName == null) {
            return null;
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    public Boolean getIsAdmin() {
        return userType != null && "A".equalsIgnoreCase(userType);
    }
}
