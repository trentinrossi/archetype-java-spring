package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequestDto {

    @Schema(description = "Credit card number", example = "1234567890123456", required = true)
    private String cardNumber;

    @Schema(description = "Associated account identifier", example = "12345678901", required = true)
    private Long accountId;

    @Schema(description = "Associated customer identifier", example = "123456789", required = true)
    private Long customerId;
}
