package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {
    
    @Schema(description = "Account balance, credit limit, dates, and status information with proper decimal handling", example = "Sample account data with 289 characters", required = false)
    private String accountData;
}