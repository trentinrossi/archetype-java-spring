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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id", nullable = false)
    private AdminUser adminUser;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AdminMenuOption(Integer optionNumber, String optionName, String programName, Boolean isActive, AdminUser adminUser) {
        this.optionNumber = optionNumber;
        this.optionName = optionName;
        this.programName = programName;
        this.isActive = isActive;
        this.adminUser = adminUser;
    }

    public boolean isAvailable() {
        return this.isActive != null && this.isActive;
    }

    public String getDisplayText() {
        return String.format("%02d - %s", this.optionNumber, this.optionName);
    }

    public String getStatusLabel() {
        return isAvailable() ? "Active" : "Coming Soon";
    }

    @PrePersist
    @PreUpdate
    private void validateFields() {
        if (this.optionNumber != null && (this.optionNumber < 0 || this.optionNumber > 99)) {
            throw new IllegalArgumentException("Option number must be between 0 and 99");
        }
        if (this.optionName != null && this.optionName.length() > 35) {
            throw new IllegalArgumentException("Option name cannot exceed 35 characters");
        }
        if (this.programName != null && this.programName.length() > 8) {
            throw new IllegalArgumentException("Program name cannot exceed 8 characters");
        }
    }
}
