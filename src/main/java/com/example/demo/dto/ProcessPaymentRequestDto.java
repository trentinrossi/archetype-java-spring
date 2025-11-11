package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequestDto {

    @Schema(description = "Unique account identifier", example = "ACC00001234", required = true)
    @NotBlank(message = "Acct ID can NOT be empty...")
    @Size(max = 11, message = "Account ID must not exceed 11 characters")
    private String accountId;

    @Schema(description = "Credit card number associated with the account", example = "4111111111111111", required = true)
    @NotBlank(message = "Card number is required")
    @Size(max = 16, message = "Card number must not exceed 16 characters")
    private String cardNumber;

    @Schema(description = "Payment confirmation flag (Y/y to confirm, N/n to cancel, empty to display info only)", example = "Y", required = false)
    @Pattern(regexp = "^[YyNn]?$", message = "Invalid value. Valid values are (Y/N)...")
    private String confirmPayment;
}
