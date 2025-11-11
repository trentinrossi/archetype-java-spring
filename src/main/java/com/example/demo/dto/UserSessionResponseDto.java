package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionResponseDto {

    @Schema(description = "Unique identifier for the user session", example = "1")
    private Long id;

    @Schema(description = "Current transaction identifier (CA00)", example = "CA00")
    private String transactionId;

    @Schema(description = "Current program name (COADM01C)", example = "COADM01C")
    private String programName;

    @Schema(description = "Name of the program that transferred control", example = "COMEN01C")
    private String fromProgram;

    @Schema(description = "Transaction ID of the calling program", example = "CM00")
    private String fromTransaction;

    @Schema(description = "Context information for program state", example = "123456789")
    private Long programContext;

    @Schema(description = "Indicates if program is being reentered after user input", example = "true")
    private Boolean reenterFlag;

    @Schema(description = "Timestamp when the user session was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the user session was last updated", example = "2024-01-15T14:45:00")
    private LocalDateTime updatedAt;
}
