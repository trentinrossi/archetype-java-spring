package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransactionCategory.TransactionCategoryId.class)
public class TransactionCategory {

    @Id
    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;

    @Id
    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private String transactionCategoryCode;

    @Column(name = "category_description", nullable = false, length = 50)
    private String categoryDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (transactionTypeCode == null || transactionTypeCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction type code is required");
        }
        if (transactionCategoryCode == null || transactionCategoryCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction category code is required");
        }
        if (categoryDescription == null || categoryDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Category description is required");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionCategoryId implements Serializable {
        private String transactionTypeCode;
        private String transactionCategoryCode;
    }
}
