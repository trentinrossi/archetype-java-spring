package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReferenceResponseDto {

    @Schema(description = "Account ID in cross-reference", example = "ACC00001234")
    private String accountId;

    @Schema(description = "Card number associated with account", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Timestamp when the cross-reference was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the cross-reference was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
