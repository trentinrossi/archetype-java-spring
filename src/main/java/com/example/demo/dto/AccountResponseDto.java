package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    @Schema(description = "Unique account identifier", example = "ACC00001234")
    private String accountId;

    @Schema(description = "Current account balance with 2 decimal places", example = "1500.00")
    private BigDecimal currentBalance;

    @Schema(description = "Indicates if account has a positive balance", example = "true")
    private Boolean hasPositiveBalance;

    @Schema(description = "Timestamp when the account was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the account was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
