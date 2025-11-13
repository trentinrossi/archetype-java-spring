package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_selections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "selection_flag", nullable = false, length = 1)
    private String selectionFlag;

    @Column(name = "selected_user_id", nullable = false, length = 8)
    private String selectedUserId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserSelection(String selectionFlag, String selectedUserId) {
        this.selectionFlag = selectionFlag;
        this.selectedUserId = selectedUserId;
    }

    @PrePersist
    @PreUpdate
    private void validateUserSelection() {
        if (selectionFlag == null || selectionFlag.trim().isEmpty()) {
            throw new IllegalArgumentException("Selection flag is required");
        }

        if (selectionFlag.length() != 1) {
            throw new IllegalArgumentException("Selection flag must be exactly 1 character");
        }

        if (!selectionFlag.matches("[UD]")) {
            throw new IllegalArgumentException("Selection flag must be U (update) or D (delete)");
        }

        if (selectedUserId == null || selectedUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("Selected user ID is required");
        }

        if (selectedUserId.length() > 8) {
            throw new IllegalArgumentException("Selected user ID cannot exceed 8 characters");
        }
    }

    public boolean isUpdateSelection() {
        return "U".equalsIgnoreCase(selectionFlag);
    }

    public boolean isDeleteSelection() {
        return "D".equalsIgnoreCase(selectionFlag);
    }

    public String getSelectionType() {
        if (isUpdateSelection()) return "UPDATE";
        if (isDeleteSelection()) return "DELETE";
        return "UNKNOWN";
    }
}
