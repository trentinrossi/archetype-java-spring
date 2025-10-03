package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignOnResponse {
    @Schema(description = "Sign on success status", example = "true", required = true)
    private boolean success;

    @Schema(description = "Sign on response message", example = "Sign on successful", required = true)
    private String message;

    @Schema(description = "User type", example = "A", required = false)
    private String userType;

    @Schema(description = "Redirect program", example = "MAIN_MENU", required = false)
    private String redirectProgram;
}