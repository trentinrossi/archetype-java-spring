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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_number", nullable = false)
    private Integer optionNumber;

    @Column(name = "option_name", nullable = false, length = 40)
    private String optionName;

    @Column(name = "program_name", nullable = false, length = 8)
    private String programName;

    @Column(name = "user_type_required", nullable = false, length = 1)
    private String userTypeRequired;

    @Column(name = "option_count", nullable = false)
    private Integer optionCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public MenuOption(Integer optionNumber, String optionName, String programName, String userTypeRequired, Integer optionCount) {
        this.optionNumber = optionNumber;
        this.optionName = optionName;
        this.programName = programName;
        this.userTypeRequired = userTypeRequired;
        this.optionCount = optionCount;
    }

    public boolean isAdminOnly() {
        return "A".equalsIgnoreCase(userTypeRequired);
    }

    public boolean isUserAccessible() {
        return "U".equalsIgnoreCase(userTypeRequired) || "R".equalsIgnoreCase(userTypeRequired);
    }

    public boolean canUserAccess(String userType) {
        if (userType == null) return false;
        if ("A".equalsIgnoreCase(userTypeRequired)) {
            return "A".equalsIgnoreCase(userType);
        }
        return true;
    }

    public String getDisplayText() {
        return String.format("%02d - %s", optionNumber, optionName);
    }

    @PrePersist
    @PreUpdate
    private void validateMenuOption() {
        if (optionNumber != null && (optionNumber < 0 || optionNumber > 99)) {
            throw new IllegalArgumentException("Option number must be between 0 and 99");
        }
        
        if (optionName != null && optionName.length() > 40) {
            throw new IllegalArgumentException("Option name cannot exceed 40 characters");
        }
        
        if (programName != null && programName.length() > 8) {
            throw new IllegalArgumentException("Program name cannot exceed 8 characters");
        }

        if (userTypeRequired != null && userTypeRequired.length() != 1) {
            throw new IllegalArgumentException("User type required must be exactly 1 character");
        }
    }
}
