package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionType {

    @Id
    @Column(name = "transaction_type_code", nullable = false, length = 2)
    private String transactionTypeCode;

    @Column(name = "transaction_type_description", nullable = false, length = 50)
    private String transactionTypeDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
