package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "disclosure_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisclosureGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dis_acct_group_id", nullable = false, length = 10)
    private String disAcctGroupId;

    @Column(name = "dis_tran_cat_cd", nullable = false, length = 4)
    private Integer disTranCatCd;

    @Column(name = "dis_tran_type_cd", nullable = false, length = 2)
    private String disTranTypeCd;

    @Column(name = "dis_int_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal disIntRate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateDisclosureGroup() {
        if (disIntRate == null || disIntRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate must be numeric and greater than or equal to zero");
        }
    }
}
