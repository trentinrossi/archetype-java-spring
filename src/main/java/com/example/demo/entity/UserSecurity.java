package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_security")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurity {
    
    @Id
    @Column(name = "user_id", length = 8, nullable = false)
    private String userId;
    
    @Column(name = "password", length = 8, nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;
    
    @Column(name = "program_name", length = 8, nullable = false)
    private String programName = "COSGN00C";
    
    @Column(name = "transaction_id", length = 4, nullable = false)
    private String transactionId = "CC00";
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public UserSecurity(String userId, String password, UserType userType) {
        this.userId = userId;
        this.password = password;
        this.userType = userType;
        this.programName = "COSGN00C";
        this.transactionId = "CC00";
        this.active = true;
    }
    
    public boolean isAdmin() {
        return UserType.ADMIN.equals(this.userType);
    }
    
    public boolean isGeneral() {
        return UserType.GENERAL.equals(this.userType);
    }
    
    public String getRedirectProgram() {
        return isAdmin() ? "COADM01C" : "COMEN01C";
    }
    
    public boolean canAuthenticate() {
        return this.active != null && this.active;
    }
    
    public enum UserType {
        ADMIN("Admin"),
        GENERAL("General");
        
        private final String displayName;
        
        UserType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}