package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @Column(name = "account_id", nullable = false, length = 11)
    private Long accountId;
    
    @Column(name = "account_data", nullable = false, length = 289)
    private String accountData;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardCrossReference> cardCrossReferences = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public Account(Long accountId, String accountData, Customer customer) {
        this.accountId = accountId;
        this.accountData = accountData;
        this.customer = customer;
    }
    
    public void addCardCrossReference(CardCrossReference cardCrossReference) {
        cardCrossReferences.add(cardCrossReference);
        cardCrossReference.setAccount(this);
    }
    
    public void removeCardCrossReference(CardCrossReference cardCrossReference) {
        cardCrossReferences.remove(cardCrossReference);
        cardCrossReference.setAccount(null);
    }
    
    public boolean isValidAccountId() {
        if (accountId == null) {
            return false;
        }
        String accountIdStr = String.valueOf(accountId);
        return accountIdStr.matches("\\d{11}");
    }
}
