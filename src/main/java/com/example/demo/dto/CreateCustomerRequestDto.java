package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequestDto {
    
    @Schema(description = "Primary key - unique customer identification number in numeric format", example = "123456789", required = true)
    @NotBlank(message = "Customer ID is required")
    @Size(min = 9, max = 9, message = "Customer ID must be exactly 9 characters")
    @Pattern(regexp = "^[0-9]{9}$", message = "Customer ID must be 9 characters in numeric format")
    private String customerId;
    
    @Schema(description = "Complete customer profile including demographics, addresses, contact information, SSN, and credit scores", example = "Customer profile data...", required = true)
    @NotBlank(message = "Customer data is required")
    @Size(min = 1, max = 491, message = "Customer data must not exceed 491 characters")
    private String customerData;
}
