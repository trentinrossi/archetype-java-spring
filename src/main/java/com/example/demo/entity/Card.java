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
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    
    @Id
    @Column(name = "card_number", length = 16, nullable = false)
    private String cardNumber;
    
    @Column(name = "account_id", length = 11, nullable = false)
    private String accountId;
    
    @Column(name = "cvv_code", length = 3, nullable = false)
    private String cvvCode;
    
    @Column(name = "embossed_name", length = 50, nullable = false)
    private String embossedName;
    
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;
    
    @Column(name = "active_status", length = 1, nullable = false)
    private String activeStatus;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Card(String cardNumber, String accountId, String cvvCode, String embossedName, LocalDate expirationDate, String activeStatus) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.cvvCode = cvvCode;
        this.embossedName = embossedName;
        this.expirationDate = expirationDate;
        this.activeStatus = activeStatus;
    }
    
    public boolean isActive() {
        return "Y".equalsIgnoreCase(activeStatus);
    }
    
    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }
}
