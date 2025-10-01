package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @Column(name = "sec_usr_id", length = 8, nullable = false, updatable = false)
    private String secUsrId;
    
    @Column(name = "sec_usr_fname", length = 20, nullable = false)
    private String secUsrFname;
    
    @Column(name = "sec_usr_lname", length = 20, nullable = false)
    private String secUsrLname;
    
    @Column(name = "sec_usr_pwd", length = 8, nullable = false)
    private String secUsrPwd;
    
    @Column(name = "sec_usr_type", length = 1, nullable = false)
    private String secUsrType;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public User(String secUsrId, String secUsrFname, String secUsrLname, String secUsrPwd, String secUsrType) {
        this.secUsrId = secUsrId;
        this.secUsrFname = secUsrFname;
        this.secUsrLname = secUsrLname;
        this.secUsrPwd = secUsrPwd;
        this.secUsrType = secUsrType;
    }
    
    public String getFullName() {
        return secUsrFname + " " + secUsrLname;
    }
    
    public boolean isAdmin() {
        return "A".equals(secUsrType);
    }
    
    public boolean isRegularUser() {
        return "U".equals(secUsrType);
    }
    
    public String getUserTypeDisplayName() {
        return switch (secUsrType) {
            case "A" -> "Admin";
            case "U" -> "User";
            default -> "Unknown";
        };
    }
}