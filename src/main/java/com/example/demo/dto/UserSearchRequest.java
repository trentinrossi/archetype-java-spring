package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User search request")
public class UserSearchRequest {
    
    @Schema(description = "Search term for user ID", example = "USR001", required = false)
    private String secUsrId;
    
    @Schema(description = "Search term for first name", example = "John", required = false)
    private String secUsrFname;
    
    @Schema(description = "Search term for last name", example = "Doe", required = false)
    private String secUsrLname;
    
    @Schema(description = "Search term for user type", example = "A", required = false)
    private String secUsrType;
}