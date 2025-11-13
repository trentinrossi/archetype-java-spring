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
public class CreateUserRequestDto {

    @Schema(description = "Type of user (regular or admin)", example = "A", required = true)
    @NotNull(message = "User Type can NOT be empty...")
    @NotBlank(message = "User Type can NOT be empty...")
    @Size(min = 1, max = 1, message = "User Type can NOT be empty...")
    @Pattern(regexp = "^(?!\\s*$).+", message = "User Type can NOT be empty...")
    private String userType;

    @Schema(description = "Whether the user is authenticated", example = "true", required = true)
    @NotNull(message = "Authenticated status is required")
    private Boolean authenticated;

    @Schema(description = "Unique identifier for the user", example = "USR00001", required = true)
    @NotNull(message = "User ID can NOT be empty...")
    @NotBlank(message = "User ID can NOT be empty...")
    @Size(min = 1, max = 8, message = "User ID can NOT be empty...")
    @Pattern(regexp = "^(?!\\s*$).+", message = "User ID can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "User ID can NOT be empty...")
    private String userId;

    @Schema(description = "User authentication password", example = "Pass1234", required = true)
    @NotNull(message = "Password can NOT be empty...")
    @NotBlank(message = "Password can NOT be empty...")
    @Size(min = 1, max = 8, message = "Password can NOT be empty...")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Password can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "Password can NOT be empty...")
    private String password;

    @Schema(description = "User's first name", example = "John", required = true)
    @NotNull(message = "First Name can NOT be empty...")
    @NotBlank(message = "First Name can NOT be empty...")
    @Size(min = 1, max = 25, message = "First Name can NOT be empty...")
    @Pattern(regexp = "^(?!\\s*$).+", message = "First Name can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "First Name can NOT be empty...")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe", required = true)
    @NotNull(message = "Last Name can NOT be empty...")
    @NotBlank(message = "Last Name can NOT be empty...")
    @Size(min = 1, max = 25, message = "Last Name can NOT be empty...")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Last Name can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "Last Name can NOT be empty...")
    private String lastName;
}
