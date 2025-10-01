package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {
    
    @Size(max = 8, message = "User ID cannot exceed 8 characters")
    @Schema(description = "User ID to search for", example = "USR001", required = false, maxLength = 8)
    private String id;
}