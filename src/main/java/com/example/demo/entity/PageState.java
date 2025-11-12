package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "page_states")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_transaction_id", length = 16)
    private String firstTransactionId;

    @Column(name = "last_transaction_id", length = 16)
    private String lastTransactionId;

    @Column(name = "page_number", nullable = false, length = 8)
    private Integer pageNumber;

    @Column(name = "next_page_flag", nullable = false, length = 1)
    private String nextPageFlag;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validatePageState() {
        if (nextPageFlag == null || (!nextPageFlag.equals("Y") && !nextPageFlag.equals("N"))) {
            throw new IllegalArgumentException("Next page flag must be Y or N");
        }
    }

    public boolean hasNextPage() {
        return "Y".equals(nextPageFlag);
    }
}
