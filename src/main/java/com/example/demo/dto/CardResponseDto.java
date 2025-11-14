package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

    @Schema(description = "Card ID", example = "1")
    private Long id;

    @Schema(description = "Card number associated with account", example = "4111111111111111")
    private String xrefCardNum;

    @Schema(description = "Account ID for cross-reference", example = "ACC123456789")
    private String xrefAcctId;

    @Schema(description = "Card information validity status", example = "true")
    private Boolean isValid;

    @Schema(description = "Card creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Card last update timestamp", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
