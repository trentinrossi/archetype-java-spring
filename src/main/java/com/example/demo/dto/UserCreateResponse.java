package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateResponse {
    
    @Schema(description = "Creation result", example = "SUCCESS", required = true)
    private String result;
    
    @Schema(description = "Error message if creation failed", example = "User ID already exists", required = false)
    private String errorMessage;
    
    @Schema(description = "Created user ID", example = "ADMIN001", required = false)
    private String userId;
    
    public static UserCreateResponse success(String userId) {
        return new UserCreateResponse("SUCCESS", null, userId);
    }
    
    public static UserCreateResponse failure(String errorMessage) {
        return new UserCreateResponse("FAILURE", errorMessage, null);
    }
}