package com.carddemo.billpayment.repository;

import com.carddemo.billpayment.entity.Transaction;
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
    
    Optional<Transaction> findTopByOrderByTranIdDesc();
    
    List<Transaction> findByTranCardNum(String tranCardNum);
    
    Page<Transaction> findByTranCardNum(String tranCardNum, Pageable pageable);
    
    List<Transaction> findByTranTypeCd(String tranTypeCd);
    
    Page<Transaction> findByTranTypeCd(String tranTypeCd, Pageable pageable);
    
    List<Transaction> findByTranCatCd(Integer tranCatCd);
    
    Page<Transaction> findByTranCatCd(Integer tranCatCd, Pageable pageable);
    
    List<Transaction> findByTranTypeCdAndTranCatCd(String tranTypeCd, Integer tranCatCd);
    
    Page<Transaction> findByTranTypeCdAndTranCatCd(String tranTypeCd, Integer tranCatCd, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranTypeCd = '02' AND t.tranCatCd = 2")
    List<Transaction> findBillPaymentTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.tranTypeCd = '02' AND t.tranCatCd = 2")
    Page<Transaction> findBillPaymentTransactions(Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranTypeCd = '02' AND t.tranCatCd = 2")
    List<Transaction> findBillPaymentTransactionsByCardNum(@Param("cardNum") String cardNum);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranTypeCd = '02' AND t.tranCatCd = 2")
    Page<Transaction> findBillPaymentTransactionsByCardNum(@Param("cardNum") String cardNum, Pageable pageable);
    
    List<Transaction> findByTranOrigTsBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Transaction> findByTranOrigTsBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<Transaction> findByTranProcTsBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Transaction> findByTranProcTsBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranOrigTs BETWEEN :startDate AND :endDate")
    List<Transaction> findByCardNumAndDateRange(@Param("cardNum") String cardNum, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranOrigTs BETWEEN :startDate AND :endDate")
    Page<Transaction> findByCardNumAndDateRange(@Param("cardNum") String cardNum, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate, 
                                                 Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranOrigTs >= :since ORDER BY t.tranOrigTs DESC")
    List<Transaction> findRecentTransactions(@Param("since") LocalDateTime since);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranOrigTs >= :since ORDER BY t.tranOrigTs DESC")
    Page<Transaction> findRecentTransactions(@Param("since") LocalDateTime since, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranOrigTs >= :since ORDER BY t.tranOrigTs DESC")
    List<Transaction> findRecentTransactionsByCardNum(@Param("cardNum") String cardNum, 
                                                       @Param("since") LocalDateTime since);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranOrigTs >= :since ORDER BY t.tranOrigTs DESC")
    Page<Transaction> findRecentTransactionsByCardNum(@Param("cardNum") String cardNum, 
                                                       @Param("since") LocalDateTime since, 
                                                       Pageable pageable);
    
    List<Transaction> findByTranMerchantId(Integer tranMerchantId);
    
    Page<Transaction> findByTranMerchantId(Integer tranMerchantId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.tranMerchantName) LIKE LOWER(CONCAT('%', :merchantName, '%'))")
    List<Transaction> findByMerchantNameContaining(@Param("merchantName") String merchantName);
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.tranMerchantName) LIKE LOWER(CONCAT('%', :merchantName, '%'))")
    Page<Transaction> findByMerchantNameContaining(@Param("merchantName") String merchantName, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.tranDesc) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Transaction> findByDescriptionContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.tranDesc) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Transaction> findByDescriptionContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    long countByTranCardNum(String tranCardNum);
    
    long countByTranTypeCd(String tranTypeCd);
    
    long countByTranCatCd(Integer tranCatCd);
    
    long countByTranTypeCdAndTranCatCd(String tranTypeCd, Integer tranCatCd);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.tranTypeCd = '02' AND t.tranCatCd = 2")
    long countBillPaymentTransactions();
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranTypeCd = '02' AND t.tranCatCd = 2")
    long countBillPaymentTransactionsByCardNum(@Param("cardNum") String cardNum);
    
    @Query("SELECT COALESCE(SUM(t.tranAmt), 0) FROM Transaction t WHERE t.tranCardNum = :cardNum")
    BigDecimal sumTransactionAmountsByCardNum(@Param("cardNum") String cardNum);
    
    @Query("SELECT COALESCE(SUM(t.tranAmt), 0) FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranOrigTs BETWEEN :startDate AND :endDate")
    BigDecimal sumTransactionAmountsByCardNumAndDateRange(@Param("cardNum") String cardNum, 
                                                           @Param("startDate") LocalDateTime startDate, 
                                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(t.tranAmt), 0) FROM Transaction t WHERE t.tranTypeCd = :tranTypeCd")
    BigDecimal sumTransactionAmountsByType(@Param("tranTypeCd") String tranTypeCd);
    
    @Query("SELECT COALESCE(SUM(t.tranAmt), 0) FROM Transaction t WHERE t.tranCatCd = :tranCatCd")
    BigDecimal sumTransactionAmountsByCategory(@Param("tranCatCd") Integer tranCatCd);
    
    @Query("SELECT COALESCE(SUM(t.tranAmt), 0) FROM Transaction t WHERE t.tranTypeCd = '02' AND t.tranCatCd = 2")
    BigDecimal sumBillPaymentTransactionAmounts();
    
    @Query("SELECT COALESCE(SUM(t.tranAmt), 0) FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranTypeCd = '02' AND t.tranCatCd = 2")
    BigDecimal sumBillPaymentTransactionAmountsByCardNum(@Param("cardNum") String cardNum);
    
    @Query("SELECT COALESCE(AVG(t.tranAmt), 0) FROM Transaction t WHERE t.tranCardNum = :cardNum")
    BigDecimal averageTransactionAmountByCardNum(@Param("cardNum") String cardNum);
    
    @Query("SELECT COALESCE(AVG(t.tranAmt), 0) FROM Transaction t WHERE t.tranTypeCd = '02' AND t.tranCatCd = 2")
    BigDecimal averageBillPaymentTransactionAmount();
    
    boolean existsByTranId(Long tranId);
    
    boolean existsByTranCardNum(String tranCardNum);
    
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Transaction t WHERE t.tranCardNum = :cardNum AND t.tranOrigTs BETWEEN :startDate AND :endDate")
    boolean existsByCardNumAndDateRange(@Param("cardNum") String cardNum, 
                                        @Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    List<Transaction> findByTranSource(String tranSource);
    
    Page<Transaction> findByTranSource(String tranSource, Pageable pageable);
    
    long countByTranSource(String tranSource);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum ORDER BY t.tranOrigTs DESC")
    List<Transaction> findByCardNumOrderByOrigTsDesc(@Param("cardNum") String cardNum);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum ORDER BY t.tranOrigTs DESC")
    Page<Transaction> findByCardNumOrderByOrigTsDesc(@Param("cardNum") String cardNum, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum ORDER BY t.tranAmt DESC")
    List<Transaction> findByCardNumOrderByAmountDesc(@Param("cardNum") String cardNum);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranCardNum = :cardNum ORDER BY t.tranAmt DESC")
    Page<Transaction> findByCardNumOrderByAmountDesc(@Param("cardNum") String cardNum, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.account.acctId = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") String accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.account.acctId = :accountId")
    Page<Transaction> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.acctId = :accountId")
    long countByAccountId(@Param("accountId") String accountId);
}
