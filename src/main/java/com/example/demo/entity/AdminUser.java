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
    @Column(name = "user_id", nullable = false, length = 8)
    private String userId;

    @Column(name = "authentication_status", nullable = false)
    private Boolean authenticationStatus;

    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
        this.adminMenuOptions = new ArrayList<>();
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

    @PrePersist
    protected void onCreate() {
        if (authenticationStatus == null) {
            authenticationStatus = false;
        }
        if (userId != null && userId.length() > 8) {
            throw new IllegalStateException("User ID cannot exceed 8 characters");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (userId != null && userId.length() > 8) {
            throw new IllegalStateException("User ID cannot exceed 8 characters");
        }
    }
}
