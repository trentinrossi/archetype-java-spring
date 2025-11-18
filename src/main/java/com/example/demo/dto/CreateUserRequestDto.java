package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @Schema(description = "Unique user identifier", example = "USER001", required = true)
    @NotBlank(message = "User ID is required")
    @Size(max = 20, message = "User ID must not exceed 20 characters")
    private String userId;

    @Schema(description = "Type of user (ADMIN or REGULAR)", example = "REGULAR", required = true)
    @NotNull(message = "User type is required")
    private UserType userType;

    @Schema(description = "Username for login", example = "johndoe", required = true)
    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    private String username;

    @Schema(description = "User's email address", example = "john.doe@example.com", required = false)
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @Schema(description = "User's first name", example = "John", required = false)
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe", required = false)
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;
}
