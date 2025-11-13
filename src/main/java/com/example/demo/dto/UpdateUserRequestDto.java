package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Schema(description = "Type of user (regular or admin)", example = "A", required = false)
    @Size(max = 1, message = "User Type must not exceed 1 character")
    @Pattern(regexp = "^(?!\\s*$).+", message = "User Type can NOT be empty...")
    private String userType;

    @Schema(description = "Whether the user is authenticated", example = "true", required = false)
    private Boolean authenticated;

    @Schema(description = "User authentication password", example = "Pass1234", required = false)
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Password can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "Password can NOT be empty...")
    private String password;

    @Schema(description = "User's first name", example = "John", required = false)
    @Size(max = 25, message = "First Name must not exceed 25 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "First Name can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "First Name can NOT be empty...")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe", required = false)
    @Size(max = 25, message = "Last Name must not exceed 25 characters")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Last Name can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "Last Name can NOT be empty...")
    private String lastName;
}
