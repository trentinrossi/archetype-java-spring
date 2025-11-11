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
    
    boolean existsByCardNumber(String cardNumber);
    
    List<Card> findByCustomer_CustomerId(Long customerId);
    
    Page<Card> findByCustomer_CustomerId(Long customerId, Pageable pageable);
    
    List<Card> findByAccount_AccountId(Long accountId);
    
    Page<Card> findByAccount_AccountId(Long accountId, Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.customer.customerId = :customerId AND c.account.accountId = :accountId")
    List<Card> findByCustomerIdAndAccountId(@Param("customerId") Long customerId, 
                                             @Param("accountId") Long accountId);
    
    @Query("SELECT c FROM Card c WHERE SIZE(c.transactions) >= :minCount")
    List<Card> findCardsWithMinimumTransactions(@Param("minCount") int minCount);
    
    @Query("SELECT c FROM Card c WHERE SIZE(c.transactions) < 10")
    List<Card> findCardsWithAvailableCapacity();
    
    @Query("SELECT c FROM Card c WHERE SIZE(c.transactions) = 10")
    List<Card> findCardsAtMaxCapacity();
    
    @Query("SELECT c FROM Card c WHERE c.cardNumber IN :cardNumbers")
    List<Card> findByCardNumberIn(@Param("cardNumbers") List<String> cardNumbers);
    
    long countByCustomer_CustomerId(Long customerId);
    
    long countByAccount_AccountId(Long accountId);
    
    @Query("SELECT COUNT(c) FROM Card c WHERE SIZE(c.transactions) = 10")
    long countCardsAtMaxCapacity();
    
    @Query("SELECT COUNT(c) FROM Card c WHERE SIZE(c.transactions) < 10")
    long countCardsWithAvailableCapacity();
}
