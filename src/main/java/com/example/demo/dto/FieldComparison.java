package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldComparison {
    @Schema(description = "Field name", example = "custFirstName", required = true)
    private String fieldName;

    @Schema(description = "Old value", example = "John", required = false)
    private String oldValue;

    @Schema(description = "New value", example = "Jonathan", required = false)
    private String newValue;

    @Schema(description = "Whether the field has changed", example = "true", required = true)
    private boolean hasChanged;

    @Schema(description = "Field validation status", example = "VALID", required = false)
    private String validationStatus;

    @Schema(description = "Field validation message", example = "Field is valid", required = false)
    private String validationMessage;

    @Schema(description = "Field data type", example = "String", required = false)
    private String dataType;
}