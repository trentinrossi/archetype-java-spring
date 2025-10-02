package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignOnResponse {
    
    @Schema(description = "Authentication success status", example = "true", required = true)
    private boolean success;
    
    @Schema(description = "User type", example = "A", required = false)
    private String userType;
    
    @Schema(description = "Authentication result message", example = "Login successful", required = true)
    private String message;
}