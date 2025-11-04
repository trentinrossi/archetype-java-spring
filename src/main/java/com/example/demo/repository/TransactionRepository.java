package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    Page<Transaction> findByTransactionIdGreaterThanEqual(String transactionId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId ORDER BY t.transactionDate DESC")
    Page<Transaction> findByUserId(@Param("userId") String userId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId >= :startTransactionId ORDER BY t.transactionId")
    Page<Transaction> findTransactionsStartingFrom(@Param("startTransactionId") String startTransactionId, Pageable pageable);
}
