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
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    Optional<Transaction> findTopByOrderByTransactionIdDesc();
    
    List<Transaction> findByCardNumber(String cardNumber);
    
    List<Transaction> findByAccountId(String accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.processingTimestamp >= :startDate AND t.processingTimestamp <= :endDate ORDER BY t.processingTimestamp DESC")
    List<Transaction> findByProcessingTimestampBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.processingTimestamp >= :startDate AND t.processingTimestamp <= :endDate ORDER BY t.accountId, t.processingTimestamp")
    Page<Transaction> findByProcessingTimestampBetweenOrderByAccountId(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId AND t.transactionTypeCode = :typeCode AND t.transactionCategoryCode = :categoryCode")
    List<Transaction> findByAccountIdAndTypeAndCategory(@Param("accountId") String accountId, @Param("typeCode") String typeCode, @Param("categoryCode") Integer categoryCode);
}
