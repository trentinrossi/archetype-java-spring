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
    private Long id;

    @Column(name = "page_number", nullable = false)
    private Long pageNumber;

    @Column(name = "first_user_id", length = 8)
    private String firstUserId;

    @Column(name = "last_user_id", length = 8)
    private String lastUserId;

    @Column(name = "has_next_page", nullable = false)
    private Boolean hasNextPage;

    @Column(name = "records_per_page", nullable = false)
    private Integer recordsPerPage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserListPage(Long pageNumber, String firstUserId, String lastUserId, Boolean hasNextPage, Integer recordsPerPage) {
        this.pageNumber = pageNumber;
        this.firstUserId = firstUserId;
        this.lastUserId = lastUserId;
        this.hasNextPage = hasNextPage;
        this.recordsPerPage = recordsPerPage;
    }

    @PrePersist
    @PreUpdate
    private void validateUserListPage() {
        if (pageNumber != null && pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 1");
        }

        if (recordsPerPage == null) {
            this.recordsPerPage = 10;
        }
    }

    public boolean hasPreviousPage() {
        return pageNumber != null && pageNumber > 1;
    }

    public Long getNextPageNumber() {
        return hasNextPage && pageNumber != null ? pageNumber + 1 : null;
    }

    public Long getPreviousPageNumber() {
        return hasPreviousPage() ? pageNumber - 1 : null;
    }

    public boolean isEmpty() {
        return firstUserId == null && lastUserId == null;
    }

    public int getRecordCount() {
        if (isEmpty()) return 0;
        return recordsPerPage != null ? recordsPerPage : 10;
    }
}
