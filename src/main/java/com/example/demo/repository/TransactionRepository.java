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
    
    Optional<Transaction> findByTranId(Long tranId);
    
    Page<Transaction> findByTranCardNum(String cardNumber, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNumber AND t.transactionId >= :startTransactionId ORDER BY t.transactionId ASC")
    Page<Transaction> findByCardNumberStartingFromTransactionId(
        @Param("cardNumber") String cardNumber,
        @Param("startTransactionId") String startTransactionId,
        Pageable pageable
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId >= :startTransactionId ORDER BY t.transactionId ASC")
    Page<Transaction> findStartingFromTransactionId(
        @Param("startTransactionId") String startTransactionId,
        Pageable pageable
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId < :transactionId ORDER BY t.transactionId DESC")
    List<Transaction> findPreviousTransactions(
        @Param("transactionId") String transactionId,
        Pageable pageable
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionId > :transactionId ORDER BY t.transactionId ASC")
    List<Transaction> findNextTransactions(
        @Param("transactionId") String transactionId,
        Pageable pageable
    );
    
    Page<Transaction> findByTranMerchantId(String merchantId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranOrigTs BETWEEN :startDate AND :endDate ORDER BY t.tranOrigTs ASC")
    Page<Transaction> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNumber AND t.tranOrigTs BETWEEN :startDate AND :endDate ORDER BY t.tranOrigTs ASC")
    Page<Transaction> findByCardNumberAndDateRange(
        @Param("cardNumber") String cardNumber,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );
    
    Page<Transaction> findByTranTypeCd(Integer transactionTypeCode, Pageable pageable);
    
    Page<Transaction> findByTranCatCd(Integer transactionCategoryCode, Pageable pageable);
    
    Page<Transaction> findByTranSource(String transactionSource, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranAmt >= :minAmount AND t.tranAmt <= :maxAmount ORDER BY t.tranAmt DESC")
    Page<Transaction> findByAmountRange(
        @Param("minAmount") BigDecimal minAmount,
        @Param("maxAmount") BigDecimal maxAmount,
        Pageable pageable
    );
    
    @Query("SELECT MAX(t.tranId) FROM Transaction t")
    Long findMaxTranId();
    
    @Query("SELECT MAX(t.transactionId) FROM Transaction t")
    String findMaxTransactionId();
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.tranCardNum = :cardNumber")
    long countByCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT SUM(t.tranAmt) FROM Transaction t WHERE t.tranCardNum = :cardNumber")
    BigDecimal sumAmountByCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT SUM(t.tranAmt) FROM Transaction t WHERE t.tranCardNum = :cardNumber AND t.tranOrigTs BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByCardNumberAndDateRange(
        @Param("cardNumber") String cardNumber,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.tranDesc) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.tranMerchantName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Transaction> searchByDescriptionOrMerchantName(
        @Param("searchTerm") String searchTerm,
        Pageable pageable
    );
    
    Page<Transaction> findAllByOrderByTransactionIdAsc(Pageable pageable);
    
    Page<Transaction> findAllByOrderByTranOrigTsDesc(Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranOrigTs >= :since ORDER BY t.tranOrigTs DESC")
    List<Transaction> findRecentTransactions(@Param("since") LocalDate since);
}
