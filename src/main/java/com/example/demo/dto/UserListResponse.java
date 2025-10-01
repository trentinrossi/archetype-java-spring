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
    
    @Schema(description = "Unique user identifier", example = "USR00001", required = true)
    private String secUsrId;
    
    @Schema(description = "First name of the user", example = "John", required = true)
    private String secUsrFname;
    
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String secUsrLname;
    
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "User type - A for Admin, U for User", example = "U", required = true)
    private String secUsrType;
    
    @Schema(description = "User type display name", example = "User", required = true)
    private String userTypeDisplayName;
    
    @Schema(description = "Timestamp when the user was created", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
}