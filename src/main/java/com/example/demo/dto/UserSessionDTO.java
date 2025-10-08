package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDTO {
    
    private String userId;
    
    private String firstName;
    
    private String lastName;
    
    private String userType;
    
    private String sessionToken;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sessionExpiry;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginAt;
    
    private String lastIpAddress;
    
    private Boolean twoFactorEnabled;
    
    private Boolean forcePasswordChange;
    
    private Boolean passwordExpired;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isAdmin() {
        return "A".equals(userType);
    }
    
    public boolean isSessionValid() {
        return sessionToken != null && 
               sessionExpiry != null && 
               sessionExpiry.isAfter(LocalDateTime.now());
    }
}