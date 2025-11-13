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
public class LoginRequestDto {

    @Schema(description = "User ID for authentication", example = "USR00001", required = true)
    @NotNull(message = "User ID can NOT be empty...")
    @NotBlank(message = "User ID can NOT be empty...")
    @Size(min = 1, max = 8, message = "User ID can NOT be empty...")
    @Pattern(regexp = "^(?!\\s*$).+", message = "User ID can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "User ID can NOT be empty...")
    private String userId;

    @Schema(description = "Password for authentication", example = "Pass1234", required = true)
    @NotNull(message = "Password can NOT be empty...")
    @NotBlank(message = "Password can NOT be empty...")
    @Size(min = 1, max = 8, message = "Password can NOT be empty...")
    @Pattern(regexp = "^(?!\\s*$).+", message = "Password can NOT be empty...")
    @Pattern(regexp = "^(?!.*\\x00).*$", message = "Password can NOT be empty...")
    private String password;
}
