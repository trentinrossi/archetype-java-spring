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
@Table(name = "merchants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {

    @Id
    @Column(name = "merchant_id", nullable = false, length = 9)
    private String merchantId;

    @Column(name = "merchant_name", nullable = false, length = 30)
    private String merchantName;

    @Column(name = "merchant_city", nullable = false, length = 25)
    private String merchantCity;

    @Column(name = "merchant_zip", nullable = false, length = 10)
    private String merchantZip;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Merchant(String merchantId, String merchantName, String merchantCity, String merchantZip) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.merchantCity = merchantCity;
        this.merchantZip = merchantZip;
    }

    /**
     * BR008: Merchant ID Validation - Merchant ID must be provided and numeric
     */
    public boolean isValidMerchantId() {
        if (merchantId == null || merchantId.trim().isEmpty()) {
            return false;
        }
        if (merchantId.length() > 9) {
            return false;
        }
        return merchantId.matches("\\d+");
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setMerchant(this);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setMerchant(null);
    }

    @PrePersist
    @PreUpdate
    public void validateMerchant() {
        if (merchantId == null || merchantId.trim().isEmpty()) {
            throw new IllegalStateException("Merchant ID must be entered");
        }
        if (!merchantId.matches("\\d+")) {
            throw new IllegalStateException("Merchant ID must be numeric");
        }
        if (merchantId.length() > 9) {
            throw new IllegalStateException("Merchant ID must not exceed 9 digits");
        }
        if (merchantName == null || merchantName.trim().isEmpty()) {
            throw new IllegalStateException("Merchant Name must be entered");
        }
        if (merchantCity == null || merchantCity.trim().isEmpty()) {
            throw new IllegalStateException("Merchant City must be entered");
        }
        if (merchantZip == null || merchantZip.trim().isEmpty()) {
            throw new IllegalStateException("Merchant Zip must be entered");
        }
    }
}
