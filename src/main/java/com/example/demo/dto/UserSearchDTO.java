package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO {
    @Size(max = 8, message = "User ID must not exceed 8 characters")
    @Schema(description = "Starting characters of User ID to filter by", example = "USR", required = false)
    private String secUsrIdStartsWith;
}