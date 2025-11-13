package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuOptionRequestDto {

    @Schema(description = "Sequential number of the menu option", example = "1", required = false)
    @Min(value = 1, message = "Option number must be at least 1")
    private Integer optionNumber;

    @Schema(description = "Display name of the menu option", example = "User Management", required = false)
    @Size(max = 40, message = "Option name must not exceed 40 characters")
    private String optionName;

    @Schema(description = "Program to execute when option is selected", example = "COUSR00C", required = false)
    @Size(max = 8, message = "Program name must not exceed 8 characters")
    private String programName;

    @Schema(description = "User type required to access this option (A=Admin, U=User)", example = "A", required = false)
    @Size(max = 1, message = "User type required must not exceed 1 character")
    private String userTypeRequired;

    @Schema(description = "Total number of menu options available", example = "10", required = false)
    @Min(value = 1, message = "Option count must be at least 1")
    private Integer optionCount;

    @Schema(description = "Indicates if option is active", example = "true", required = false)
    private Boolean isActive;

    @Schema(description = "Display order of the option", example = "1", required = false)
    private Integer displayOrder;
}
