package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignOnRequest {
    
    @NotBlank(message = "User ID is required")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "User ID for authentication", example = "USER001", required = true)
    private String userId;
    
    @NotBlank(message = "Password is required")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "User password", example = "password", required = true)
    private String password;
}