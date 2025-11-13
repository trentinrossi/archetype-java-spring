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

    public MenuOption(Integer optionNumber, String optionName, String programName, 
                     String userTypeRequired, Integer optionCount) {
        this.optionNumber = optionNumber;
        this.optionName = optionName;
        this.programName = programName;
        this.userTypeRequired = userTypeRequired;
        this.optionCount = optionCount;
        this.isActive = true;
        this.displayOrder = optionNumber;
    }

    public boolean isAdminOnly() {
        return "A".equalsIgnoreCase(userTypeRequired);
    }

    public boolean isUserAccessible() {
        return "U".equalsIgnoreCase(userTypeRequired) || "R".equalsIgnoreCase(userTypeRequired);
    }

    public boolean isAccessibleByUserType(String userType) {
        if ("A".equalsIgnoreCase(userType)) {
            return true; // Admin can access all options
        }
        return !isAdminOnly(); // Regular users can only access non-admin options
    }

    public boolean isComingSoon() {
        return programName != null && programName.startsWith("DUMMY");
    }

    public boolean isValidOption() {
        return optionNumber != null && optionNumber > 0 && optionNumber <= optionCount;
    }

    public String getAccessLevelDisplay() {
        if (isAdminOnly()) {
            return "Admin Only";
        } else if (isUserAccessible()) {
            return "All Users";
        }
        return "Unknown";
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
