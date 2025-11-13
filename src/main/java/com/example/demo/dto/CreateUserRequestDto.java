package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @Schema(description = "Unique identifier for the user", example = "USER001", required = true)
    @NotBlank(message = "User ID cannot be empty or spaces")
    @Size(min = 1, max = 8, message = "User ID must be between 1 and 8 characters")
    private String userId;

    @Schema(description = "Type of user (A for admin, U for user)", example = "U", required = true)
    @NotBlank(message = "User Type cannot be empty or contain only low-values")
    @Size(min = 1, max = 1, message = "User Type must be exactly 1 character")
    @Pattern(regexp = "[AUR]", message = "User Type must be A, U, or R")
    private String userType;

    @Schema(description = "Whether the user is authenticated", example = "false", required = true)
    @NotNull(message = "Authenticated status is required")
    private Boolean authenticated;

    @Schema(description = "User authentication password", example = "pass123", required = true)
    @NotBlank(message = "Password cannot be empty or spaces")
    @Size(min = 1, max = 8, message = "Password must be between 1 and 8 characters")
    private String password;

    @Schema(description = "User's first name", example = "John", required = true)
    @NotBlank(message = "First name cannot be empty or contain only low-values")
    @Size(min = 1, max = 25, message = "First name must be between 1 and 25 characters")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe", required = true)
    @NotBlank(message = "Last name cannot be empty or contain only low-values")
    @Size(min = 1, max = 25, message = "Last name must be between 1 and 25 characters")
    private String lastName;
}
