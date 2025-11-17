package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMerchantRequestDto {

    @Schema(description = "Merchant business name", example = "ABC Grocery Store", required = false)
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String merchantName;

    @Schema(description = "Merchant city location", example = "New York", required = false)
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String merchantCity;

    @Schema(description = "Merchant ZIP code", example = "10001", required = false)
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String merchantZip;
}
