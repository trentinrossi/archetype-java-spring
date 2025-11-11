package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminUserRequestDto {
    
    @Schema(description = "Unique identifier for the admin user", example = "ADMIN001", required = false)
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    private String userId;

    @Schema(description = "Indicates if user has been authenticated as admin", example = "true", required = false)
    private Boolean authenticationStatus;
}
