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
    @Column(name = "sec_usr_id", length = 8, nullable = false)
    private String id;
    
    @Column(name = "sec_usr_fname", length = 20, nullable = false)
    private String firstName;
    
    @Column(name = "sec_usr_lname", length = 20, nullable = false)
    private String lastName;
    
    @Column(name = "sec_usr_pwd", length = 8, nullable = false)
    private String password;
    
    @Column(name = "sec_usr_type", length = 1, nullable = false)
    private String userType;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public User(String id, String firstName, String lastName, String password, String userType) {
        this.id = id;
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
    
    public boolean isRegularUser() {
        return "U".equals(userType);
    }
}