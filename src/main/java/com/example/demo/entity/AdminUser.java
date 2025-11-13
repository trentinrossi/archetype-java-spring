package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {

    @Id
    @Column(name = "user_id", nullable = false, length = 8, unique = true)
    private String userId;

    @Column(name = "authentication_status", nullable = false)
    private Boolean authenticationStatus;

    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AdminMenuOption> adminMenuOptions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AdminUser(String userId, Boolean authenticationStatus) {
        this.userId = userId;
        this.authenticationStatus = authenticationStatus;
    }

    @PrePersist
    @PreUpdate
    private void validateAdminUser() {
        validateUserId();
        validateAuthenticationStatus();
    }

    private void validateUserId() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty or spaces");
        }

        if (userId.length() > 8) {
            throw new IllegalArgumentException("User ID cannot exceed 8 characters");
        }
    }

    private void validateAuthenticationStatus() {
        if (authenticationStatus == null) {
            throw new IllegalArgumentException("Authentication status is required");
        }
    }

    public boolean isAuthenticated() {
        return authenticationStatus != null && authenticationStatus;
    }

    public void authenticate() {
        this.authenticationStatus = true;
    }

    public void deauthenticate() {
        this.authenticationStatus = false;
    }

    public void addAdminMenuOption(AdminMenuOption adminMenuOption) {
        if (adminMenuOptions == null) {
            adminMenuOptions = new ArrayList<>();
        }
        adminMenuOptions.add(adminMenuOption);
        adminMenuOption.setAdminUser(this);
    }

    public void removeAdminMenuOption(AdminMenuOption adminMenuOption) {
        if (adminMenuOptions != null) {
            adminMenuOptions.remove(adminMenuOption);
            adminMenuOption.setAdminUser(null);
        }
    }

    public int getMenuOptionCount() {
        return adminMenuOptions != null ? adminMenuOptions.size() : 0;
    }
}
