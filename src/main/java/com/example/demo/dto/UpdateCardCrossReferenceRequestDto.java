package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardCrossReferenceRequestDto {

    @Schema(description = "Contains customer ID and account ID relationships (maximum 34 characters)", example = "CUST123456ACCT789012", required = false)
    private String crossReferenceData;
}