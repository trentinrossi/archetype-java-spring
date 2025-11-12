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

    @Column(name = "page_number", nullable = false, length = 8)
    private Integer pageNumber;

    @Column(name = "first_user_id", length = 8)
    private String firstUserId;

    @Column(name = "last_user_id", length = 8)
    private String lastUserId;

    @Column(name = "has_next_page", nullable = false, length = 1)
    private Boolean hasNextPage;

    @Column(name = "records_per_page", nullable = false, length = 2)
    private Integer recordsPerPage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserListPage() {
        this.recordsPerPage = 10;
    }
}
