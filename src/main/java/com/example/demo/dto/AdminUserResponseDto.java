package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponseDto {

    @Schema(description = "Unique identifier for the admin user", example = "ADMIN001")
    private String userId;

    @Schema(description = "Indicates if user has been authenticated as admin", example = "true")
    private Boolean authenticationStatus;

    @Schema(description = "Indicates if user is currently authenticated", example = "true")
    private Boolean isAuthenticated;

    @Schema(description = "Number of menu options accessible to this admin user", example = "5")
    private Integer menuOptionCount;

    @Schema(description = "Timestamp when the admin user was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the admin user was last updated")
    private LocalDateTime updatedAt;
}
