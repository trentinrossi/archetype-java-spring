package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardRequestDto {

    @Schema(description = "Associated account identifier", example = "12345678901", required = false)
    private Long accountId;

    @Schema(description = "Associated customer identifier", example = "123456789", required = false)
    private Long customerId;
}
