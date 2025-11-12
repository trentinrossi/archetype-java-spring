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
    
    List<Card> findByAccountId(Long accountId);
    
    List<Card> findByCustomerId(Long customerId);
    
    boolean existsByAccountId(Long accountId);
    
    boolean existsByCustomerId(Long customerId);
    
    long countByAccountId(Long accountId);
    
    long countByCustomerId(Long customerId);
    
    @Query("SELECT c FROM Card c WHERE c.cardNumber = :cardNumber AND c.accountId = :accountId")
    Optional<Card> findByCardNumberAndAccountId(@Param("cardNumber") String cardNumber, @Param("accountId") Long accountId);
    
    @Query("SELECT c FROM Card c WHERE c.accountId = :accountId ORDER BY c.cardNumber ASC")
    List<Card> findAllByAccountIdOrderByCardNumber(@Param("accountId") Long accountId);
    
    @Query("SELECT c FROM Card c WHERE c.customerId = :customerId ORDER BY c.cardNumber ASC")
    List<Card> findAllByCustomerIdOrderByCardNumber(@Param("customerId") Long customerId);
    
    @Query("SELECT c FROM Card c WHERE c.expirationDate < :currentDate")
    List<Card> findExpiredCards(@Param("currentDate") String currentDate);
    
    @Query("SELECT c FROM Card c WHERE c.expirationDate >= :currentDate")
    List<Card> findActiveCards(@Param("currentDate") String currentDate);
    
    @Query("SELECT c FROM Card c WHERE c.accountId IN :accountIds")
    List<Card> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT c FROM Card c ORDER BY c.cardNumber ASC")
    Page<Card> findAllOrderByCardNumber(Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Card c WHERE c.expirationDate < :currentDate")
    long countExpiredCards(@Param("currentDate") String currentDate);
    
    @Query("SELECT c FROM Card c WHERE c.xrefCardNum = :xrefCardNum")
    Optional<Card> findByXrefCardNum(@Param("xrefCardNum") String xrefCardNum);
    
    boolean existsByXrefCardNum(String xrefCardNum);
}
