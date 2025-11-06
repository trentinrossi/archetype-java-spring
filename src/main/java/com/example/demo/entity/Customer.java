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
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @Column(name = "customer_id", nullable = false, length = 9)
    private Long customerId;
    
    @Column(name = "customer_data", nullable = false, length = 491)
    private String customerData;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CardCrossReference> cardCrossReferences = new ArrayList<>();
    
    public Customer(Long customerId, String customerData) {
        this.customerId = customerId;
        this.customerData = customerData;
    }
    
    public boolean isValidCustomerId() {
        if (customerId == null) {
            return false;
        }
        String customerIdStr = String.valueOf(customerId);
        return customerIdStr.matches("\\d{9}");
    }
    
    public void addAccount(Account account) {
        accounts.add(account);
        account.setCustomer(this);
    }
    
    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setCustomer(null);
    }
    
    public void addCardCrossReference(CardCrossReference cardCrossReference) {
        cardCrossReferences.add(cardCrossReference);
        cardCrossReference.setCustomer(this);
    }
    
    public void removeCardCrossReference(CardCrossReference cardCrossReference) {
        cardCrossReferences.remove(cardCrossReference);
        cardCrossReference.setCustomer(null);
    }
}
