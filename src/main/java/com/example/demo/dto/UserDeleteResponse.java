package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User deletion response")
public class UserDeleteResponse {
    
    @Schema(description = "User ID that was deleted", example = "USR001", required = true)
    private String secUsrId;
    
    @Schema(description = "Deletion status message", example = "User successfully deleted", required = true)
    private String message;
    
    @Schema(description = "Deletion timestamp", example = "2023-10-01T12:00:00", required = true)
    private String deletedAt;
}