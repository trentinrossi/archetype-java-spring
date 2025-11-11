package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequestDto {

    @Schema(description = "Transaction description", example = "BILL PAYMENT - UPDATED", required = false)
    @Size(max = 50, message = "Transaction description cannot exceed 50 characters")
    private String tranDesc;

    @Schema(description = "Merchant name", example = "BILL PAYMENT UPDATED", required = false)
    @Size(max = 50, message = "Merchant name cannot exceed 50 characters")
    private String tranMerchantName;

    @Schema(description = "Merchant city", example = "NEW YORK", required = false)
    @Size(max = 50, message = "Merchant city cannot exceed 50 characters")
    private String tranMerchantCity;

    @Schema(description = "Merchant ZIP code", example = "10001", required = false)
    @Size(max = 10, message = "Merchant ZIP code cannot exceed 10 characters")
    private String tranMerchantZip;
}
