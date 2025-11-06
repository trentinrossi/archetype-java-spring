package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    
    @Schema(description = "Internal database ID", example = "1")
    private Long id;
    
    @Schema(description = "Primary key - unique customer identification number in numeric format", example = "123456789", required = true)
    private String customerId;
    
    @Schema(description = "Complete customer profile including demographics, addresses, contact information, SSN, and credit scores", required = true)
    private String customerData;
    
    @Schema(description = "Timestamp when the customer was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the customer was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}