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

    @Schema(description = "Session database ID", example = "1")
    private Long id;

    @Schema(description = "Current transaction identifier", example = "CA00")
    private String transactionId;

    @Schema(description = "Current program name", example = "COADM01C")
    private String programName;

    @Schema(description = "Name of the program that transferred control", example = "COSGN00C")
    private String fromProgram;

    @Schema(description = "Transaction ID of the calling program", example = "SG00")
    private String fromTransaction;

    @Schema(description = "Context information for program execution", example = "123456789")
    private Long programContext;

    @Schema(description = "Indicates if program is being reentered after user input", example = "true")
    private Boolean reenterFlag;

    @Schema(description = "Name of the program to transfer to", example = "COUSR00C")
    private String toProgram;

    @Schema(description = "Indicates if the program is being reentered", example = "Y")
    private String programReenterFlag;

    @Schema(description = "User type indicator (A=Admin, R=Regular)", example = "A")
    private String userType;

    @Schema(description = "Transaction ID of the calling program", example = "SG00")
    private String fromTransactionId;

    @Schema(description = "User identifier for the session", example = "USR00001")
    private String userId;

    @Schema(description = "Session context description", example = "User: USR00001, Transaction: CA00, Program: COADM01C")
    private String sessionContext;

    @Schema(description = "Indicates if user is admin", example = "true")
    private Boolean isAdminUser;

    @Schema(description = "Indicates if session is reentering", example = "true")
    private Boolean isReentering;

    @Schema(description = "Indicates if program is reentering", example = "true")
    private Boolean isProgramReentering;

    @Schema(description = "Indicates if session has calling program", example = "true")
    private Boolean hasCallingProgram;

    @Schema(description = "Indicates if session has context", example = "true")
    private Boolean hasContext;

    @Schema(description = "Timestamp when session was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when session was last updated")
    private LocalDateTime updatedAt;
}
