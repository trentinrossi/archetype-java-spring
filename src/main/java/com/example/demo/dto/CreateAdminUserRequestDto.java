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
public class CreateAdminUserRequestDto {

    @Schema(description = "Unique identifier for the admin user", example = "ADMIN001", required = true)
    @NotNull(message = "User ID cannot be empty or spaces")
    @NotBlank(message = "User ID cannot be empty or spaces")
    @Size(min = 1, max = 8, message = "User ID must be between 1 and 8 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "User ID cannot be empty or spaces")
    private String userId;

    @Schema(description = "Indicates if user has been authenticated as admin", example = "true", required = true)
    @NotNull(message = "Authentication status is required")
    private Boolean authenticationStatus;
}
