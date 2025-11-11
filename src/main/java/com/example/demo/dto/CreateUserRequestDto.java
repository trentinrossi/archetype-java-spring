package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateUserRequestDto
 * 
 * Request DTO for creating a new user.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @Schema(description = "Unique user identifier", 
            example = "USR12345678901234567", 
            required = true)
    @NotBlank(message = "User ID is required")
    @Size(max = 20, message = "User ID must not exceed 20 characters")
    private String userId;

    @Schema(description = "Type of user (ADMIN or REGULAR)", 
            example = "ADMIN", 
            required = true)
    @NotBlank(message = "User type is required")
    @Pattern(regexp = "^(ADMIN|REGULAR)$", 
             message = "User type must be either ADMIN or REGULAR")
    private String userType;
}
