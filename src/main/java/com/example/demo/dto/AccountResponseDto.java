package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AccountResponseDto
 * 
 * Response DTO for account information.
 * 
 * Business Rules Implemented:
 * - BR004: Account Filter Validation
 * - BR017: Input Error Protection
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    @Schema(description = "Unique account identifier", example = "12345678901")
    private String accountId;

    @Schema(description = "Number of credit cards associated with this account", example = "3")
    private Integer creditCardCount;

    @Schema(description = "Number of users with access to this account", example = "2")
    private Integer userAccessCount;

    @Schema(description = "Timestamp when the account was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
