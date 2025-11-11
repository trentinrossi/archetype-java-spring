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
import java.util.stream.Collectors;

/**
 * User Entity
 * 
 * Represents a system user with specific permissions and access rights.
 * 
 * Field Specifications:
 * - user_id: alphanumeric (length: 20), required, unique identifier
 * - user_type: alphanumeric (length: 10), required, enum (ADMIN or REGULAR)
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR003: Single Selection Enforcement
 * - BR008: First Page Navigation Restriction
 * - BR009: Last Page Navigation Restriction
 * - BR011: Exit to Menu
 * - BR012: View Card Details
 * - BR013: Update Card Information
 * - BR014: Forward Pagination
 * - BR015: Backward Pagination
 * - BR017: Input Error Protection
 * 
 * Relationships:
 * - Many-to-Many with Account (for access control)
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id", nullable = false, length = 20)
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

    public User(String userId, UserType userType) {
        this.userId = userId;
        this.userType = userType;
    }

    /**
     * Checks if this user is an administrator
     * Implements BR001: User Permission Based Card Access
     * 
     * @return true if user type is ADMIN, false otherwise
     */
    public boolean isAdmin() {
        return this.userType != null && this.userType.isAdmin();
    }

    /**
     * Checks if this user is a regular user
     * Implements BR001: User Permission Based Card Access
     * 
     * @return true if user type is REGULAR, false otherwise
     */
    public boolean isRegularUser() {
        return this.userType != null && this.userType.isRegularUser();
    }

    /**
     * Checks if this user can view all credit cards without context
     * Implements BR001: User Permission Based Card Access
     * 
     * Business Logic:
     * - Admin users can view all credit cards without context
     * - Regular users can only view cards associated with their specific account
     * 
     * @return true if user can view all cards, false otherwise
     */
    public boolean canViewAllCards() {
        return isAdmin();
    }

    /**
     * Checks if this user has access to a specific account
     * Implements BR001: User Permission Based Card Access
     * 
     * @param accountId The account ID to check access for
     * @return true if user has access, false otherwise
     */
    public boolean hasAccessToAccount(String accountId) {
        if (isAdmin()) {
            return true;
        }
        return accounts.stream()
                .anyMatch(account -> account.getAccountId().equals(accountId));
    }

    /**
     * Checks if this user can view a card for a specific account
     * Implements BR001: User Permission Based Card Access
     * 
     * @param accountId The account ID associated with the card
     * @return true if user can view the card, false otherwise
     */
    public boolean canViewCard(String accountId) {
        if (isAdmin()) {
            return true;
        }
        return hasAccessToAccount(accountId);
    }

    /**
     * Determines if account context is required for this user
     * Implements BR001: User Permission Based Card Access
     * 
     * @return true if account context is required, false otherwise
     */
    public boolean requiresAccountContext() {
        return isRegularUser();
    }

    /**
     * Gets all account IDs accessible by this user
     * Implements BR001: User Permission Based Card Access
     * 
     * @return Set of accessible account IDs
     */
    public Set<String> getAccessibleAccountIds() {
        return accounts.stream()
                .map(Account::getAccountId)
                .collect(Collectors.toSet());
    }

    /**
     * Validates user data before persisting or updating
     */
    @PrePersist
    @PreUpdate
    private void validateUser() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalStateException("User ID cannot be null or empty");
        }
        if (userId.length() > 20) {
            throw new IllegalStateException("User ID cannot exceed 20 characters");
        }
        if (userType == null) {
            throw new IllegalStateException("User type cannot be null");
        }
    }
}
