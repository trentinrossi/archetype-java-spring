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
    @Column(name = "type_code", nullable = false, length = 2)
    private String typeCode;
    
    @Id
    @Column(name = "category_code", nullable = false, length = 4)
    private String categoryCode;
    
    @Column(name = "description", nullable = false, length = 50)
    private String description;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionCategoryId implements Serializable {
        private String typeCode;
        private String categoryCode;
    }
}
