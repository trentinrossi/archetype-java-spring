package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for account response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    
    @Schema(description = "Unique account identifier", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Number of credit cards associated with this account", example = "3")
    private Integer creditCardCount;
    
    @Schema(description = "Number of active credit cards", example = "2")
    private Long activeCreditCardCount;
    
    @Schema(description = "Number of users with access to this account", example = "1")
    private Integer userCount;
    
    @Schema(description = "Timestamp when the account was created")
    private LocalDateTime createdAt;
    
    @Schema(description = "Timestamp when the account was last updated")
    private LocalDateTime updatedAt;
}
