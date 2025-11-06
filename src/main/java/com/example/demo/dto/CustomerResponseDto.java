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
    
    @Schema(description = "Unique customer identification number in numeric format", example = "123456789")
    private String customerId;
    
    @Schema(description = "Complete customer profile including demographics, addresses, contact information, SSN, and credit scores", example = "Customer profile data...")
    private String customerData;
    
    @Schema(description = "Timestamp when the customer record was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the customer record was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
