package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Transaction entity
 * Provides data access methods for transaction operations
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Transaction.TransactionId> {
    
    /**
     * Find transactions by card number
     */
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber ORDER BY t.transactionDate DESC, t.transactionTime DESC")
    Page<Transaction> findByCardNumber(@Param("cardNumber") String cardNumber, Pageable pageable);
    
    /**
     * Find transactions by card number and date range
     */
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY t.transactionDate DESC, t.transactionTime DESC")
    List<Transaction> findByCardNumberAndDateRange(@Param("cardNumber") String cardNumber,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
    
    /**
     * Find transactions by status
     */
    Page<Transaction> findByTransactionStatus(TransactionStatus status, Pageable pageable);
    
    /**
     * Find transactions by transaction type
     */
    Page<Transaction> findByTransactionType(String transactionType, Pageable pageable);
    
    /**
     * Find transactions by card number and status
     */
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionStatus = :status " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByCardNumberAndStatus(@Param("cardNumber") String cardNumber,
                                                 @Param("status") TransactionStatus status);
    
    /**
     * Find pending transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionStatus = 'PENDING' " +
           "ORDER BY t.transactionDate DESC, t.transactionTime DESC")
    Page<Transaction> findPendingTransactions(Pageable pageable);
    
    /**
     * Find disputed transactions
     */
    Page<Transaction> findByIsDisputedTrue(Pageable pageable);
    
    /**
     * Find reversed transactions
     */
    Page<Transaction> findByIsReversedTrue(Pageable pageable);
    
    /**
     * Find international transactions
     */
    Page<Transaction> findByIsInternationalTrue(Pageable pageable);
    
    /**
     * Find recurring transactions
     */
    Page<Transaction> findByIsRecurringTrue(Pageable pageable);
    
    /**
     * Find transactions by merchant name
     */
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.merchantName) LIKE LOWER(CONCAT('%', :merchantName, '%'))")
    Page<Transaction> findByMerchantNameContaining(@Param("merchantName") String merchantName, Pageable pageable);
    
    /**
     * Find transactions by merchant category
     */
    Page<Transaction> findByMerchantCategory(String merchantCategory, Pageable pageable);
    
    /**
     * Find transactions by amount greater than
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionAmount > :amount ORDER BY t.transactionAmount DESC")
    Page<Transaction> findByAmountGreaterThan(@Param("amount") BigDecimal amount, Pageable pageable);
    
    /**
     * Find transactions by amount range
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionAmount BETWEEN :minAmount AND :maxAmount " +
           "ORDER BY t.transactionDate DESC")
    Page<Transaction> findByAmountRange(@Param("minAmount") BigDecimal minAmount,
                                        @Param("maxAmount") BigDecimal maxAmount,
                                        Pageable pageable);
    
    /**
     * Find transactions by date range
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY t.transactionDate DESC, t.transactionTime DESC")
    Page<Transaction> findByDateRange(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate,
                                      Pageable pageable);
    
    /**
     * Find transactions by billing cycle
     */
    List<Transaction> findByBillingCycle(String billingCycle);
    
    /**
     * Find transactions by statement date
     */
    List<Transaction> findByStatementDate(LocalDate statementDate);
    
    /**
     * Count transactions by card number
     */
    long countByCardNumber(String cardNumber);
    
    /**
     * Count transactions by status
     */
    long countByTransactionStatus(TransactionStatus status);
    
    /**
     * Count disputed transactions
     */
    long countByIsDisputedTrue();
    
    /**
     * Calculate sum of transaction amounts by card number
     */
    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.cardNumber = :cardNumber " +
           "AND t.transactionStatus = 'POSTED'")
    BigDecimal sumPostedAmountByCardNumber(@Param("cardNumber") String cardNumber);
    
    /**
     * Calculate sum of transaction amounts by card number and date range
     */
    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.cardNumber = :cardNumber " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "AND t.transactionStatus = 'POSTED'")
    BigDecimal sumPostedAmountByCardNumberAndDateRange(@Param("cardNumber") String cardNumber,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);
    
    /**
     * Calculate sum of transaction amounts by type
     */
    @Query("SELECT SUM(t.transactionAmount) FROM Transaction t WHERE t.transactionType = :type " +
           "AND t.transactionStatus = 'POSTED'")
    BigDecimal sumPostedAmountByType(@Param("type") String type);
    
    /**
     * Find recent transactions (last N days)
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :since " +
           "ORDER BY t.transactionDate DESC, t.transactionTime DESC")
    List<Transaction> findRecentTransactions(@Param("since") LocalDate since);
    
    /**
     * Find transactions by card number and type
     */
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionType = :type " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByCardNumberAndType(@Param("cardNumber") String cardNumber,
                                               @Param("type") String type);
    
    /**
     * Find transactions created in time range
     */
    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startTime AND :endTime " +
           "ORDER BY t.createdAt DESC")
    List<Transaction> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}
