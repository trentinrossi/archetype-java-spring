package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "user_type", nullable = false, length = 10)
    private String userType;

    @Column(name = "authenticated", nullable = false)
    private Boolean authenticated;

    @Column(name = "password", nullable = false, length = 8)
    private String password;

    @Column(name = "first_name", nullable = false, length = 25)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 25)
    private String lastName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateUser() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password can NOT be empty...");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First Name can NOT be empty...");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name can NOT be empty...");
        }
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }
        if (!firstName.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("First name is required and must contain only letters");
        }
        if (!lastName.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Last name is required and must contain only letters");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
