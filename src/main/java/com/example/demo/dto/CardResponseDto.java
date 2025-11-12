package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

    @Schema(description = "Credit card number", example = "4532015112830366")
    private String cardNumber;

    @Schema(description = "Masked credit card number for security", example = "************0366")
    private String maskedCardNumber;

    @Schema(description = "Formatted credit card number", example = "4532 0151 1283 0366")
    private String formattedCardNumber;

    @Schema(description = "Associated account identifier", example = "12345678901")
    private String accountId;

    @Schema(description = "Customer ID linked to the card", example = "123456789")
    private String customerId;

    @Schema(description = "Security code for the card", example = "***")
    private String cvvCode;

    @Schema(description = "Date when the card expires", example = "2025-12-31")
    private String expirationDate;

    @Schema(description = "Credit card number for cross-reference", example = "4532015112830366")
    private String xrefCardNum;

    @Schema(description = "Indicates if card is expired based on expiration date", example = "false")
    private Boolean isExpired;

    @Schema(description = "Card status display name", example = "Active")
    private String cardStatus;

    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp")
    private LocalDateTime updatedAt;
}
