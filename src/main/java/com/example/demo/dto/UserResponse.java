package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User response DTO")
public class UserResponse {
    
    @Schema(description = "User ID", example = "USR00001", required = true)
    private String userId;
    
    @Schema(description = "First name", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "Last name", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "Full name", example = "John Doe")
    private String fullName;
    
    @Schema(description = "Password", example = "password", required = true)
    private String password;
    
    @Schema(description = "User type", example = "A", required = true)
    private String userType;
    
    @Schema(description = "Creation timestamp", example = "2024-01-01T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp", example = "2024-01-01T10:00:00")
    private LocalDateTime updatedAt;
}