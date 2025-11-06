package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {
    
    @Schema(description = "Account balance, credit limit, dates, and status information with proper decimal handling", example = "Updated account data containing balance and credit information", required = false)
    @Size(max = 289, message = "Account data must not exceed 289 characters")
    private String accountData;
}
