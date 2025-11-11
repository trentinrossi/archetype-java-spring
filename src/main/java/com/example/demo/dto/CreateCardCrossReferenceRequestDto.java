package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequestDto {

    @Schema(description = "Account ID in cross-reference", example = "ACC00001234", required = true)
    @NotBlank(message = "Account ID cannot be empty")
    @Size(max = 11, message = "Account ID must not exceed 11 characters")
    private String accountId;

    @Schema(description = "Card number associated with account", example = "4111111111111111", required = true)
    @NotBlank(message = "Card number cannot be empty")
    @Size(max = 16, message = "Card number must not exceed 16 characters")
    private String cardNumber;
}
