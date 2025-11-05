package com.example.demo.repository;

import com.example.demo.entity.CardCrossReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, Long> {
    
    List<CardCrossReference> findByAccountId(String accountId);
    
    List<CardCrossReference> findByCardNumber(String cardNumber);
    
    List<CardCrossReference> findByCustomerId(String customerId);
    
    Optional<CardCrossReference> findByAccountIdAndCardNumber(String accountId, String cardNumber);
}
