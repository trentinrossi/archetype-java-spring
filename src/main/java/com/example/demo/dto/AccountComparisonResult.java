package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountComparisonResult {
    @Schema(description = "Whether there are differences between the data sets", example = "true", required = true)
    private boolean hasDifferences;

    @Schema(description = "Map of field differences (field name -> difference description)", required = false)
    private Map<String, FieldComparison> fieldComparisons;

    @Schema(description = "List of changed fields", required = false)
    private List<String> changedFields;

    @Schema(description = "List of unchanged fields", required = false)
    private List<String> unchangedFields;

    @Schema(description = "Summary of changes", example = "3 fields changed, 15 fields unchanged", required = false)
    private String changesSummary;

    @Schema(description = "Total number of fields compared", example = "18", required = true)
    private int totalFieldsCompared;

    @Schema(description = "Number of fields changed", example = "3", required = true)
    private int fieldsChanged;

    @Schema(description = "Number of fields unchanged", example = "15", required = true)
    private int fieldsUnchanged;

    @Schema(description = "Validation results for the new data", required = false)
    private Map<String, String> validationResults;

    @Schema(description = "Comparison timestamp", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime comparisonTimestamp;
}