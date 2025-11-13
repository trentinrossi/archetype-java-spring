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

    @Schema(description = "Admin menu option database ID", example = "1")
    private Long id;

    @Schema(description = "Sequential number of the admin menu option", example = "1")
    private Integer optionNumber;

    @Schema(description = "Display name of the admin menu option", example = "User Management")
    private String optionName;

    @Schema(description = "Program to execute when option is selected", example = "COUSR00C")
    private String programName;

    @Schema(description = "Indicates if option is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Display order of the option", example = "1")
    private Integer displayOrder;

    @Schema(description = "Indicates if option is coming soon", example = "false")
    private Boolean isComingSoon;

    @Schema(description = "Status display text", example = "Active")
    private String statusDisplay;

    @Schema(description = "Timestamp when option was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when option was last updated")
    private LocalDateTime updatedAt;
}
