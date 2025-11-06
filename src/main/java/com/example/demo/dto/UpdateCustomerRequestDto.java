package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequestDto {
    
    @Schema(description = "Complete customer profile including demographics, addresses, contact information, SSN, and credit scores", example = "Customer profile data with 491 characters", required = false)
    private String customerData;
}