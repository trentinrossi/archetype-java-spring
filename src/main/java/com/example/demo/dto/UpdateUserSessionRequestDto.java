package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserSessionRequestDto {

    @Schema(description = "Current transaction identifier (CA00)", example = "CA00", required = false)
    @Size(max = 4, message = "Transaction ID must not exceed 4 characters")
    private String transactionId;

    @Schema(description = "Current program name (COADM01C)", example = "COADM01C", required = false)
    @Size(max = 8, message = "Program name must not exceed 8 characters")
    private String programName;

    @Schema(description = "Name of the program that transferred control", example = "COMEN01C", required = false)
    @Size(max = 8, message = "From program must not exceed 8 characters")
    private String fromProgram;

    @Schema(description = "Transaction ID of the calling program", example = "CM00", required = false)
    @Size(max = 4, message = "From transaction must not exceed 4 characters")
    private String fromTransaction;

    @Schema(description = "Context information for program state", example = "123456789", required = false)
    private Long programContext;

    @Schema(description = "Indicates if program is being reentered after user input", example = "true", required = false)
    private Boolean reenterFlag;
}
