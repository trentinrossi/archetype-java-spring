package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "date_validation_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateValidationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "date", nullable = false, length = 10)
    private String date;

    @Column(name = "date_format", nullable = false, length = 10)
    private String dateFormat;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Date is required");
        }
        if (dateFormat == null || dateFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("Date format is required");
        }
    }
}
