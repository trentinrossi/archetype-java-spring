package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentResponseDto {

    @Schema(description = "Unique transaction identifier", example = "123456789")
    private Long transactionId;

    @Schema(description = "Account identifier for which payment was processed", example = "ACC00001234")
    private String accountId;

    @Schema(description = "Account balance before payment", example = "1500.00")
    private BigDecimal previousBalance;

    @Schema(description = "Account balance after payment", example = "0.00")
    private BigDecimal newBalance;

    @Schema(description = "Amount paid (full balance)", example = "1500.00")
    private BigDecimal paymentAmount;

    @Schema(description = "Timestamp when payment was processed", example = "2024-01-15T14:45:00")
    private LocalDateTime timestamp;

    @Schema(description = "Success or error message", example = "Payment processed successfully")
    private String message;

    @Schema(description = "Transaction type code", example = "02")
    private String transactionTypeCode;

    @Schema(description = "Transaction category code", example = "2")
    private Integer transactionCategoryCode;

    @Schema(description = "Transaction source", example = "POS TERM")
    private String transactionSource;

    @Schema(description = "Transaction description", example = "BILL PAYMENT - ONLINE")
    private String transactionDescription;

    @Schema(description = "Merchant ID", example = "999999999")
    private Long merchantId;

    @Schema(description = "Merchant name", example = "BILL PAYMENT")
    private String merchantName;

    @Schema(description = "Merchant city", example = "N/A")
    private String merchantCity;

    @Schema(description = "Merchant ZIP code", example = "N/A")
    private String merchantZip;
}
