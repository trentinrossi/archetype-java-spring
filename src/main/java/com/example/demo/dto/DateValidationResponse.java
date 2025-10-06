package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateValidationResponse {
    
    @Schema(description = "Validation ID", example = "1", required = true)
    private Long validationId;
    
    @Schema(description = "Input date that was validated", example = "20231201", required = true)
    private String inputDate;
    
    @Schema(description = "Date format pattern used", example = "YYYYMMDD", required = true)
    private String formatPattern;
    
    @Schema(description = "Validation result", example = "true", required = true)
    private Boolean validationResult;
    
    @Schema(description = "Severity code", example = "INFO", required = true)
    private String severityCode;
    
    @Schema(description = "Message number", example = "1001", required = true)
    private Integer messageNumber;
    
    @Schema(description = "Result message", example = "Valid date", required = true)
    private String resultMessage;
    
    @Schema(description = "Test date used in validation", example = "20231201", required = false)
    private String testDate;
    
    @Schema(description = "Mask used for validation", example = "YYYYMMDD", required = false)
    private String maskUsed;
    
    @Schema(description = "Lillian date output", example = "738521", required = false)
    private Long lillianDateOutput;
    
    @Schema(description = "Error type if validation failed", example = "INVALID_DATE", required = false)
    private String errorType;
    
    @Schema(description = "Structured message following CEEDAYS format", example = "INFO Mesg Code:1001Valid date      TstDate:20231201 Mask used:YYYYMMDD    ", required = true)
    private String structuredMessage;
}