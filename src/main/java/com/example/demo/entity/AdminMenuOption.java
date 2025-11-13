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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_number", nullable = false)
    private Integer optionNumber;

    @Column(name = "option_name", nullable = false, length = 35)
    private String optionName;

    @Column(name = "program_name", nullable = false, length = 8)
    private String programName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AdminMenuOption(Integer optionNumber, String optionName, String programName, Boolean isActive) {
        this.optionNumber = optionNumber;
        this.optionName = optionName;
        this.programName = programName;
        this.isActive = isActive;
        this.displayOrder = optionNumber;
    }

    public boolean isComingSoon() {
        return programName != null && programName.startsWith("DUMMY");
    }

    public String getStatusDisplay() {
        if (!isActive) {
            return "Inactive";
        } else if (isComingSoon()) {
            return "Coming Soon";
        }
        return "Active";
    }
}
