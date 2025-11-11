package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateUserRequestDto
 * 
 * Request DTO for updating an existing user.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Schema(description = "Type of user (ADMIN or REGULAR)", 
            example = "ADMIN", 
            required = false)
    @Pattern(regexp = "^(ADMIN|REGULAR)$", 
             message = "User type must be either ADMIN or REGULAR")
    private String userType;
}
