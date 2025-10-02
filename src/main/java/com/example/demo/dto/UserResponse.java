package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    @Schema(description = "Unique user identifier", example = "USER001", required = true)
    private String userId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "User type (A=Admin, R=Regular)", example = "R", required = false)
    private String userType;
    
    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the user was last updated", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
}