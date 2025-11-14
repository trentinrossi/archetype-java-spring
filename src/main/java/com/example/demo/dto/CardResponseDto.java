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

    @Schema(description = "Credit card number", example = "1234567890123456")
    private String cardNumber;

    @Schema(description = "Associated account identifier", example = "12345678901")
    private Long accountId;

    @Schema(description = "Associated customer identifier", example = "123456789")
    private Long customerId;

    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp")
    private LocalDateTime updatedAt;
}
