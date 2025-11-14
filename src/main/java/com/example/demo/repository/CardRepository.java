package com.example.demo.repository;

import com.example.demo.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    
    Optional<Card> findByCardNumber(String cardNumber);
    
    List<Card> findByAccountId(Long accountId);
    
    Page<Card> findByAccountId(Long accountId, Pageable pageable);
    
    List<Card> findByCustomerId(Long customerId);
    
    Page<Card> findByCustomerId(Long customerId, Pageable pageable);
    
    Optional<Card> findByCardNumberAndAccountId(String cardNumber, Long accountId);
    
    Optional<Card> findByCardNumberAndCustomerId(String cardNumber, Long customerId);
    
    boolean existsByCardNumber(String cardNumber);
    
    boolean existsByAccountId(Long accountId);
    
    boolean existsByCustomerId(Long customerId);
    
    @Query("SELECT COUNT(c) FROM Card c WHERE c.accountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT COUNT(c) FROM Card c WHERE c.customerId = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT c FROM Card c WHERE c.accountId = :accountId AND c.customerId = :customerId")
    List<Card> findByAccountIdAndCustomerId(@Param("accountId") Long accountId, @Param("customerId") Long customerId);
    
    @Query("SELECT c FROM Card c WHERE c.cardNumber IN :cardNumbers")
    List<Card> findByCardNumberIn(@Param("cardNumbers") List<String> cardNumbers);
    
    @Query("SELECT c FROM Card c WHERE c.accountId IN :accountIds")
    List<Card> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT c FROM Card c WHERE c.customerId IN :customerIds")
    List<Card> findByCustomerIdIn(@Param("customerIds") List<Long> customerIds);
}
