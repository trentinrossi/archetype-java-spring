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
    
    @Schema(description = "Internal database ID", example = "1")
    private Long id;
    
    @Schema(description = "Primary key - card number linking to customer and account", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @Schema(description = "Contains customer ID and account ID relationships", example = "CUST123456789012345678901234567890", required = true)
    private String crossReferenceData;
    
    @Schema(description = "Timestamp when the card cross reference was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the card cross reference was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}