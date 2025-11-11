package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillPaymentRequestDto {

    @Schema(description = "Account ID for bill payment", example = "ACC00001234", required = true)
    @NotBlank(message = "Acct ID can NOT be empty...")
    @Size(min = 11, max = 11, message = "Account ID must be exactly 11 characters")
    private String accountId;

    @Schema(description = "Card number for bill payment", example = "1234567890123456", required = true)
    @NotBlank(message = "Card number is required")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String cardNumber;

    @Schema(description = "User confirmation for payment (Y/N)", example = "Y", required = true)
    @NotBlank(message = "Confirmation is required")
    @Size(min = 1, max = 1, message = "Confirmation must be Y or N")
    private String confirmation;
}
