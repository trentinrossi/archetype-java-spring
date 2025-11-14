package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardCrossReferenceRequestDto {

    @Schema(description = "Customer Identification Number", example = "123456789", required = false)
    private Long customerId;
}
