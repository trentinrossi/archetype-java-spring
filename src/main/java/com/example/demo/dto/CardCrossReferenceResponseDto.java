package com.example.demo.dto;

import com.example.demo.enums.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for card cross reference data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReferenceResponseDto {
    
    @Schema(description = "Card number (masked)", example = "************1111")
    private String cardNumber;
    
    @Schema(description = "Customer ID", example = "123456789")
    private String customerId;
    
    @Schema(description = "Account ID", example = "12345678901")
    private String accountId;
    
    @Schema(description = "Card type", example = "VISA")
    private String cardType;
    
    @Schema(description = "Card holder name", example = "John M. Doe")
    private String cardHolderName;
    
    @Schema(description = "Expiration date", example = "2027-12-31")
    private LocalDate expirationDate;
    
    @Schema(description = "Issue date", example = "2023-01-15")
    private LocalDate issueDate;
    
    @Schema(description = "Card status", example = "ACTIVE")
    private CardStatus cardStatus;
    
    @Schema(description = "Card status display name", example = "Active")
    private String cardStatusDisplayName;
    
    @Schema(description = "Is primary card", example = "true")
    private Boolean isPrimaryCard;
    
    @Schema(description = "Card sequence number", example = "1")
    private Integer cardSequenceNumber;
    
    @Schema(description = "Embossed name", example = "JOHN M DOE")
    private String embossedName;
    
    @Schema(description = "PIN set", example = "true")
    private Boolean pinSet;
    
    @Schema(description = "PIN set date", example = "2023-01-20")
    private LocalDate pinSetDate;
    
    @Schema(description = "Activation date", example = "2023-01-20")
    private LocalDate activationDate;
    
    @Schema(description = "Last used date", example = "2024-01-20")
    private LocalDate lastUsedDate;
    
    @Schema(description = "Replacement card number (masked)", example = "************2222")
    private String replacementCardNumber;
    
    @Schema(description = "Replaced card number (masked)", example = "************0000")
    private String replacedCardNumber;
    
    @Schema(description = "Card production date", example = "2023-01-10")
    private LocalDate cardProductionDate;
    
    @Schema(description = "Card mailed date", example = "2023-01-12")
    private LocalDate cardMailedDate;
    
    @Schema(description = "Is expired", example = "false")
    private Boolean isExpired;
    
    @Schema(description = "Is active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Needs replacement", example = "false")
    private Boolean needsReplacement;
    
    @Schema(description = "Can transact", example = "true")
    private Boolean canTransact;
    
    @Schema(description = "Created timestamp", example = "2023-01-15T10:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last updated timestamp", example = "2024-01-20T15:30:00")
    private LocalDateTime updatedAt;
}
