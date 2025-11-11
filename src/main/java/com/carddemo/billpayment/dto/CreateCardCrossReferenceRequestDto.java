package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardCrossReferenceRequestDto {

    @Schema(description = "Account ID in cross-reference", example = "ACC00001234", required = true)
    @NotBlank(message = "Account ID cannot be empty")
    @Size(min = 11, max = 11, message = "Account ID must be exactly 11 characters")
    private String xrefAcctId;

    @Schema(description = "Card number associated with account", example = "1234567890123456", required = true)
    @NotBlank(message = "Card number cannot be empty")
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String xrefCardNum;
}
