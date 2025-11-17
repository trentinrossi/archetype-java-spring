package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Schema(description = "Unique account identifier", example = "12345678901", required = false)
    @Positive(message = "Account ID must be a positive number")
    private Long acctId;
}
