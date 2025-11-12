package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "disclosure_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DisclosureGroup.DisclosureGroupId.class)
public class DisclosureGroup {

    @Id
    @Column(name = "account_group_id", nullable = false, length = 10)
    private String accountGroupId;

    @Id
    @Column(name = "transaction_category_code", nullable = false, length = 4)
    private String transactionCategoryCode;

    @Id
    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (accountGroupId == null || accountGroupId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account group ID is required");
        }
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate must be greater than or equal to zero");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisclosureGroupId implements Serializable {
        private String accountGroupId;
        private String transactionCategoryCode;
        private String transactionTypeCode;
    }
}
