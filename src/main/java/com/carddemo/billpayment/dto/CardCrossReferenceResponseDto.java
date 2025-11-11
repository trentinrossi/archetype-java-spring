package com.carddemo.billpayment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReferenceResponseDto {

    @Schema(description = "Unique identifier for the cross-reference", example = "1")
    private Long id;

    @Schema(description = "Account ID in cross-reference", example = "ACC00001234")
    private String xrefAcctId;

    @Schema(description = "Card number associated with account", example = "1234567890123456")
    private String xrefCardNum;

    @Schema(description = "Masked card number for display", example = "************3456")
    private String maskedCardNum;

    @Schema(description = "Cross-reference creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Cross-reference last update timestamp")
    private LocalDateTime updatedAt;

    public CardCrossReferenceResponseDto(Long id, String xrefAcctId, String xrefCardNum) {
        this.id = id;
        this.xrefAcctId = xrefAcctId;
        this.xrefCardNum = xrefCardNum;
        this.maskedCardNum = maskCardNumber(xrefCardNum);
    }

    private String maskCardNumber(String cardNum) {
        if (cardNum == null || cardNum.length() != 16) {
            return "****************";
        }
        return "************" + cardNum.substring(12);
    }
}
