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
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Card(String cardNumber, Customer customer, Account account) {
        this.cardNumber = cardNumber;
        this.customer = customer;
        this.account = account;
    }

    @PrePersist
    @PreUpdate
    private void validateCard() {
        validateCardNumber();
        validateCustomerId();
        validateAccountId();
        validateTransactionCapacity();
    }

    private void validateCardNumber() {
        if (cardNumber == null || !cardNumber.matches("^[a-zA-Z0-9]{16}$")) {
            throw new IllegalArgumentException("Card number must be 16 characters alphanumeric");
        }
    }

    private void validateCustomerId() {
        if (customer == null || customer.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID must be 9 digits numeric and exist in customer file");
        }
        String customerIdStr = String.valueOf(customer.getCustomerId());
        if (!customerIdStr.matches("^\\d{9}$")) {
            throw new IllegalArgumentException("Customer ID must be 9 digits numeric");
        }
    }

    private void validateAccountId() {
        if (account == null || account.getAccountId() == null) {
            throw new IllegalArgumentException("Account ID must be 11 digits numeric and exist in account file");
        }
        String accountIdStr = String.valueOf(account.getAccountId());
        if (!accountIdStr.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("Account ID must be 11 digits numeric");
        }
    }

    /**
     * BR005: Transaction Table Capacity Limit
     * Validates that card does not exceed maximum of 10 transactions
     */
    private void validateTransactionCapacity() {
        if (transactions != null && transactions.size() > 10) {
            throw new IllegalStateException("Transaction capacity limit exceeded: maximum 10 transactions per card");
        }
    }

    /**
     * BR005: Check if card can accept more transactions
     */
    public boolean canAddTransaction() {
        return transactions.size() < 10;
    }

    /**
     * Get current transaction count for this card
     */
    public int getTransactionCount() {
        return transactions != null ? transactions.size() : 0;
    }

    /**
     * BR001: Transaction Grouping by Card
     * Add a transaction to this card with capacity validation
     */
    public void addTransaction(Transaction transaction) {
        if (!canAddTransaction()) {
            throw new IllegalStateException("Cannot add transaction: maximum capacity of 10 transactions reached");
        }
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
        transaction.setCard(this);
    }

    /**
     * Remove a transaction from this card
     */
    public void removeTransaction(Transaction transaction) {
        if (transactions != null) {
            transactions.remove(transaction);
            transaction.setCard(null);
        }
    }

    /**
     * BR009: Card-Account-Customer Linkage
     * Verify card is properly linked to customer
     */
    public boolean isLinkedToCustomer() {
        return customer != null && customer.getCustomerId() != null;
    }

    /**
     * BR009: Card-Account-Customer Linkage
     * Verify card is properly linked to account
     */
    public boolean isLinkedToAccount() {
        return account != null && account.getAccountId() != null;
    }

    /**
     * BR009: Card-Account-Customer Linkage
     * Verify complete linkage between card, account, and customer
     */
    public boolean hasValidLinkage() {
        return isLinkedToCustomer() && isLinkedToAccount();
    }

    /**
     * BR001: Get all transactions for this card for statement generation
     */
    public List<Transaction> getTransactionsForStatement() {
        return transactions != null ? new ArrayList<>(transactions) : new ArrayList<>();
    }
}
