package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false, length = 4)
    private String transactionId;

    @Column(name = "program_name", nullable = false, length = 8)
    private String programName;

    @Column(name = "from_program", length = 8)
    private String fromProgram;

    @Column(name = "from_transaction", length = 4)
    private String fromTransaction;

    @Column(name = "program_context", length = 9)
    private Integer programContext;

    @Column(name = "reenter_flag", nullable = false, length = 1)
    private Boolean reenterFlag;

    @Column(name = "to_program", nullable = false, length = 8)
    private String toProgram;

    @Column(name = "program_reenter_flag", nullable = false, length = 1)
    private Boolean programReenterFlag;

    @Column(name = "user_type", nullable = false, length = 1)
    private String userType;

    @Column(name = "user_id", nullable = false, length = 8)
    private String userId;

    @Column(name = "from_transaction_id", nullable = false, length = 4)
    private String fromTransactionId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateUserSession() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID can NOT be empty...");
        }
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("User Type can NOT be empty...");
        }
    }
}
