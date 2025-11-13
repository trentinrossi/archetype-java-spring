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

    @Schema(description = "Unique identifier for the menu option")
    private Long id;

    @Schema(description = "Sequential number of the menu option", example = "1")
    private Integer optionNumber;

    @Schema(description = "Display name of the menu option", example = "View Account")
    private String optionName;

    @Schema(description = "Program to execute when option is selected", example = "COACTVW")
    private String programName;

    @Schema(description = "User type required to access this option (A for admin, U for user)", example = "U")
    private String userTypeRequired;

    @Schema(description = "Total number of menu options available", example = "10")
    private Integer optionCount;

    @Schema(description = "Indicates if this option is admin only", example = "false")
    private Boolean isAdminOnly;

    @Schema(description = "Indicates if this option is accessible to regular users", example = "true")
    private Boolean isUserAccessible;

    @Schema(description = "Display text for the menu option", example = "01 - View Account")
    private String displayText;

    @Schema(description = "Timestamp when the menu option was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the menu option was last updated")
    private LocalDateTime updatedAt;
}
