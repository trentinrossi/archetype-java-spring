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
    
    @Schema(description = "Primary key - card number linking to customer and account", example = "1234567890123456")
    private String cardNumber;
    
    @Schema(description = "Contains customer ID and account ID relationships", example = "CUST123456789012ACCT123456789012")
    private String crossReferenceData;
    
    @Schema(description = "Customer ID associated with this card", example = "123456789")
    private String customerId;
    
    @Schema(description = "Account ID associated with this card", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Timestamp when the record was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the record was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
}
