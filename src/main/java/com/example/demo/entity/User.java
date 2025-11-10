package com.example.demo.entity;

import com.example.demo.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a system user with specific permissions and access rights.
 * User type determines what credit cards they can view and manage.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 10)
    private UserType userType;
    
    @Column(name = "account_id", length = 11)
    private String accountId;
    
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;
    
    @Column(name = "first_name", length = 100)
    private String firstName;
    
    @Column(name = "last_name", length = 100)
    private String lastName;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Constructor for creating a new user
     * @param username the username
     * @param userType the type of user (ADMIN or REGULAR)
     * @param email the email address
     */
    public User(String username, UserType userType, String email) {
        this.username = username;
        this.userType = userType;
        this.email = email;
        this.active = true;
    }
    
    /**
     * Get the full name of the user
     * @return full name or username if names not set
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }
    
    /**
     * Check if user is an administrator
     * @return true if user is admin
     */
    public boolean isAdmin() {
        return userType != null && userType.isAdmin();
    }
    
    /**
     * Check if user can view all cards
     * Admin users can view all cards when no context is passed
     * @return true if user can view all cards
     */
    public boolean canViewAllCards() {
        return userType != null && userType.canViewAllCards();
    }
    
    /**
     * Check if user is restricted to their account
     * Regular users can only view cards associated with their specific account
     * @return true if user is restricted to their account
     */
    public boolean isAccountRestricted() {
        return userType != null && userType.isAccountRestricted();
    }
}
