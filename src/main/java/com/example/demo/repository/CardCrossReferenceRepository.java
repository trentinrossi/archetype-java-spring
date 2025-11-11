package com.example.demo.repository;

import com.example.demo.entity.CardCrossReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, CardCrossReference.CardCrossReferenceId> {
    
    /**
     * Find all card cross-references for a specific account
     */
    List<CardCrossReference> findByAccountId(String accountId);
    
    /**
     * Find all card cross-references for a specific card number
     */
    List<CardCrossReference> findByCardNumber(String cardNumber);
    
    /**
     * Find a specific cross-reference by account ID and card number
     */
    Optional<CardCrossReference> findByAccountIdAndCardNumber(String accountId, String cardNumber);
    
    /**
     * Check if a cross-reference exists for account and card
     */
    boolean existsByAccountIdAndCardNumber(String accountId, String cardNumber);
    
    /**
     * Check if any cross-reference exists for an account
     */
    boolean existsByAccountId(String accountId);
    
    /**
     * Check if any cross-reference exists for a card number
     */
    boolean existsByCardNumber(String cardNumber);
    
    /**
     * Count cross-references for a specific account
     */
    long countByAccountId(String accountId);
    
    /**
     * Count cross-references for a specific card number
     */
    long countByCardNumber(String cardNumber);
    
    /**
     * Delete all cross-references for a specific account
     */
    void deleteByAccountId(String accountId);
    
    /**
     * Delete all cross-references for a specific card number
     */
    void deleteByCardNumber(String cardNumber);
}
