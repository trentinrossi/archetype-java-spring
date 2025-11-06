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
    
    @Schema(description = "Internal database ID", example = "1")
    private Long id;
    
    @Schema(description = "Primary key - unique account identification number", example = "12345678901", required = true)
    private Long accountId;
    
    @Schema(description = "Account balance, credit limit, dates, and status information with proper decimal handling", example = "Account data containing balance and credit information", required = true)
    private String accountData;
    
    @Schema(description = "Timestamp when the account was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}