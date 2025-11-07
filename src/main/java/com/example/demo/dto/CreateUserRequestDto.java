package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO for creating a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {
    
    @Schema(description = "Unique user identifier - maximum 20 characters", 
            example = "user123", 
            required = true)
    @NotBlank(message = "User ID is required")
    @Size(max = 20, message = "User ID must not exceed 20 characters")
    private String userId;
    
    @Schema(description = "Type of user - ADMIN or REGULAR", 
            example = "REGULAR", 
            required = true)
    @NotNull(message = "User type is required")
    private UserType userType;
    
    @Schema(description = "Set of account IDs this user has access to", 
            example = "[\"12345678901\", \"98765432109\"]")
    private Set<String> accountIds;
}
