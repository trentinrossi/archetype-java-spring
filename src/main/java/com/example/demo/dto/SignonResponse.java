package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignonResponse {
    
    @Schema(description = "Authentication result", example = "SUCCESS", required = true)
    private String result;
    
    @Schema(description = "User type (A=Admin, R=Regular)", example = "A", required = false)
    private String userType;
    
    @Schema(description = "Target module for user", example = "COADM01C", required = false)
    private String targetModule;
    
    @Schema(description = "Error message if authentication failed", example = "Invalid credentials", required = false)
    private String errorMessage;
    
    public static SignonResponse success(String userType, String targetModule) {
        return new SignonResponse("SUCCESS", userType, targetModule, null);
    }
    
    public static SignonResponse failure(String errorMessage) {
        return new SignonResponse("FAILURE", null, null, errorMessage);
    }
}