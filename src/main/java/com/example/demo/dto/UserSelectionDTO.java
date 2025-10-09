package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSelectionDTO {
    
    @Schema(description = "Unique identifier for the user", example = "USR001", required = true)
    private String userId;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "Type of user - A for Admin, R for Regular", example = "A", required = true)
    private String userType;
    
    @Schema(description = "Display name for user type", example = "Administrator", required = true)
    private String userTypeDisplayName;
    
    @Schema(description = "Indicates if user is currently active/selectable", example = "true", required = true)
    private boolean selectable;
    
    @Schema(description = "Additional context information for selection", example = "Available for assignment", required = false)
    private String selectionContext;
}