package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.demo.enums.CardStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Maintains relationships between card numbers, customer identifiers, and account identifiers
 * Entity represents the cross-reference mapping for card-customer-account relationships
 */
@Entity
@Table(name = "card_cross_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReference {
    
    @Id
    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @Column(name = "card_type", length = 20)
    private String cardType;
    
    @Column(name = "card_holder_name", length = 100)
    private String cardHolderName;
    
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    
    @Column(name = "issue_date")
    private LocalDate issueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", nullable = false, length = 20)
    private CardStatus cardStatus = CardStatus.ACTIVE;
    
    @Column(name = "is_primary_card")
    private Boolean isPrimaryCard = false;
    
    @Column(name = "card_sequence_number")
    private Integer cardSequenceNumber;
    
    @Column(name = "embossed_name", length = 26)
    private String embossedName;
    
    @Column(name = "pin_set")
    private Boolean pinSet = false;
    
    @Column(name = "pin_set_date")
    private LocalDate pinSetDate;
    
    @Column(name = "activation_date")
    private LocalDate activationDate;
    
    @Column(name = "last_used_date")
    private LocalDate lastUsedDate;
    
    @Column(name = "replacement_card_number", length = 16)
    private String replacementCardNumber;
    
    @Column(name = "replaced_card_number", length = 16)
    private String replacedCardNumber;
    
    @Column(name = "card_production_date")
    private LocalDate cardProductionDate;
    
    @Column(name = "card_mailed_date")
    private LocalDate cardMailedDate;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Helper methods
    public boolean isExpired() {
        return expirationDate != null && LocalDate.now().isAfter(expirationDate);
    }
    
    public boolean isActive() {
        return CardStatus.ACTIVE.equals(cardStatus) && !isExpired();
    }
    
    public boolean needsReplacement() {
        if (expirationDate == null) return false;
        LocalDate threeMonthsFromNow = LocalDate.now().plusMonths(3);
        return expirationDate.isBefore(threeMonthsFromNow);
    }
    
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }
    
    public boolean canTransact() {
        return isActive() && !isExpired() && pinSet;
    }
}
