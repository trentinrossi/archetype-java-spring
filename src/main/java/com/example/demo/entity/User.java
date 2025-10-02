package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "usrsec")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @Column(name = "user_id", length = 8, nullable = false)
    private String userId;
    
    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;
    
    @Column(name = "last_name", length = 20, nullable = false)
    private String lastName;
    
    @Column(name = "password", length = 8, nullable = false)
    private String password;
    
    @Column(name = "user_type", length = 1, nullable = true)
    private String userType;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public User(String userId, String firstName, String lastName, String password, String userType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = userType;
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
}