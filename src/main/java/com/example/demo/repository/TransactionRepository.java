package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    List<Transaction> findByCardNumber(String cardNumber);
    
    List<Transaction> findByCardNumberOrderByOriginationTimestampDesc(String cardNumber);
    
    Optional<Transaction> findTopByOrderByTransactionIdDesc();
}
