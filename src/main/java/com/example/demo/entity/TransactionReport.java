package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "report_type", nullable = false, length = 10)
    private String reportType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "confirmation_flag", nullable = false, length = 1)
    private String confirmationFlag;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validate() {
        if (reportType == null || reportType.trim().isEmpty()) {
            throw new IllegalArgumentException("Report type is required");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date is required");
        }
        if (confirmationFlag == null || (!confirmationFlag.equals("Y") && !confirmationFlag.equals("N"))) {
            throw new IllegalArgumentException("Confirmation flag must be Y or N");
        }
    }

    public boolean isConfirmed() {
        return "Y".equals(confirmationFlag);
    }
}
