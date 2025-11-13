package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuOptionRequestDto {

    @Schema(description = "Sequential number of the menu option", example = "1", required = true)
    @NotNull(message = "Option number is required")
    @Min(value = 1, message = "Option number must be at least 1")
    private Integer optionNumber;

    @Schema(description = "Display name of the menu option", example = "User Management", required = true)
    @NotNull(message = "Option name is required")
    @NotBlank(message = "Option name is required")
    @Size(max = 40, message = "Option name must not exceed 40 characters")
    private String optionName;

    @Schema(description = "Program to execute when option is selected", example = "COUSR00C", required = true)
    @NotNull(message = "Program name is required")
    @NotBlank(message = "Program name is required")
    @Size(max = 8, message = "Program name must not exceed 8 characters")
    private String programName;

    @Schema(description = "User type required to access this option (A=Admin, U=User)", example = "A", required = true)
    @NotNull(message = "User type required is required")
    @NotBlank(message = "User type required is required")
    @Size(max = 1, message = "User type required must not exceed 1 character")
    private String userTypeRequired;

    @Schema(description = "Total number of menu options available", example = "10", required = true)
    @NotNull(message = "Option count is required")
    @Min(value = 1, message = "Option count must be at least 1")
    private Integer optionCount;

    @Schema(description = "Indicates if option is active", example = "true", required = false)
    private Boolean isActive = true;

    @Schema(description = "Display order of the option", example = "1", required = false)
    private Integer displayOrder;
}
