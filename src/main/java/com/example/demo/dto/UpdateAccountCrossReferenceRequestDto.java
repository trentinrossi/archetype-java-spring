package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountCrossReferenceRequestDto {

    @Schema(description = "Additional account cross-reference information", example = "Account cross-reference data", required = false)
    @Size(max = 34, message = "Cross reference data must not exceed 34 characters")
    private String crossReferenceData;
}
