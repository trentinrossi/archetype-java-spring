package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    boolean existsByTransactionId(String transactionId);
    
    List<Transaction> findByCard_CardNumber(String cardNumber);
    
    Page<Transaction> findByCard_CardNumber(String cardNumber, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.card.cardNumber = :cardNumber ORDER BY t.createdAt DESC")
    List<Transaction> findByCardNumberOrderByCreatedAtDesc(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT t FROM Transaction t WHERE t.card.account.accountId = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.card.account.accountId = :accountId")
    Page<Transaction> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.card.customer.customerId = :customerId")
    List<Transaction> findByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionTypeCode = :typeCode")
    List<Transaction> findByTransactionTypeCode(@Param("typeCode") String typeCode);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionCategoryCode = :categoryCode")
    List<Transaction> findByTransactionCategoryCode(@Param("categoryCode") String categoryCode);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionAmount > 0")
    List<Transaction> findCreditTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionAmount < 0")
    List<Transaction> findDebitTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionAmount BETWEEN :minAmount AND :maxAmount")
    List<Transaction> findByAmountRange(@Param("minAmount") BigDecimal minAmount, 
                                        @Param("maxAmount") BigDecimal maxAmount);
    
    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.card.cardNumber = :cardNumber")
    BigDecimal sumTransactionAmountsByCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.card.account.accountId = :accountId")
    BigDecimal sumTransactionAmountsByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.card.cardNumber = :cardNumber")
    long countByCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.card.account.accountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);
}
