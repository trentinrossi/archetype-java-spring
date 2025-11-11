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
    
    /**
     * BR006: Find all transactions for a specific account
     */
    List<Transaction> findByAccount_AccountId(String accountId);
    
    /**
     * BR006: Find all transactions for a specific account (paginated)
     */
    Page<Transaction> findByAccount_AccountId(String accountId, Pageable pageable);
    
    /**
     * BR006: Find transactions by card number
     */
    List<Transaction> findByCardNumber(String cardNumber);
    
    /**
     * BR006: Find bill payment transactions (type code 02, category 2)
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionTypeCode = '02' AND t.transactionCategoryCode = 2")
    List<Transaction> findBillPaymentTransactions();
    
    /**
     * BR006: Find bill payment transactions for a specific account
     */
    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionTypeCode = '02' AND t.transactionCategoryCode = 2")
    List<Transaction> findBillPaymentTransactionsByAccount(@Param("accountId") String accountId);
    
    /**
     * Find transactions by type code
     */
    List<Transaction> findByTransactionTypeCode(String transactionTypeCode);
    
    /**
     * Find transactions by category code
     */
    List<Transaction> findByTransactionCategoryCode(Integer transactionCategoryCode);
    
    /**
     * Find transactions by type and category code
     */
    List<Transaction> findByTransactionTypeCodeAndTransactionCategoryCode(String transactionTypeCode, Integer transactionCategoryCode);
    
    /**
     * Find transactions within a date range
     */
    @Query("SELECT t FROM Transaction t WHERE t.originationTimestamp BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find transactions by amount range
     */
    @Query("SELECT t FROM Transaction t WHERE t.amount BETWEEN :minAmount AND :maxAmount")
    List<Transaction> findTransactionsByAmountRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    /**
     * BR005: Get the maximum transaction ID for generating next ID
     */
    @Query("SELECT MAX(t.transactionId) FROM Transaction t")
    Optional<Long> findMaxTransactionId();
    
    /**
     * Count transactions for a specific account
     */
    long countByAccount_AccountId(String accountId);
    
    /**
     * Sum of transaction amounts for a specific account
     */
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.accountId = :accountId")
    BigDecimal sumTransactionAmountsByAccount(@Param("accountId") String accountId);
    
    /**
     * Find recent transactions (last N days)
     */
    @Query("SELECT t FROM Transaction t WHERE t.originationTimestamp >= :since ORDER BY t.originationTimestamp DESC")
    List<Transaction> findRecentTransactions(@Param("since") LocalDateTime since);
    
    /**
     * Find transactions by merchant ID
     */
    List<Transaction> findByMerchantId(Long merchantId);
    
    /**
     * Find transactions by merchant name
     */
    List<Transaction> findByMerchantName(String merchantName);
}
