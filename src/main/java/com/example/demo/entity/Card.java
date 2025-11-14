package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards", indexes = {
    @Index(name = "idx_card_num", columnList = "xref_card_num"),
    @Index(name = "idx_card_acct_id", columnList = "xref_acct_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "xref_card_num", nullable = false, length = 16)
    private String xrefCardNum;
    
    @Column(name = "xref_acct_id", nullable = false, length = 11)
    private String xrefAcctId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_acct_id", referencedColumnName = "acct_id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_card_account"))
    private Account account;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Card(String xrefCardNum, String xrefAcctId) {
        this.xrefCardNum = xrefCardNum;
        this.xrefAcctId = xrefAcctId;
    }
    
    public boolean isValidForBillPayment() {
        return xrefCardNum != null && !xrefCardNum.isEmpty() 
            && xrefAcctId != null && !xrefAcctId.isEmpty();
    }
    
    @PrePersist
    @PreUpdate
    private void validateCard() {
        if (xrefCardNum == null || xrefCardNum.trim().isEmpty()) {
            throw new IllegalStateException("Card number is required");
        }
        if (xrefAcctId == null || xrefAcctId.trim().isEmpty()) {
            throw new IllegalStateException("Account ID is required");
        }
    }
}
