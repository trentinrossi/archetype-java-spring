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

    @Schema(description = "Timestamp when the admin user was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the admin user was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
