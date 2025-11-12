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
@Table(name = "job_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jcl_content", nullable = false, length = 80)
    private String jclContent;

    @Column(name = "parameter_start_date", nullable = false, length = 10)
    private LocalDate parameterStartDate;

    @Column(name = "parameter_end_date", nullable = false, length = 10)
    private LocalDate parameterEndDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
