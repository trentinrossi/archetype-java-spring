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
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(name = "password", nullable = false, length = 8)
    private String password;
    
    @Column(name = "user_type", nullable = false, length = 1)
    private String userType;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public UserSecurity(String userId, String firstName, String lastName, String password, String userType) {
        this.userId = userId != null ? userId.toUpperCase() : null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = userType != null ? userType.toUpperCase() : null;
    }
    
    public void setUserId(String userId) {
        this.userId = userId != null ? userId.toUpperCase() : null;
    }
    
    public void setUserType(String userType) {
        this.userType = userType != null ? userType.toUpperCase() : null;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean isAdmin() {
        return "A".equals(userType);
    }
    
    public boolean isRegular() {
        return "R".equals(userType);
    }
    
    public boolean validatePassword(String inputPassword) {
        return password != null && password.equals(inputPassword);
    }
    
    public boolean hasValidUserType() {
        return "A".equals(userType) || "R".equals(userType);
    }
}