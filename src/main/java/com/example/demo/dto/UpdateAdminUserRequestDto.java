package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminUserRequestDto {

    @Schema(description = "Indicates if user has been authenticated as admin", example = "true", required = false)
    private Boolean authenticationStatus;
}
