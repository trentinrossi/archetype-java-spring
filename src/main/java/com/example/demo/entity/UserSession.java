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

    @Column(name = "program_context")
    private Long programContext;

    @Column(name = "reenter_flag", nullable = false)
    private Boolean reenterFlag;

    @Column(name = "to_program", nullable = false, length = 8)
    private String toProgram;

    @Column(name = "program_reenter_flag", nullable = false, length = 1)
    private String programReenterFlag;

    @Column(name = "user_type", nullable = false, length = 1)
    private String userType;

    @Column(name = "from_transaction_id", nullable = false, length = 4)
    private String fromTransactionId;

    @Column(name = "user_id", nullable = false, length = 8)
    private String userId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserSession(String transactionId, String programName, String toProgram, 
                      String programReenterFlag, String userType, String fromTransactionId, 
                      String userId, Boolean reenterFlag) {
        this.transactionId = transactionId;
        this.programName = programName;
        this.toProgram = toProgram;
        this.programReenterFlag = programReenterFlag;
        this.userType = userType;
        this.fromTransactionId = fromTransactionId;
        this.userId = userId;
        this.reenterFlag = reenterFlag;
    }

    @PrePersist
    @PreUpdate
    public void validateUserSession() {
        validateUserType();
        validateUserId();
    }

    private void validateUserType() {
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalStateException("User Type cannot be empty or contain only low-values");
        }
    }

    private void validateUserId() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalStateException("User ID cannot be empty or spaces");
        }
    }

    public boolean isReentering() {
        return Boolean.TRUE.equals(reenterFlag);
    }

    public boolean isProgramReentering() {
        return "Y".equalsIgnoreCase(programReenterFlag) || "1".equals(programReenterFlag);
    }

    public boolean hasFromProgram() {
        return fromProgram != null && !fromProgram.trim().isEmpty();
    }

    public boolean hasFromTransaction() {
        return fromTransaction != null && !fromTransaction.trim().isEmpty();
    }

    public boolean hasProgramContext() {
        return programContext != null && programContext > 0;
    }

    public String getSessionIdentifier() {
        return userId + "-" + transactionId + "-" + programName;
    }

    public boolean isAdminUser() {
        return "A".equalsIgnoreCase(userType);
    }

    public boolean isRegularUser() {
        return "R".equalsIgnoreCase(userType) || "U".equalsIgnoreCase(userType);
    }

    public void transferToProgram(String targetProgram, String targetTransaction) {
        this.fromProgram = this.programName;
        this.fromTransaction = this.transactionId;
        this.toProgram = targetProgram;
        this.transactionId = targetTransaction;
    }

    public void markAsReentered() {
        this.reenterFlag = true;
        this.programReenterFlag = "Y";
    }

    public void resetReenterFlags() {
        this.reenterFlag = false;
        this.programReenterFlag = "N";
    }
}
