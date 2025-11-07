package com.example.demo.entity;

import com.example.demo.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a system user with specific permissions and access rights.
 * Business Rule BR001: Admin users can view all credit cards when no context is passed.
 * Non-admin users can only view cards associated with their specific account.
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_user_type", columnList = "user_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @Column(name = "user_id", nullable = false, unique = true, length = 20)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 10)
    private UserType userType;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "user_account_access",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> accounts = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Constructor with user ID and type
     */
    public User(String userId, UserType userType) {
        this.userId = userId;
        this.userType = userType;
    }
    
    /**
     * Check if user is an administrator
     * Business Rule BR001: Admin users have elevated permissions
     */
    public boolean isAdmin() {
        return userType != null && userType.isAdmin();
    }
    
    /**
     * Check if user can view all credit cards
     * Business Rule BR001: Admin users can view all cards when no context is passed
     */
    public boolean canViewAllCards() {
        return isAdmin();
    }
    
    /**
     * Check if user has access to a specific account
     * Business Rule BR001: Non-admin users can only view cards associated with their specific account
     */
    public boolean hasAccessToAccount(String accountId) {
        if (isAdmin()) {
            return true; // Admin has access to all accounts
        }
        return accounts.stream()
                .anyMatch(account -> account.getAccountId().equals(accountId));
    }
    
    /**
     * Add account access for this user
     */
    public void addAccountAccess(Account account) {
        accounts.add(account);
        account.getUsers().add(this);
    }
    
    /**
     * Remove account access for this user
     */
    public void removeAccountAccess(Account account) {
        accounts.remove(account);
        account.getUsers().remove(this);
    }
    
    /**
     * Get display name for user type
     */
    public String getUserTypeDisplayName() {
        return userType != null ? userType.getDisplayName() : "Unknown";
    }
}
