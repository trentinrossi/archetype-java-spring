package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequestDto {

    @Schema(description = "Card number associated with account", example = "4111111111111111", required = true)
    private String xrefCardNum;

    @Schema(description = "Account ID for cross-reference", example = "ACC123456789", required = true)
    private String xrefAcctId;
}
