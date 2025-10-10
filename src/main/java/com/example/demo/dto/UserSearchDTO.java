package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for searching and filtering users")
public class UserSearchDTO {
    
    @Size(max = 8, message = "User ID cannot exceed 8 characters")
    @Schema(description = "User ID to search for", example = "USR00001", required = false)
    private String userId;
    
    @Size(max = 20, message = "First name cannot exceed 20 characters")
    @Schema(description = "First name to search for", example = "John", required = false)
    private String firstName;
    
    @Size(max = 20, message = "Last name cannot exceed 20 characters")
    @Schema(description = "Last name to search for", example = "Doe", required = false)
    private String lastName;
    
    @Size(max = 1, message = "User type must be 1 character")
    @Schema(description = "User type to filter by", example = "A", required = false)
    private String userType;
    
    @Schema(description = "Page number for pagination (0-based)", example = "0", required = false)
    private Integer page = 0;
    
    @Schema(description = "Page size for pagination", example = "10", required = false)
    private Integer size = 10;
    
    @Schema(description = "Sort field", example = "userId", required = false)
    private String sortBy = "userId";
    
    @Schema(description = "Sort direction", example = "ASC", required = false)
    private String sortDirection = "ASC";
}