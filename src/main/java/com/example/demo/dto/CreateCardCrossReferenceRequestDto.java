package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequestDto {

    @Schema(description = "Account Identification Number", example = "12345678901", required = true)
    private Long accountId;

    @Schema(description = "Customer Identification Number", example = "123456789", required = true)
    private Long customerId;
}
