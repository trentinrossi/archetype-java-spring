package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
    
    private List<UserSummary> users;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
    private String sortBy;
    private String sortDirection;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private String userId;
        private String firstName;
        private String lastName;
        private String fullName;
        private String userType;
        private String userTypeDisplayName;
        private String userStatus;
        private String userStatusDisplayName;
        private Boolean accountLocked;
        private Boolean passwordExpired;
        private Boolean isActive;
        private Boolean isAdmin;
        private java.time.LocalDateTime lastLoginAt;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;
        private Long loginCount;
        private Integer failedLoginAttempts;
        private Boolean isHighRisk;
    }
}