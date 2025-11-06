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
    
    Optional<Transaction> findByCardNumberAndTransactionId(String cardNumber, String transactionId);
    
    boolean existsByCardNumberAndTransactionId(String cardNumber, String transactionId);
    
    Page<Transaction> findByCardNumber(String cardNumber, Pageable pageable);
    
    List<Transaction> findByCardNumberOrderByTransactionIdAsc(String cardNumber);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber ORDER BY t.transactionId ASC")
    List<Transaction> findTransactionsByCardNumberSequential(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT t FROM Transaction t ORDER BY t.cardNumber ASC, t.transactionId ASC")
    List<Transaction> findAllOrderByCompositeKey();
    
    @Query("SELECT t FROM Transaction t ORDER BY t.cardNumber ASC, t.transactionId ASC")
    Page<Transaction> findAllOrderByCompositeKey(Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber = :cardNumber")
    long countByCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionId >= :startTransactionId ORDER BY t.transactionId ASC")
    List<Transaction> findByCardNumberAndTransactionIdGreaterThanEqual(
            @Param("cardNumber") String cardNumber, 
            @Param("startTransactionId") String startTransactionId);
    
    @Query("SELECT DISTINCT t.cardNumber FROM Transaction t ORDER BY t.cardNumber ASC")
    List<String> findDistinctCardNumbers();
    
    List<Transaction> findTop100ByOrderByCardNumberAscTransactionIdAsc();
    
    @Query(value = "SELECT * FROM transactions WHERE card_number = :cardNumber ORDER BY transaction_id ASC LIMIT :limit", nativeQuery = true)
    List<Transaction> findTransactionsByCardNumberWithLimit(
            @Param("cardNumber") String cardNumber, 
            @Param("limit") int limit);
}
