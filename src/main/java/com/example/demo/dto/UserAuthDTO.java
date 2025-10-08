package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDTO {
    
    @NotBlank(message = "User ID cannot be blank")
    @Size(min = 8, max = 8, message = "User ID must be exactly 8 characters")
    private String userId;
    
    @NotBlank(message = "Password cannot be blank")
    private String password;
    
    private String twoFactorCode;
    
    private String ipAddress;
    
    private String userAgent;
}