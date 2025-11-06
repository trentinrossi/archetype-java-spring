package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequestDto {
    
    @Schema(description = "Primary key - card number linking to customer and account", example = "1234567890123456", required = true)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String cardNumber;
    
    @Schema(description = "Contains customer ID and account ID relationships", example = "CUST123456789012ACCT123456789012", required = true)
    @NotBlank(message = "Cross reference data is required")
    @Size(max = 34, message = "Cross reference data must not exceed 34 characters")
    private String crossReferenceData;
    
    @Schema(description = "Customer ID associated with this card", example = "123456789", required = true)
    @NotBlank(message = "Customer ID is required")
    private Long customerId;
    
    @Schema(description = "Account ID associated with this card", example = "12345678901", required = true)
    @NotBlank(message = "Account ID is required")
    private Long accountId;
}
