package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantResponseDto {

    @Schema(description = "Unique merchant identifier", example = "123456789")
    private String merchantId;

    @Schema(description = "Merchant business name", example = "ABC Grocery Store")
    private String merchantName;

    @Schema(description = "Merchant city location", example = "New York")
    private String merchantCity;

    @Schema(description = "Merchant ZIP code", example = "10001")
    private String merchantZip;

    @Schema(description = "Number of transactions for this merchant")
    private Integer transactionCount;

    @Schema(description = "Timestamp when record was created", example = "2023-12-31T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when record was last updated", example = "2023-12-31T14:30:00")
    private LocalDateTime updatedAt;
}
