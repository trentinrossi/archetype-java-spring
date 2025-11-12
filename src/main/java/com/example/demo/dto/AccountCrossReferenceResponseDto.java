package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCrossReferenceResponseDto {

    @Schema(description = "Credit card number serving as primary identifier", example = "1234567890123456")
    private String cardNumber;

    @Schema(description = "Additional account cross-reference information", example = "Account cross-reference data")
    private String crossReferenceData;

    @Schema(description = "Timestamp when the record was created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the record was last updated", example = "2023-06-15T14:30:00")
    private LocalDateTime updatedAt;
}
