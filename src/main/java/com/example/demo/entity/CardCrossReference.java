package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_cross_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReference {

    @Id
    @Column(name = "xref_card_num", nullable = false, length = 16)
    private String xrefCardNum;

    @Column(name = "xref_cust_num", nullable = false, length = 9)
    private Long xrefCustNum;

    @Column(name = "xref_acct_id", nullable = false, length = 11)
    private Long xrefAcctId;

    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;

    @Column(name = "customer_id", nullable = false, length = 9)
    private Long customerId;

    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "cross_reference_data", nullable = false, length = 34)
    private String crossReferenceData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void validateCrossReference() {
        if (cardNumber == null || !cardNumber.matches("^[0-9]{16}$")) {
            throw new IllegalArgumentException("Card number must be 16 digits");
        }
        if (accountId == null || accountId == 0) {
            throw new IllegalArgumentException("Account Filter must be a non-zero 11 digit number");
        }
        if (customerId == null || customerId == 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        if (xrefCardNum == null) {
            xrefCardNum = cardNumber;
        }
        if (xrefAcctId == null) {
            xrefAcctId = accountId;
        }
        if (xrefCustNum == null) {
            xrefCustNum = customerId;
        }
    }
}
