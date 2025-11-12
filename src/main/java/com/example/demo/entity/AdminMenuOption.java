package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_menu_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenuOption {

    @Id
    @Column(name = "option_number", nullable = false, length = 2)
    private Integer optionNumber;

    @Column(name = "option_name", nullable = false, length = 35)
    private String optionName;

    @Column(name = "program_name", nullable = false, length = 8)
    private String programName;

    @Column(name = "is_active", nullable = false, length = 1)
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
