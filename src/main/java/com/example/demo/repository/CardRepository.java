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
    
    Optional<Card> findByCardNum(String cardNum);
    
    boolean existsByCardNum(String cardNum);
    
    List<Card> findByAcctId(Long acctId);
    
    Page<Card> findByAcctId(Long acctId, Pageable pageable);
    
    @Query("SELECT c FROM Card c WHERE c.account.acctId = :acctId")
    List<Card> findCardsByAccountId(@Param("acctId") Long acctId);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.card.cardNum = :cardNum")
    long countTransactionsByCardNumber(@Param("cardNum") String cardNum);
}
