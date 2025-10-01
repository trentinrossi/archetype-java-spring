package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

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
    @Schema(description = "Password of the user", example = "pass123", required = false)
    private String password;

    @Size(min = 1, max = 1, message = "User type must be exactly 1 character")
    @Schema(description = "Type of the user", example = "A", required = false)
    private String userType;
}