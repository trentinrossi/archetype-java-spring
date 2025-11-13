package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserSessionRequestDto {

    @Schema(description = "Current transaction identifier", example = "CA00", required = true)
    @NotBlank(message = "Transaction ID cannot be empty")
    @Size(max = 4, message = "Transaction ID must not exceed 4 characters")
    private String transactionId;

    @Schema(description = "Current program name", example = "COADM01C", required = true)
    @NotBlank(message = "Program name cannot be empty")
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

    @Schema(description = "Indicates if program is being reentered after user input", example = "true", required = true)
    @NotNull(message = "Reenter flag is required")
    private Boolean reenterFlag;

    @Schema(description = "Name of the program to transfer to", example = "COUSR00C", required = true)
    @NotBlank(message = "To program cannot be empty")
    @Size(max = 8, message = "To program must not exceed 8 characters")
    private String toProgram;

    @Schema(description = "Indicates if the program is being reentered", example = "Y", required = true)
    @NotBlank(message = "Program reenter flag cannot be empty")
    @Size(min = 1, max = 1, message = "Program reenter flag must be exactly 1 character")
    @Pattern(regexp = "[YN]", message = "Program reenter flag must be Y or N")
    private String programReenterFlag;

    @Schema(description = "User type indicator (A for admin, U for user)", example = "A", required = true)
    @NotBlank(message = "User Type cannot be empty or contain only low-values")
    @Size(min = 1, max = 1, message = "User type must be exactly 1 character")
    private String userType;

    @Schema(description = "Transaction ID of the calling program", example = "CA00", required = true)
    @NotBlank(message = "From transaction ID cannot be empty")
    @Size(max = 4, message = "From transaction ID must not exceed 4 characters")
    private String fromTransactionId;

    @Schema(description = "User identifier for the session", example = "ADMIN001", required = true)
    @NotBlank(message = "User ID cannot be empty or spaces")
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    private String userId;
}
