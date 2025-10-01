package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User response")
public class UserResponse {
    
    @Schema(description = "User ID", example = "USR001", required = true)
    private String secUsrId;
    
    @Schema(description = "User first name", example = "John", required = true)
    private String secUsrFname;
    
    @Schema(description = "User last name", example = "Doe", required = true)
    private String secUsrLname;
    
    @Schema(description = "User type (A=Admin, U=User)", example = "U", required = true)
    private String secUsrType;
    
    @Schema(description = "Full name", example = "John Doe", required = true)
    private String fullName;
    
    @Schema(description = "Creation timestamp", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp", example = "2023-10-01T12:00:00", required = true)
    private LocalDateTime updatedAt;
    
    public UserResponse(String secUsrId, String secUsrFname, String secUsrLname, String secUsrType) {
        this.secUsrId = secUsrId;
        this.secUsrFname = secUsrFname;
        this.secUsrLname = secUsrLname;
        this.secUsrType = secUsrType;
        this.fullName = secUsrFname + " " + secUsrLname;
    }
}