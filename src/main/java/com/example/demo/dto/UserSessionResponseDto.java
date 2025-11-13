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

    @Schema(description = "Unique identifier for the session")
    private Long id;

    @Schema(description = "Current transaction identifier", example = "CA00")
    private String transactionId;

    @Schema(description = "Current program name", example = "COADM01C")
    private String programName;

    @Schema(description = "Name of the program that transferred control", example = "COMEN01C")
    private String fromProgram;

    @Schema(description = "Transaction ID of the calling program", example = "CO00")
    private String fromTransaction;

    @Schema(description = "Context information for program execution", example = "123456789")
    private Long programContext;

    @Schema(description = "Indicates if program is being reentered after user input", example = "true")
    private Boolean reenterFlag;

    @Schema(description = "Name of the program to transfer to", example = "COUSR00C")
    private String toProgram;

    @Schema(description = "Indicates if the program is being reentered", example = "Y")
    private String programReenterFlag;

    @Schema(description = "User type indicator (A for admin, U for user)", example = "A")
    private String userType;

    @Schema(description = "Transaction ID of the calling program", example = "CA00")
    private String fromTransactionId;

    @Schema(description = "User identifier for the session", example = "ADMIN001")
    private String userId;

    @Schema(description = "Session identifier combining user ID, transaction ID, and program name")
    private String sessionIdentifier;

    @Schema(description = "Indicates if user is an admin user")
    private Boolean isAdminUser;

    @Schema(description = "Indicates if user is a regular user")
    private Boolean isRegularUser;

    @Schema(description = "Timestamp when the session was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the session was last updated")
    private LocalDateTime updatedAt;
}
