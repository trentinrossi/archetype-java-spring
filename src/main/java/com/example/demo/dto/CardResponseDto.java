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

    @Schema(description = "Unique card number", example = "4532123456789012")
    private String cardNum;

    @Schema(description = "Associated account identifier", example = "12345678901")
    private Long acctId;

    @Schema(description = "Number of transactions for this card")
    private Integer transactionCount;

    @Schema(description = "Timestamp when record was created", example = "2023-12-31T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when record was last updated", example = "2023-12-31T14:30:00")
    private LocalDateTime updatedAt;
}
