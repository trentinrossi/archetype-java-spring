package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {
    
    @Schema(description = "Username for the user", example = "john.doe", required = true)
    @NotBlank(message = "Username is required")
    private String username;
    
    @Schema(description = "Type of user (ADMIN or REGULAR)", example = "REGULAR", required = true)
    @NotNull(message = "User type is required")
    private UserType userType;
    
    @Schema(description = "Account ID for regular users (11 digits)", example = "12345678901", required = false)
    @Pattern(regexp = "^\\d{11}$", message = "Account ID must be exactly 11 digits if provided")
    private String accountId;
    
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Schema(description = "First name of the user", example = "John", required = false)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String lastName;
}
