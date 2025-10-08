package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @Size(max = 20, message = "First name must not exceed 20 characters")
    @Schema(description = "First name of the user", example = "John", required = false)
    private String firstName;

    @Size(max = 20, message = "Last name must not exceed 20 characters")
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String lastName;

    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "Password for the user", example = "pass123", required = false)
    private String password;

    @Pattern(regexp = "^[AR]$", message = "User type must be A (Admin) or R (Regular)")
    @Schema(description = "Type of the user (A=Admin, R=Regular)", example = "A", required = false)
    private String userType;
}