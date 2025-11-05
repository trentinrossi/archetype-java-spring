package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    List<Transaction> findByCardNumber(String cardNumber);
    
    List<Transaction> findByProcessingTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<Transaction> findByCardNumberAndProcessingTimestampBetween(String cardNumber, LocalDateTime start, LocalDateTime end);
    
    Optional<Transaction> findTopByOrderByTransactionIdDesc();
}
