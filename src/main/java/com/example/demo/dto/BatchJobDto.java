package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Batch Job - Represents a batch job submitted for report generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchJobDto {

    @Schema(description = "Name of the batch job", example = "TRANSACTION_REPORT_JOB_20231231", required = true)
    private String jobName;

    @Schema(description = "JCL statements for job execution (up to 1000 lines)", required = true)
    private List<String> jclLines;

    @Schema(description = "Start date parameter passed to batch job", example = "2023-01-01", required = true)
    private String startDateParameter;

    @Schema(description = "End date parameter passed to batch job", example = "2023-12-31", required = true)
    private String endDateParameter;

    @Schema(description = "Job status", example = "SUBMITTED")
    private String status;

    @Schema(description = "Job submission timestamp", example = "2023-12-31T14:30:00")
    private String submittedAt;
}
