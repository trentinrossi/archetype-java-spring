package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDto {
    
    @Schema(description = "Primary key - unique customer identification number in numeric format (9 characters)", example = "123456789", required = true)
    private String customerId;
    
    @Schema(description = "Complete customer profile including demographics, addresses, contact information, SSN, and credit scores (491 characters)", example = "Customer profile data...", required = true)
    private String customerData;
}