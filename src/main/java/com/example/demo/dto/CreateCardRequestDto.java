package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequestDto {

    @Schema(description = "Unique card number", example = "4532123456789012", required = true)
    @NotBlank(message = "Card Number must be entered")
    @Size(min = 16, max = 16, message = "Card Number must be 16 digits")
    @Pattern(regexp = "\\d{16}", message = "Card Number must be numeric")
    private String cardNum;

    @Schema(description = "Associated account identifier", example = "12345678901", required = true)
    @NotNull(message = "Account ID must be entered")
    @Positive(message = "Account ID must be a positive number")
    private Long acctId;
}
