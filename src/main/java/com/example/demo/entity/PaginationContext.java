package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagination_contexts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagination_context_id")
    private Long paginationContextId;

    @Column(name = "screen_number", nullable = false)
    private Integer screenNumber;

    @Column(name = "first_card_key", nullable = false, length = 27)
    private String firstCardKey;

    @Column(name = "last_card_key", nullable = false, length = 27)
    private String lastCardKey;

    @Column(name = "next_page_exists", nullable = false)
    private Boolean nextPageExists;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (screenNumber == null || screenNumber < 1) {
            throw new IllegalArgumentException("Screen number must be greater than 0");
        }
    }

    public boolean hasNextPage() {
        return nextPageExists != null && nextPageExists;
    }
}
