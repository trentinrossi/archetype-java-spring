package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserSelectionRequestDto {

    @Schema(description = "Selection indicator: U for update, D for delete", example = "U", required = true)
    @NotBlank(message = "Selection flag is required")
    @Size(min = 1, max = 1, message = "Selection flag must be exactly 1 character")
    @Pattern(regexp = "[UD]", message = "Selection flag must be U (update) or D (delete)")
    private String selectionFlag;

    @Schema(description = "User ID of the selected user", example = "USER001", required = true)
    @NotBlank(message = "Selected user ID is required")
    @Size(max = 8, message = "Selected user ID must not exceed 8 characters")
    private String selectedUserId;
}
