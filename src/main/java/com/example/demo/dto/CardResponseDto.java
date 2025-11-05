package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {
    
    @Schema(description = "Card number (16 digits)", example = "1234567890123456")
    private String cardNumber;

    @Schema(description = "Status of the card", example = "ACTIVE")
    private String status;

    @Schema(description = "Additional card details", example = "Premium card with rewards")
    private String cardDetails;

    @Schema(description = "Timestamp when the card was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the card was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
