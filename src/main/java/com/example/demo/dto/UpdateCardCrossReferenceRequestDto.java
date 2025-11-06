package com.example.demo.dto;

import com.example.demo.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for updating an existing card cross reference
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardCrossReferenceRequestDto {
    
    @Schema(description = "Card status", example = "ACTIVE", required = false)
    private CardStatus cardStatus;
    
    @Schema(description = "Card holder name", example = "John M. Doe", required = false)
    private String cardHolderName;
    
    @Schema(description = "Expiration date", example = "2027-12-31", required = false)
    private LocalDate expirationDate;
    
    @Schema(description = "Is primary card", example = "true", required = false)
    private Boolean isPrimaryCard;
    
    @Schema(description = "Embossed name (max 26 characters)", example = "JOHN M DOE", required = false)
    private String embossedName;
    
    @Schema(description = "PIN set", example = "true", required = false)
    private Boolean pinSet;
    
    @Schema(description = "PIN set date", example = "2023-01-20", required = false)
    private LocalDate pinSetDate;
    
    @Schema(description = "Activation date", example = "2023-01-20", required = false)
    private LocalDate activationDate;
    
    @Schema(description = "Last used date", example = "2024-01-20", required = false)
    private LocalDate lastUsedDate;
    
    @Schema(description = "Replacement card number", example = "4111111111112222", required = false)
    private String replacementCardNumber;
}
