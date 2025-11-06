package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerRequestDto {
    
    @Schema(description = "Complete customer profile including demographics, addresses, contact information, SSN, and credit scores", example = "Updated customer profile data...", required = false)
    @Size(min = 1, max = 491, message = "Customer data must not exceed 491 characters")
    private String customerData;
}
