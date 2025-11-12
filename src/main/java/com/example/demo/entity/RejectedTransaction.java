package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rejected_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectedTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rejected_transaction_id")
    private Long rejectedTransactionId;

    @Column(name = "transaction_data", nullable = false, length = 350)
    private String transactionData;

    @Column(name = "rejection_reason_code", nullable = false, length = 4)
    private String rejectionReasonCode;

    @Column(name = "rejection_reason_description", nullable = false, length = 76)
    private String rejectionReasonDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (transactionData == null || transactionData.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction data is required");
        }
        if (rejectionReasonCode == null || rejectionReasonCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason code is required");
        }
    }
}
