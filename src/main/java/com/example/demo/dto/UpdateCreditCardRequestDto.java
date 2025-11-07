package com.example.demo.dto;

import com.example.demo.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing credit card
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCreditCardRequestDto {
    
    @Schema(description = "Current status of the credit card", 
            example = "BLOCKED")
    private CardStatus cardStatus;
}
