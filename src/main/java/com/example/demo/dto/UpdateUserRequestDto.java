package com.example.demo.dto;

import com.example.demo.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO for updating an existing user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {
    
    @Schema(description = "Type of user - ADMIN or REGULAR", 
            example = "ADMIN")
    private UserType userType;
    
    @Schema(description = "Set of account IDs this user has access to", 
            example = "[\"12345678901\", \"98765432109\"]")
    private Set<String> accountIds;
}
