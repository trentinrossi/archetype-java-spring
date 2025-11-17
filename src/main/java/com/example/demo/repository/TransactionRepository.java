package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    boolean existsByTransactionId(String transactionId);
    
    List<Transaction> findByCardNumber(String cardNumber);
    
    Page<Transaction> findByCardNumber(String cardNumber, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber ORDER BY t.tranId DESC")
    Page<Transaction> findByCardNumberOrderByTranIdDesc(@Param("cardNumber") String cardNumber, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.merchantId = :merchantId ORDER BY t.tranId DESC")
    Page<Transaction> findByMerchantId(@Param("merchantId") String merchantId, Pageable pageable);
    
    List<Transaction> findByMerchantId(String merchantId);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranOrigTs BETWEEN :startDate AND :endDate ORDER BY t.tranId DESC")
    Page<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranOrigTs BETWEEN :startDate AND :endDate")
    List<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.tranOrigTs BETWEEN :startDate AND :endDate ORDER BY t.tranId DESC")
    Page<Transaction> findByCardNumberAndDateRange(@Param("cardNumber") String cardNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId >= :startTransactionId ORDER BY t.transactionId ASC")
    Page<Transaction> findTransactionsStartingFrom(@Param("startTransactionId") String startTransactionId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId > :lastTransactionId ORDER BY t.transactionId ASC")
    List<Transaction> findNextTransactions(@Param("lastTransactionId") String lastTransactionId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId < :firstTransactionId ORDER BY t.transactionId DESC")
    List<Transaction> findPreviousTransactions(@Param("firstTransactionId") String firstTransactionId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t ORDER BY t.tranId ASC")
    Page<Transaction> findAllOrderByTranIdAsc(Pageable pageable);
    
    @Query("SELECT t FROM Transaction t ORDER BY t.tranId DESC")
    Page<Transaction> findAllOrderByTranIdDesc(Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Transaction t")
    long countAllTransactions();
    
    long countByCardNumber(String cardNumber);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.tranOrigTs BETWEEN :startDate AND :endDate")
    long countByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT MAX(t.tranId) FROM Transaction t")
    Optional<Long> findMaxTransactionId();
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionTypeCode = :typeCode ORDER BY t.tranId DESC")
    Page<Transaction> findByTransactionTypeCode(@Param("typeCode") String typeCode, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionCategoryCode = :categoryCode ORDER BY t.tranId DESC")
    Page<Transaction> findByTransactionCategoryCode(@Param("categoryCode") String categoryCode, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionSource = :source ORDER BY t.tranId DESC")
    Page<Transaction> findByTransactionSource(@Param("source") String source, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranAmt >= :minAmount AND t.tranAmt <= :maxAmount ORDER BY t.tranId DESC")
    Page<Transaction> findByAmountRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.merchantName) LIKE LOWER(CONCAT('%', :merchantName, '%')) ORDER BY t.tranId DESC")
    Page<Transaction> findByMerchantNameContaining(@Param("merchantName") String merchantName, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.merchantCity = :city ORDER BY t.tranId DESC")
    Page<Transaction> findByMerchantCity(@Param("city") String city, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.merchantZip = :zip ORDER BY t.tranId DESC")
    Page<Transaction> findByMerchantZip(@Param("zip") String zip, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.tranDesc) LIKE LOWER(CONCAT('%', :description, '%')) ORDER BY t.tranId DESC")
    Page<Transaction> findByDescriptionContaining(@Param("description") String description, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionTypeCode = :typeCode ORDER BY t.tranId DESC")
    Page<Transaction> findByCardNumberAndTransactionType(@Param("cardNumber") String cardNumber, @Param("typeCode") String typeCode, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionCategoryCode = :categoryCode ORDER BY t.tranId DESC")
    Page<Transaction> findByCardNumberAndCategory(@Param("cardNumber") String cardNumber, @Param("categoryCode") String categoryCode, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranProcTs BETWEEN :startDate AND :endDate ORDER BY t.tranId DESC")
    Page<Transaction> findByProcessedDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.tranAmt >= :minAmount ORDER BY t.tranId DESC")
    Page<Transaction> findByCardNumberAndMinAmount(@Param("cardNumber") String cardNumber, @Param("minAmount") BigDecimal minAmount, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.merchantId = :merchantId ORDER BY t.tranId DESC")
    Page<Transaction> findByCardNumberAndMerchantId(@Param("cardNumber") String cardNumber, @Param("merchantId") String merchantId, Pageable pageable);
    
    @Query("SELECT DISTINCT t.transactionTypeCode FROM Transaction t ORDER BY t.transactionTypeCode")
    List<String> findDistinctTransactionTypeCodes();
    
    @Query("SELECT DISTINCT t.transactionCategoryCode FROM Transaction t ORDER BY t.transactionCategoryCode")
    List<String> findDistinctTransactionCategoryCodes();
    
    @Query("SELECT DISTINCT t.transactionSource FROM Transaction t ORDER BY t.transactionSource")
    List<String> findDistinctTransactionSources();
    
    @Query("SELECT SUM(t.tranAmt) FROM Transaction t WHERE t.cardNumber = :cardNumber")
    Optional<BigDecimal> sumTransactionAmountByCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT SUM(t.tranAmt) FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.tranOrigTs BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumTransactionAmountByCardNumberAndDateRange(@Param("cardNumber") String cardNumber, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT AVG(t.tranAmt) FROM Transaction t WHERE t.cardNumber = :cardNumber")
    Optional<BigDecimal> averageTransactionAmountByCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId IN :transactionIds ORDER BY t.tranId DESC")
    List<Transaction> findByTransactionIdIn(@Param("transactionIds") List<String> transactionIds);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.tranOrigTs >= :sinceDate ORDER BY t.tranId DESC")
    List<Transaction> findRecentTransactionsByCardNumber(@Param("cardNumber") String cardNumber, @Param("sinceDate") LocalDate sinceDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranOrigTs >= :sinceDate ORDER BY t.tranId DESC")
    Page<Transaction> findRecentTransactions(@Param("sinceDate") LocalDate sinceDate, Pageable pageable);
    
    boolean existsByTranId(Long tranId);
    
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.tranOrigTs = :transactionDate AND t.tranAmt = :amount AND t.merchantId = :merchantId")
    boolean existsDuplicateTransaction(@Param("cardNumber") String cardNumber, @Param("transactionDate") LocalDate transactionDate, @Param("amount") BigDecimal amount, @Param("merchantId") String merchantId);
}
