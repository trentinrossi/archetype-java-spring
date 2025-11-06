package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardCrossReferenceRequestDto {
    
    @Schema(description = "Contains customer ID and account ID relationships", example = "CUST123456789012ACCT123456789012", required = false)
    @Size(max = 34, message = "Cross reference data must not exceed 34 characters")
    private String crossReferenceData;
}
