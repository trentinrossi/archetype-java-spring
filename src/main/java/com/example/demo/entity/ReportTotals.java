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
@Table(name = "report_totals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTotals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_total", nullable = false, precision = 13, scale = 2)
    private BigDecimal pageTotal;

    @Column(name = "account_total", nullable = false, precision = 13, scale = 2)
    private BigDecimal accountTotal;

    @Column(name = "grand_total", nullable = false, precision = 13, scale = 2)
    private BigDecimal grandTotal;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ReportTotals() {
        this.pageTotal = BigDecimal.ZERO;
        this.accountTotal = BigDecimal.ZERO;
        this.grandTotal = BigDecimal.ZERO;
    }

    public void addToPageTotal(BigDecimal amount) {
        this.pageTotal = this.pageTotal.add(amount);
    }

    public void addToAccountTotal(BigDecimal amount) {
        this.accountTotal = this.accountTotal.add(amount);
    }

    public void addToGrandTotal(BigDecimal amount) {
        this.grandTotal = this.grandTotal.add(amount);
    }

    public void resetPageTotal() {
        this.pageTotal = BigDecimal.ZERO;
    }

    public void resetAccountTotal() {
        this.accountTotal = BigDecimal.ZERO;
    }
}
