package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardCrossReferenceRequestDto {

    @Schema(description = "Card number associated with account", example = "1234567890123456", required = false)
    @Size(min = 16, max = 16, message = "Card number must be exactly 16 characters")
    private String xrefCardNum;
}
