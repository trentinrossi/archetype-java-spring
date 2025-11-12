package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardRecordRequestDto {

    @Schema(description = "Additional card-related information and attributes", example = "Card data containing account details and attributes", required = false)
    @Size(max = 134, message = "Card data must not exceed 134 characters")
    private String cardData;
}
