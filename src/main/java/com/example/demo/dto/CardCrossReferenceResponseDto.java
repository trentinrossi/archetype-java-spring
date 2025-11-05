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
    
    @Schema(description = "Card number (16 digits)", example = "1234567890123456")
    private String cardNumber;
    
    @Schema(description = "Account ID", example = "12345678901")
    private Long accountId;
    
    @Schema(description = "Customer ID", example = "123456789")
    private Long customerId;
    
    @Schema(description = "Timestamp when the record was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the record was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
