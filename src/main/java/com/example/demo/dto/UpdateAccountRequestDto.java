package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing account
 * Note: Account ID cannot be changed after creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {
    
    @Schema(description = "Additional account information or metadata", 
            example = "Updated account information")
    private String notes;
}
