package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOptionResponseDto {

    @Schema(description = "Menu option database ID", example = "1")
    private Long id;

    @Schema(description = "Sequential number of the menu option", example = "1")
    private Integer optionNumber;

    @Schema(description = "Display name of the menu option", example = "User Management")
    private String optionName;

    @Schema(description = "Program to execute when option is selected", example = "COUSR00C")
    private String programName;

    @Schema(description = "User type required to access this option (A=Admin, U=User)", example = "A")
    private String userTypeRequired;

    @Schema(description = "Total number of menu options available", example = "10")
    private Integer optionCount;

    @Schema(description = "Indicates if option is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Display order of the option", example = "1")
    private Integer displayOrder;

    @Schema(description = "Indicates if option is admin only", example = "true")
    private Boolean isAdminOnly;

    @Schema(description = "Indicates if option is accessible by regular users", example = "false")
    private Boolean isUserAccessible;

    @Schema(description = "Indicates if option is coming soon", example = "false")
    private Boolean isComingSoon;

    @Schema(description = "Access level display text", example = "Admin Only")
    private String accessLevelDisplay;

    @Schema(description = "Status display text", example = "Active")
    private String statusDisplay;

    @Schema(description = "Timestamp when option was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when option was last updated")
    private LocalDateTime updatedAt;
}
