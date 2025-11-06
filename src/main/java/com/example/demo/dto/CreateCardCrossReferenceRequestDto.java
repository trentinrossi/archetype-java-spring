package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequestDto {
    
    @Schema(description = "Primary key - card number linking to customer and account (must be exactly 16 characters)", example = "1234567890123456", required = true)
    private String cardNumber;
    
    @Schema(description = "Contains customer ID and account ID relationships (34 characters)", example = "CUST123456789012ACCT123456789012", required = true)
    private String crossReferenceData;
}