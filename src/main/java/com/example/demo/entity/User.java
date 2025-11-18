package com.example.demo.entity;

import com.example.demo.enums.UserType;
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
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id", unique = true, nullable = false, length = 20)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 10)
    private UserType userType;
    
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(name = "email", unique = true, length = 255)
    private String email;
    
    @Column(name = "first_name", length = 100)
    private String firstName;
    
    @Column(name = "last_name", length = 100)
    private String lastName;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public User(String userId, UserType userType, String username) {
        this.userId = userId;
        this.userType = userType;
        this.username = username;
    }
    
    public boolean isAdmin() {
        return UserType.ADMIN.equals(this.userType);
    }
    
    public boolean isRegular() {
        return UserType.REGULAR.equals(this.userType);
    }
    
    /**
     * BR001: User Authorization for Card Viewing
     * Admin users can view all credit cards
     * Regular users can only view cards associated with their account
     */
    public boolean canViewAllCards() {
        return isAdmin();
    }
    
    /**
     * BR001: User Authorization for Card Viewing
     * Check if user can view a specific account's cards
     */
    public boolean canViewAccount(Long accountId) {
        if (isAdmin()) {
            return true;
        }
        // Regular users can only view their own accounts
        return accounts.stream()
                .anyMatch(account -> account.getAccountId().equals(accountId));
    }
    
    public void addAccount(Account account) {
        accounts.add(account);
        account.setUser(this);
    }
    
    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setUser(null);
    }
    
    public int getAccountCount() {
        return accounts != null ? accounts.size() : 0;
    }
    
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username;
    }
    
    @PrePersist
    @PreUpdate
    private void validateUser() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (userType == null) {
            throw new IllegalArgumentException("User type is required");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
    }
}
