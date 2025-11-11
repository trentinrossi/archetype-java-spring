package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Account Entity
 * 
 * Represents a customer account that owns one or more credit cards.
 * 
 * Field Specifications:
 * - account_id: numeric (length: 11), required, unique identifier
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR004: Account Filter Validation
 * - BR006: Filter Record Matching
 * 
 * Relationships:
 * - One-to-Many with CreditCard
 * - Many-to-Many with User (for access control)
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "account_id", nullable = false, length = 11)
    private String accountId;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CreditCard> creditCards = new ArrayList<>();

    @ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Account(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Validates account ID before persisting or updating
     * Implements BR004: Account Filter Validation
     */
    @PrePersist
    @PreUpdate
    private void validateAccountId() {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }

        String trimmedAccountId = accountId.trim();

        // BR004: Cannot be blank, spaces, or zeros
        if (trimmedAccountId.matches("^0+$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER CANNOT BE BLANK, SPACES OR ZEROS");
        }

        // BR004: Must be numeric and exactly 11 digits
        if (!trimmedAccountId.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("ACCOUNT FILTER,IF SUPPLIED MUST BE A 11 DIGIT NUMBER");
        }
    }

    /**
     * Validates if a given account ID is valid
     * Implements BR004: Account Filter Validation
     * 
     * @param accountId The account ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAccountId(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            return false;
        }

        String trimmedAccountId = accountId.trim();

        // Cannot be all zeros
        if (trimmedAccountId.matches("^0+$")) {
            return false;
        }

        // Must be numeric and exactly 11 digits
        if (!trimmedAccountId.matches("^\\d{11}$")) {
            return false;
        }

        return true;
    }

    /**
     * Adds a credit card to this account
     * 
     * @param creditCard The credit card to add
     */
    public void addCreditCard(CreditCard creditCard) {
        creditCards.add(creditCard);
        creditCard.setAccount(this);
    }

    /**
     * Removes a credit card from this account
     * 
     * @param creditCard The credit card to remove
     */
    public void removeCreditCard(CreditCard creditCard) {
        creditCards.remove(creditCard);
        creditCard.setAccount(null);
    }

    /**
     * Grants access to this account for a user
     * 
     * @param user The user to grant access to
     */
    public void grantAccessToUser(User user) {
        users.add(user);
        user.getAccounts().add(this);
    }

    /**
     * Revokes access to this account from a user
     * 
     * @param user The user to revoke access from
     */
    public void revokeAccessFromUser(User user) {
        users.remove(user);
        user.getAccounts().remove(this);
    }
}
