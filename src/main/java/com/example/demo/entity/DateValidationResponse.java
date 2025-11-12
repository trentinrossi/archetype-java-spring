package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "date_validation_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateValidationResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "severity", nullable = false, length = 4)
    private Integer severity;

    @Column(name = "message_code", nullable = false, length = 4)
    private Integer messageCode;

    @Column(name = "result", nullable = false, length = 15)
    private String result;

    @Column(name = "test_date", nullable = false, length = 10)
    private String testDate;

    @Column(name = "mask_used", nullable = false, length = 10)
    private String maskUsed;

    @Column(name = "full_message", nullable = false, length = 80)
    private String fullMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
