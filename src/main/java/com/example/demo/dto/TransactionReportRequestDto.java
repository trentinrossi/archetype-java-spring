package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Transaction Report Request - Represents a transaction report request with date range and report type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportRequestDto {

    @Schema(description = "Type of report: Monthly, Yearly, or Custom", example = "Monthly", required = true)
    @NotBlank(message = "At least one report type must be selected")
    @Pattern(regexp = "Monthly|Yearly|Custom", message = "Report type must be Monthly, Yearly, or Custom")
    private String reportType;

    @Schema(description = "Start date for the report in YYYY-MM-DD format", example = "2023-01-01", required = true)
    @NotBlank(message = "Start date must be entered")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Start date must be in format YYYY-MM-DD")
    private String startDate;

    @Schema(description = "End date for the report in YYYY-MM-DD format", example = "2023-12-31", required = true)
    @NotBlank(message = "End date must be entered")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "End date must be in format YYYY-MM-DD")
    private String endDate;

    @Schema(description = "User confirmation to proceed with report generation (Y/N)", example = "Y", required = true)
    @NotBlank(message = "Confirmation status must be entered")
    @Pattern(regexp = "Y|N", message = "Confirmation status must be Y or N")
    private String confirmationStatus;
}
