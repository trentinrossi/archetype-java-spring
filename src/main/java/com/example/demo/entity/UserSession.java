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
    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "transaction_id", nullable = false, length = 4)
    private String transactionId;

    @Column(name = "program_name", nullable = false, length = 8)
    private String programName;

    @Column(name = "from_program", length = 8)
    private String fromProgram;

    @Column(name = "from_transaction", length = 4)
    private String fromTransaction;

    @Column(name = "program_context")
    private Integer programContext;

    @Column(name = "reenter_flag", nullable = false)
    private Boolean reenterFlag = false;

    @Column(name = "to_program", nullable = false, length = 8)
    private String toProgram;

    @Column(name = "program_reenter_flag", nullable = false)
    private Boolean programReenterFlag = false;

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
    public void validate() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("User type is required");
        }
    }
}
