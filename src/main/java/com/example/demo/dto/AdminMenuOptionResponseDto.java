package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenuOptionResponseDto {

    @Schema(description = "Unique identifier for the menu option", example = "1")
    private Long id;

    @Schema(description = "Sequential number identifying the menu option", example = "1")
    private Integer optionNumber;

    @Schema(description = "Descriptive name of the administrative function", example = "User Account Management")
    private String optionName;

    @Schema(description = "Name of the program to execute for this option", example = "COUSR00C")
    private String programName;

    @Schema(description = "Indicates if option is active or coming soon (dummy)", example = "true")
    private Boolean isActive;

    @Schema(description = "Display status of the menu option", example = "Active")
    private String statusDisplay;

    @Schema(description = "ID of the admin user associated with this menu option", example = "ADMIN001")
    private String adminUserId;

    @Schema(description = "Formatted option display text", example = "01 - User Account Management")
    private String optionDisplay;

    @Schema(description = "Timestamp when the menu option was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the menu option was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;

    public String getStatusDisplay() {
        return isActive != null && isActive ? "Active" : "Coming Soon";
    }

    public String getOptionDisplay() {
        if (optionNumber != null && optionName != null) {
            return String.format("%02d - %s", optionNumber, optionName);
        }
        return optionName;
    }
}
