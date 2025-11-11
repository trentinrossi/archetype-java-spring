package com.carddemo.billpayment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_cross_reference", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"xref_acct_id", "xref_card_num"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "xref_acct_id", nullable = false, length = 11, insertable = false, updatable = false)
    private String xrefAcctId;
    
    @Column(name = "xref_card_num", nullable = false, length = 16)
    private String xrefCardNum;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_acct_id", referencedColumnName = "acct_id", nullable = false)
    private Account account;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public CardCrossReference(String xrefCardNum, Account account) {
        this.xrefCardNum = xrefCardNum;
        this.account = account;
        if (account != null) {
            this.xrefAcctId = account.getAcctId();
        }
    }
    
    @PrePersist
    @PreUpdate
    public void validateCardCrossReference() {
        if (this.xrefCardNum == null || this.xrefCardNum.trim().isEmpty()) {
            throw new IllegalStateException("Card number cannot be empty");
        }
        if (this.xrefCardNum.length() != 16) {
            throw new IllegalStateException("Card number must be exactly 16 characters");
        }
        if (this.account == null) {
            throw new IllegalStateException("Account cannot be null");
        }
    }
}
