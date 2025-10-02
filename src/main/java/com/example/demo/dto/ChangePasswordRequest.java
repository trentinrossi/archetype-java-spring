package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    
    @NotBlank(message = "Please enter Password")
    @Size(max = 8, message = "Password must not exceed 8 characters")
    @Schema(description = "New password", example = "NEWPASS", required = true, maxLength = 8)
    private String newPassword;
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword != null ? newPassword.toUpperCase() : null;
    }
}