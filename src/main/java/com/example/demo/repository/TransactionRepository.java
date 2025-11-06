package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Transaction.TransactionId> {
    
    List<Transaction> findByCardNumber(String cardNumber);
    
    Page<Transaction> findByCardNumber(String cardNumber, Pageable pageable);
    
    Optional<Transaction> findByCardNumberAndTransactionId(String cardNumber, String transactionId);
    
    boolean existsByCardNumberAndTransactionId(String cardNumber, String transactionId);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber ORDER BY t.transactionId ASC")
    List<Transaction> findByCardNumberOrderByTransactionIdAsc(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT t FROM Transaction t ORDER BY t.cardNumber ASC, t.transactionId ASC")
    List<Transaction> findAllOrderByCompositeKey();
    
    @Query("SELECT t FROM Transaction t ORDER BY t.cardNumber ASC, t.transactionId ASC")
    Page<Transaction> findAllOrderByCompositeKey(Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber = :cardNumber")
    long countByCardNumber(@Param("cardNumber") String cardNumber);
}