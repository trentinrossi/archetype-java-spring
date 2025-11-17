package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    @Schema(description = "Unique account identifier", example = "12345678901")
    private Long acctId;

    @Schema(description = "List of cards associated with this account")
    private List<String> cardNumbers;

    @Schema(description = "Number of transactions for this account")
    private Integer transactionCount;

    @Schema(description = "Timestamp when record was created", example = "2023-12-31T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when record was last updated", example = "2023-12-31T14:30:00")
    private LocalDateTime updatedAt;
}
