package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardResponseDto {

    @Schema(description = "16-digit credit card number", example = "1234567890123456")
    private String cardNumber;

    @Schema(description = "Formatted card number with spaces", example = "1234 5678 9012 3456")
    private String formattedCardNumber;

    @Schema(description = "Masked card number for security", example = "**** **** **** 3456")
    private String maskedCardNumber;

    @Schema(description = "11-digit account number associated with the card", example = "12345678901")
    private Long accountId;

    @Schema(description = "Name embossed on the credit card", example = "JOHN DOE")
    private String embossedName;

    @Schema(description = "Card expiration date", example = "2025-12-31")
    private LocalDate expirationDate;

    @Schema(description = "Formatted expiration date", example = "12/2025")
    private String expirationDateFormatted;

    @Schema(description = "Expiration month (2 digits)", example = "12")
    private String expiryMonth;

    @Schema(description = "Expiration year (4 digits)", example = "2025")
    private String expiryYear;

    @Schema(description = "Expiration month as integer", example = "12")
    private Integer expirationMonth;

    @Schema(description = "Expiration year as integer", example = "2025")
    private Integer expirationYear;

    @Schema(description = "Expiration day", example = "31")
    private Integer expirationDay;

    @Schema(description = "Active status of the card", example = "Y")
    private String activeStatus;

    @Schema(description = "Card status - Y for active, N for inactive", example = "Y")
    private String cardStatus;

    @Schema(description = "Whether the card is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Whether the card is expired", example = "false")
    private Boolean isExpired;

    @Schema(description = "CVV code (masked)", example = "***")
    private String cvvCode;

    @Schema(description = "Version number for optimistic locking", example = "1")
    private Long version;

    @Schema(description = "User who last modified the card", example = "USER001")
    private String lastModifiedBy;

    @Schema(description = "Timestamp when the card was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the card was last updated", example = "2024-01-20T14:45:00")
    private LocalDateTime updatedAt;
}
