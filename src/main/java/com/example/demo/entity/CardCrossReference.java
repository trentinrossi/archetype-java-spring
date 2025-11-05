package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_cross_references", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"card_number", "account_id", "customer_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardCrossReference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;
    
    @Column(name = "account_id", nullable = false, length = 11)
    private String accountId;
    
    @Column(name = "customer_id", nullable = false, length = 9)
    private String customerId;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public CardCrossReference(String cardNumber, String accountId, String customerId) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.customerId = customerId;
    }
}
