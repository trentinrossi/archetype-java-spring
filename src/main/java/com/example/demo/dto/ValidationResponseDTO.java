package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponseDTO {
    
    @Schema(description = "Validation result", example = "true", required = true)
    private boolean valid;
    
    @Schema(description = "Validation message", example = "Credentials are valid", required = true)
    private String message;
    
    public static ValidationResponseDTO success() {
        return new ValidationResponseDTO(true, "Credentials are valid");
    }
    
    public static ValidationResponseDTO failure(String message) {
        return new ValidationResponseDTO(false, message);
    }
}