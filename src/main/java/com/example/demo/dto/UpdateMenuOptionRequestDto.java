package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuOptionRequestDto {

    @Schema(description = "Sequential number of the menu option", example = "1", required = false)
    @Min(value = 0, message = "Option number must be between 0 and 99")
    @Max(value = 99, message = "Option number must be between 0 and 99")
    private Integer optionNumber;

    @Schema(description = "Display name of the menu option", example = "View Account", required = false)
    @Size(max = 40, message = "Option name must not exceed 40 characters")
    private String optionName;

    @Schema(description = "Program to execute when option is selected", example = "COACTVW", required = false)
    @Size(max = 8, message = "Program name must not exceed 8 characters")
    private String programName;

    @Schema(description = "User type required to access this option", example = "U", required = false)
    @Size(min = 1, max = 1, message = "User type required must be exactly 1 character")
    @Pattern(regexp = "[AUR]", message = "User type required must be A, U, or R")
    private String userTypeRequired;

    @Schema(description = "Total number of menu options available", example = "10", required = false)
    @Min(value = 0, message = "Option count must be non-negative")
    private Integer optionCount;
}
