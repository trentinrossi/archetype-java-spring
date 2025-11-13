package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminMenuOptionRequestDto {

    @Schema(description = "Sequential number identifying the menu option", example = "1", required = false)
    @Min(value = 0, message = "Option number must be between 0 and 99")
    @Max(value = 99, message = "Option number must be between 0 and 99")
    private Integer optionNumber;

    @Schema(description = "Descriptive name of the administrative function", example = "User Account Management", required = false)
    @Size(max = 35, message = "Option name must not exceed 35 characters")
    private String optionName;

    @Schema(description = "Name of the program to execute for this option", example = "COUSR00C", required = false)
    @Size(max = 8, message = "Program name must not exceed 8 characters")
    private String programName;

    @Schema(description = "Indicates if option is active or coming soon (dummy)", example = "true", required = false)
    private Boolean isActive;
}
