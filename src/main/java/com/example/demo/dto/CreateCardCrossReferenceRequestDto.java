package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for creating a new card cross reference
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequestDto {
    
    @Schema(description = "Card number - must be exactly 16 characters", example = "4111111111111111", required = true)
    private String cardNumber;
    
    @Schema(description = "Customer ID", example = "123456789", required = true)
    private String customerId;
    
    @Schema(description = "Account ID", example = "12345678901", required = true)
    private String accountId;
    
    @Schema(description = "Card type", example = "VISA", required = false)
    private String cardType;
    
    @Schema(description = "Card holder name", example = "John M. Doe", required = false)
    private String cardHolderName;
    
    @Schema(description = "Expiration date", example = "2027-12-31", required = false)
    private LocalDate expirationDate;
    
    @Schema(description = "Issue date", example = "2023-01-15", required = false)
    private LocalDate issueDate;
    
    @Schema(description = "Is primary card", example = "true", required = false)
    private Boolean isPrimaryCard;
    
    @Schema(description = "Card sequence number", example = "1", required = false)
    private Integer cardSequenceNumber;
    
    @Schema(description = "Embossed name (max 26 characters)", example = "JOHN M DOE", required = false)
    private String embossedName;
}
