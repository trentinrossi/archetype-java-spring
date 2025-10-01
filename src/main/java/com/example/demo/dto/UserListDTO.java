package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {
    @Schema(description = "User ID", example = "USR00001", required = true)
    private String secUsrId;

    @Schema(description = "First name of the user", example = "John", required = true)
    private String secUsrFname;

    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String secUsrLname;

    @Schema(description = "User type", example = "A", required = true)
    private String secUsrType;
}