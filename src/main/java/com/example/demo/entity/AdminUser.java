package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {

    @Id
    @Column(name = "user_id", nullable = false, length = 8)
    private String userId;

    @Column(name = "authentication_status", nullable = false, length = 1)
    private Boolean authenticationStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateAdminUser() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
    }
}
