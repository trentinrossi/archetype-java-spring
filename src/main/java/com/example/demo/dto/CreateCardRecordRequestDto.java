package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRecordRequestDto {

    @Schema(description = "Card number serving as the record key identifier", example = "1234567890123456", required = true)
    @NotNull(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be 16 characters in length")
    private String cardNumber;

    @Schema(description = "Additional card-related information and attributes", example = "Card data containing account details and attributes", required = true)
    @NotNull(message = "Card data is required")
    @Size(max = 134, message = "Card data must not exceed 134 characters")
    private String cardData;
}
