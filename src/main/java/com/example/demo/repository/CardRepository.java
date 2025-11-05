package com.example.demo.repository;

import com.example.demo.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    
    Optional<Card> findByCardNumber(String cardNumber);
    
    List<Card> findByAccountId(String accountId);
    
    Optional<Card> findByAccountIdAndCardNumber(String accountId, String cardNumber);
    
    List<Card> findByActiveStatus(String activeStatus);
    
    Page<Card> findByActiveStatus(String activeStatus, Pageable pageable);
    
    Page<Card> findByAccountId(String accountId, Pageable pageable);
    
    Page<Card> findByCardNumber(String cardNumber, Pageable pageable);
    
    boolean existsByCardNumber(String cardNumber);
}
