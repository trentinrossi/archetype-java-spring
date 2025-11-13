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

    @Schema(description = "Unique identifier for the menu option")
    private Long id;

    @Schema(description = "Sequential number identifying the menu option", example = "1")
    private Integer optionNumber;

    @Schema(description = "Descriptive name of the administrative function", example = "User Account Management")
    private String optionName;

    @Schema(description = "Name of the program to execute for this option", example = "COUSR00C")
    private String programName;

    @Schema(description = "Indicates if option is active or coming soon (dummy)", example = "true")
    private Boolean isActive;

    @Schema(description = "Indicates if option is coming soon (not yet implemented)", example = "false")
    private Boolean isComingSoon;

    @Schema(description = "Display text for the menu option", example = "01 - User Account Management")
    private String displayText;

    @Schema(description = "Indicates if the option can be executed", example = "true")
    private Boolean canExecute;

    @Schema(description = "Timestamp when the menu option was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the menu option was last updated")
    private LocalDateTime updatedAt;
}
