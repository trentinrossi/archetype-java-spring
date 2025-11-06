package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    
    @Schema(description = "Unique account identification number (11 numeric digits)", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Account balance, credit limit, dates, and status information with proper decimal handling", example = "Account data containing balance and credit information")
    private String accountData;
    
    @Schema(description = "Customer ID associated with this account", example = "1")
    private Long customerId;
    
    @Schema(description = "Timestamp when the account was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
