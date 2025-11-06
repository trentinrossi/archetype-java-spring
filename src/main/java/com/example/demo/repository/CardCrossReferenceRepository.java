package com.example.demo.repository;

import com.example.demo.entity.CardCrossReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, String> {
    
    Optional<CardCrossReference> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    List<CardCrossReference> findByCustomer_CustomerId(Long customerId);
    
    List<CardCrossReference> findByAccount_AccountId(Long accountId);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber LIKE :cardNumberPattern%")
    Page<CardCrossReference> findByCardNumberStartingWith(@Param("cardNumberPattern") String cardNumberPattern, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.customer.customerId = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.account.accountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT c FROM CardCrossReference c WHERE LENGTH(c.cardNumber) = 16")
    Page<CardCrossReference> findAllValidCardNumbers(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE LENGTH(c.cardNumber) <> 16")
    List<CardCrossReference> findInvalidCardNumbers();
    
    @Query("SELECT c FROM CardCrossReference c ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findAllOrderedByCardNumber();
    
    void deleteByCardNumber(String cardNumber);
}
