package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Schema(description = "Type of user (ADMIN or REGULAR)", example = "REGULAR", required = false)
    private UserType userType;

    @Schema(description = "Username for login", example = "johndoe", required = false)
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
