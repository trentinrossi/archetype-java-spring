package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOption {

    @Id
    @Column(name = "option_number", nullable = false, length = 2)
    private Integer optionNumber;

    @Column(name = "option_name", nullable = false, length = 40)
    private String optionName;

    @Column(name = "program_name", nullable = false, length = 8)
    private String programName;

    @Column(name = "required_user_type", nullable = false, length = 1)
    private String requiredUserType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
