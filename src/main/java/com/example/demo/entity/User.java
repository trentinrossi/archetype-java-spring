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
    @Column(name = "user_id", nullable = false, length = 8, unique = true)
    private String userId;

    @Column(name = "user_type", nullable = false, length = 1)
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

    public User(String userId, String userType, Boolean authenticated, String password, String firstName, String lastName) {
        this.userId = userId;
        this.userType = userType;
        this.authenticated = authenticated;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PrePersist
    @PreUpdate
    private void validateUser() {
        validateUserId();
        validateUserType();
        validatePassword();
        validateFirstName();
        validateLastName();
    }

    private void validateUserId() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty or spaces");
        }
        if (userId.length() > 8) {
            throw new IllegalArgumentException("User ID cannot exceed 8 characters");
        }
    }

    private void validateUserType() {
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("User Type cannot be empty or contain only low-values");
        }
        if (userType.length() != 1) {
            throw new IllegalArgumentException("User Type must be exactly 1 character");
        }
    }

    private void validatePassword() {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty or spaces");
        }
        if (password.length() > 8) {
            throw new IllegalArgumentException("Password cannot exceed 8 characters");
        }
    }

    private void validateFirstName() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty or contain only low-values");
        }
        if (firstName.length() > 25) {
            throw new IllegalArgumentException("First name cannot exceed 25 characters");
        }
    }

    private void validateLastName() {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty or contain only low-values");
        }
        if (lastName.length() > 25) {
            throw new IllegalArgumentException("Last name cannot exceed 25 characters");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isAuthenticated() {
        return Boolean.TRUE.equals(authenticated);
    }

    public boolean isAdmin() {
        return "A".equalsIgnoreCase(userType);
    }

    public boolean isRegularUser() {
        return "U".equalsIgnoreCase(userType) || "R".equalsIgnoreCase(userType);
    }

    public void authenticate() {
        this.authenticated = true;
    }

    public void deauthenticate() {
        this.authenticated = false;
    }

    public boolean verifyPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }
}
