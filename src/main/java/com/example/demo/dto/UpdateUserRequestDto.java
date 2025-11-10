package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {
    
    @Schema(description = "Type of user (ADMIN or REGULAR)", example = "REGULAR", required = false)
    private UserType userType;
    
    @Schema(description = "Account ID for regular users (11 digits)", example = "12345678901", required = false)
    @Pattern(regexp = "^\\d{11}$", message = "Account ID must be exactly 11 digits if provided")
    private String accountId;
    
    @Schema(description = "Email address of the user", example = "john.doe@example.com", required = false)
    @Email(message = "Email must be valid")
    private String email;
    
    @Schema(description = "First name of the user", example = "John", required = false)
    private String firstName;
    
    @Schema(description = "Last name of the user", example = "Doe", required = false)
    private String lastName;
    
    @Schema(description = "Active status of the user", example = "true", required = false)
    private Boolean active;
}
