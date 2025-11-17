package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantRequestDto {

    @Schema(description = "Unique merchant identifier", example = "123456789", required = true)
    @NotBlank(message = "Merchant ID must be entered")
    @Pattern(regexp = "\\d+", message = "Merchant ID must be numeric")
    @Size(max = 9, message = "Merchant ID must not exceed 9 digits")
    private String merchantId;

    @Schema(description = "Merchant business name", example = "ABC Grocery Store", required = true)
    @NotBlank(message = "Merchant Name must be entered")
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String merchantName;

    @Schema(description = "Merchant city location", example = "New York", required = true)
    @NotBlank(message = "Merchant City must be entered")
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String merchantCity;

    @Schema(description = "Merchant ZIP code", example = "10001", required = true)
    @NotBlank(message = "Merchant Zip must be entered")
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String merchantZip;
}
