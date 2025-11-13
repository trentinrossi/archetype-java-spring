package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserSessionRequestDto {

    @Schema(description = "Current transaction identifier", example = "CA00", required = false)
    @Size(max = 4, message = "Transaction ID must not exceed 4 characters")
    private String transactionId;

    @Schema(description = "Current program name", example = "COADM01C", required = false)
    @Size(max = 8, message = "Program name must not exceed 8 characters")
    private String programName;

    @Schema(description = "Name of the program that transferred control", example = "COMEN01C", required = false)
    @Size(max = 8, message = "From program must not exceed 8 characters")
    private String fromProgram;

    @Schema(description = "Transaction ID of the calling program", example = "CO00", required = false)
    @Size(max = 4, message = "From transaction must not exceed 4 characters")
    private String fromTransaction;

    @Schema(description = "Context information for program execution", example = "123456789", required = false)
    private Long programContext;

    @Schema(description = "Indicates if program is being reentered after user input", example = "true", required = false)
    private Boolean reenterFlag;

    @Schema(description = "Name of the program to transfer to", example = "COUSR00C", required = false)
    @Size(max = 8, message = "To program must not exceed 8 characters")
    private String toProgram;

    @Schema(description = "Indicates if the program is being reentered", example = "Y", required = false)
    @Size(min = 1, max = 1, message = "Program reenter flag must be exactly 1 character")
    @Pattern(regexp = "[YN]", message = "Program reenter flag must be Y or N")
    private String programReenterFlag;

    @Schema(description = "User type indicator", example = "A", required = false)
    @Size(min = 1, max = 1, message = "User type must be exactly 1 character")
    private String userType;

    @Schema(description = "Transaction ID of the calling program", example = "CA00", required = false)
    @Size(max = 4, message = "From transaction ID must not exceed 4 characters")
    private String fromTransactionId;

    @Schema(description = "User identifier for the session", example = "ADMIN001", required = false)
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    private String userId;
}
