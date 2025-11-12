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
    @Column(name = "user_selection_id")
    private Long userSelectionId;

    @Column(name = "selection_flag", length = 1)
    private String selectionFlag;

    @Column(name = "selected_user_id", length = 8)
    private String selectedUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (selectionFlag != null && !selectionFlag.equals("U") && !selectionFlag.equals("D")) {
            throw new IllegalArgumentException("Selection flag must be U (update) or D (delete)");
        }
    }

    public boolean isUpdateSelection() {
        return "U".equals(selectionFlag);
    }

    public boolean isDeleteSelection() {
        return "D".equals(selectionFlag);
    }
}
