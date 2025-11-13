package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    @Schema(description = "Authentication success status", example = "true")
    private Boolean success;

    @Schema(description = "Authentication message", example = "Login successful")
    private String message;

    @Schema(description = "User information after successful login")
    private UserResponseDto user;

    @Schema(description = "Session token or identifier", example = "session-token-123")
    private String sessionToken;

    public LoginResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponseDto(Boolean success, String message, UserResponseDto user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
