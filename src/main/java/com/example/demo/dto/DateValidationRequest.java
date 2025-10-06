package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateValidationRequest {
    
    @NotBlank(message = "Input date is required")
    @Size(max = 10, message = "Input date must not exceed 10 characters")
    @Schema(description = "Input date to be validated", example = "20231201", required = true)
    private String inputDate;
    
    @NotBlank(message = "Date format pattern is required")
    @Size(max = 10, message = "Date format pattern must not exceed 10 characters")
    @Schema(description = "Date format pattern for validation", example = "YYYYMMDD", required = true)
    private String formatPattern;
}