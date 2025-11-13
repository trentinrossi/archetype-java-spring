package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_type", nullable = false, length = 1)
    private String userType;
    
    @Column(name = "authenticated", nullable = false)
    private Boolean authenticated = false;
    
    @Column(name = "user_id", unique = true, nullable = false, length = 8)
    private String userId;
    
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
    
    public User(String userType, Boolean authenticated, String userId, String password, String firstName, String lastName) {
        this.userType = userType;
        this.authenticated = authenticated;
        this.userId = userId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isAdmin() {
        return "A".equalsIgnoreCase(userType);
    }
    
    public boolean isRegularUser() {
        return "R".equalsIgnoreCase(userType);
    }
    
    public boolean isAuthenticated() {
        return authenticated != null && authenticated;
    }
    
    public boolean isUserIdValid() {
        return userId != null && !userId.trim().isEmpty() && !userId.matches("^[\\x00]+$");
    }
    
    public boolean isPasswordValid() {
        return password != null && !password.trim().isEmpty() && !password.matches("^[\\x00]+$");
    }
    
    public boolean isUserTypeValid() {
        return userType != null && !userType.trim().isEmpty() && !userType.matches("^[\\x00]+$");
    }
    
    public boolean isFirstNameValid() {
        return firstName != null && !firstName.trim().isEmpty() && !firstName.matches("^[\\x00]+$");
    }
    
    public boolean isLastNameValid() {
        return lastName != null && !lastName.trim().isEmpty() && !lastName.matches("^[\\x00]+$");
    }
    
    @PrePersist
    @PreUpdate
    public void validateUser() {
        if (!isUserTypeValid()) {
            throw new IllegalStateException("User Type can NOT be empty...");
        }
        
        if (!isUserIdValid()) {
            throw new IllegalStateException("User ID can NOT be empty...");
        }
        
        if (!isPasswordValid()) {
            throw new IllegalStateException("Password can NOT be empty...");
        }
        
        if (!isFirstNameValid()) {
            throw new IllegalStateException("First Name can NOT be empty...");
        }
        
        if (!isLastNameValid()) {
            throw new IllegalStateException("Last Name can NOT be empty...");
        }
        
        if (userId != null && userId.length() > 8) {
            throw new IllegalStateException("User ID cannot exceed 8 characters");
        }
        
        if (password != null && password.length() > 8) {
            throw new IllegalStateException("Password cannot exceed 8 characters");
        }
        
        if (firstName != null && firstName.length() > 25) {
            throw new IllegalStateException("First Name cannot exceed 25 characters");
        }
        
        if (lastName != null && lastName.length() > 25) {
            throw new IllegalStateException("Last Name cannot exceed 25 characters");
        }
        
        if (userType != null && userType.length() > 1) {
            throw new IllegalStateException("User Type cannot exceed 1 character");
        }
    }
    
    public boolean canAccessAdminOption() {
        return isAuthenticated() && isAdmin();
    }
    
    public boolean matchesPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }
}
