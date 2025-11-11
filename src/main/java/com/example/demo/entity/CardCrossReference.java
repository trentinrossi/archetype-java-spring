package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_cross_reference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CardCrossReference.CardCrossReferenceId.class)
public class CardCrossReference {

    @Id
    @Column(name = "xref_acct_id", nullable = false, length = 11)
    private String accountId;

    @Id
    @Column(name = "xref_card_num", nullable = false, length = 16)
    private String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xref_acct_id", referencedColumnName = "acct_id", insertable = false, updatable = false)
    private Account account;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public CardCrossReference(String accountId, String cardNumber) {
        this.accountId = accountId;
        this.cardNumber = cardNumber;
    }

    @PrePersist
    @PreUpdate
    protected void validateCardCrossReference() {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalStateException("Account ID cannot be empty");
        }
        
        if (accountId.length() > 11) {
            throw new IllegalStateException("Account ID cannot exceed 11 characters");
        }
        
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalStateException("Card number cannot be empty");
        }
        
        if (cardNumber.length() > 16) {
            throw new IllegalStateException("Card number cannot exceed 16 characters");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardCrossReferenceId implements Serializable {
        private String accountId;
        private String cardNumber;
    }
}
