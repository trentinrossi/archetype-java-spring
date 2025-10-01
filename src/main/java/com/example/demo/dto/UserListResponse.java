package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    @Schema(description = "Unique identifier of the user", example = "USR12345", required = true)
    private String userId;

    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;

    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;

    @Schema(description = "Type of the user", example = "A", required = true)
    private String userType;

    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
}