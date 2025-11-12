package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_list_pages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_list_page_id")
    private Long userListPageId;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @Column(name = "first_user_id", length = 8)
    private String firstUserId;

    @Column(name = "last_user_id", length = 8)
    private String lastUserId;

    @Column(name = "has_next_page", nullable = false)
    private Boolean hasNextPage = false;

    @Column(name = "records_per_page", nullable = false)
    private Integer recordsPerPage = 10;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (pageNumber == null || pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }
        if (recordsPerPage == null || recordsPerPage < 1) {
            throw new IllegalArgumentException("Records per page must be greater than 0");
        }
    }
}
