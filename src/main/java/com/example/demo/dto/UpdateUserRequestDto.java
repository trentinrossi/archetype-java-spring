package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Schema(description = "Type of user", example = "U", required = false)
    @Size(min = 1, max = 1, message = "User Type must be exactly 1 character")
    @Pattern(regexp = "[AUR]", message = "User Type must be A, U, or R")
    private String userType;

    @Schema(description = "Whether the user is authenticated", example = "true", required = false)
    private Boolean authenticated;

    @Schema(description = "User authentication password", example = "newpass", required = false)
    @Size(min = 1, max = 8, message = "Password must be between 1 and 8 characters")
    private String password;

    @Schema(description = "User's first name", example = "John", required = false)
    @Size(min = 1, max = 25, message = "First name must be between 1 and 25 characters")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe", required = false)
    @Size(min = 1, max = 25, message = "Last name must be between 1 and 25 characters")
    private String lastName;
}
