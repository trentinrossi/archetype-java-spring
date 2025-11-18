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

    @Schema(description = "11-digit unique account identifier", example = "12345678901")
    private Long accountId;
    
    @Schema(description = "Formatted account ID with dashes", example = "123-4567-8901")
    private String formattedAccountId;
    
    @Schema(description = "Number of credit cards associated with this account", example = "3")
    private Integer creditCardCount;
    
    @Schema(description = "Timestamp when the account was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
