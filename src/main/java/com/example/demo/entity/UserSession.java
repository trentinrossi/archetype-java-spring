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
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public UserSession(String transactionId, String programName, Boolean reenterFlag) {
        this.transactionId = transactionId;
        this.programName = programName;
        this.reenterFlag = reenterFlag;
    }
    
    public boolean isReentering() {
        return Boolean.TRUE.equals(this.reenterFlag);
    }
    
    public boolean hasCallingProgram() {
        return this.fromProgram != null && !this.fromProgram.trim().isEmpty();
    }
    
    public boolean hasCallingTransaction() {
        return this.fromTransaction != null && !this.fromTransaction.trim().isEmpty();
    }
    
    public boolean hasProgramContext() {
        return this.programContext != null;
    }
    
    public void setReentering(boolean reenter) {
        this.reenterFlag = reenter;
    }
    
    public void clearCallingContext() {
        this.fromProgram = null;
        this.fromTransaction = null;
        this.programContext = null;
    }
    
    @PrePersist
    protected void onCreate() {
        if (this.reenterFlag == null) {
            this.reenterFlag = false;
        }
        validateTransactionId();
        validateProgramName();
    }
    
    @PreUpdate
    protected void onUpdate() {
        validateTransactionId();
        validateProgramName();
    }
    
    private void validateTransactionId() {
        if (this.transactionId == null || this.transactionId.trim().isEmpty()) {
            throw new IllegalStateException("Transaction ID cannot be null or empty");
        }
        if (this.transactionId.length() > 4) {
            throw new IllegalStateException("Transaction ID cannot exceed 4 characters");
        }
    }
    
    private void validateProgramName() {
        if (this.programName == null || this.programName.trim().isEmpty()) {
            throw new IllegalStateException("Program name cannot be null or empty");
        }
        if (this.programName.length() > 8) {
            throw new IllegalStateException("Program name cannot exceed 8 characters");
        }
    }
}
