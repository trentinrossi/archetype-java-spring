package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {

    @Schema(description = "Transaction type code", example = "01", required = false)
    private Integer tranTypeCd;

    @Schema(description = "Transaction category code", example = "1001", required = false)
    private Integer tranCatCd;

    @Schema(description = "Source of the transaction", example = "ONLINE", required = false)
    @Size(max = 10, message = "Transaction source must not exceed 10 characters")
    private String tranSource;

    @Schema(description = "Description of the transaction", example = "Purchase at ABC Store", required = false)
    @Size(max = 60, message = "Transaction description must not exceed 60 characters")
    private String tranDesc;

    @Schema(description = "Transaction amount with two decimal places", example = "1234.56", required = false)
    private BigDecimal tranAmt;

    @Schema(description = "Card number associated with the transaction", example = "4532123456789012", required = false)
    @Size(max = 16, message = "Card number must not exceed 16 characters")
    private String tranCardNum;

    @Schema(description = "Merchant identifier", example = "123456789", required = false)
    @Size(max = 9, message = "Merchant ID must not exceed 9 characters")
    private String tranMerchantId;

    @Schema(description = "Name of the merchant", example = "ABC Grocery Store", required = false)
    @Size(max = 30, message = "Merchant name must not exceed 30 characters")
    private String tranMerchantName;

    @Schema(description = "City of the merchant", example = "New York", required = false)
    @Size(max = 25, message = "Merchant city must not exceed 25 characters")
    private String tranMerchantCity;

    @Schema(description = "ZIP code of the merchant", example = "10001", required = false)
    @Size(max = 10, message = "Merchant ZIP must not exceed 10 characters")
    private String tranMerchantZip;

    @Schema(description = "Original transaction date in YYYY-MM-DD format", example = "2023-12-31", required = false)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format YYYY-MM-DD")
    private String tranOrigTs;

    @Schema(description = "Processing transaction date in YYYY-MM-DD format", example = "2023-12-31", required = false)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in format YYYY-MM-DD")
    private String tranProcTs;
}
