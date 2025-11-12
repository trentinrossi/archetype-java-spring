package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRecordResponseDto {

    @Schema(description = "Card number serving as the record key identifier", example = "1234567890123456")
    private String cardNumber;

    @Schema(description = "Additional card-related information and attributes", example = "Card data containing account details and attributes")
    private String cardData;

    @Schema(description = "Timestamp when the card record was created", example = "2023-01-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the card record was last updated", example = "2023-06-15T14:30:00")
    private LocalDateTime updatedAt;
}
