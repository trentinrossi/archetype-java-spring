package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequestDto {

    @Schema(description = "11-digit unique account identifier. Must be exactly 11 numeric digits, cannot be zero or all zeros.", example = "12345678901", required = false)
    @Pattern(regexp = "^[0-9]{11}$", message = "ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER")
    @Pattern(regexp = "^(?!0+$)[0-9]{11}$", message = "Please enter a valid account number")
    private String accountId;
}
