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
    
    Optional<Transaction> findByTranId(Long tranId);
    
    boolean existsByTranId(Long tranId);
    
    List<Transaction> findByTranCardNum(String tranCardNum);
    
    Page<Transaction> findByTranCardNum(String tranCardNum, Pageable pageable);
    
    List<Transaction> findByTranAcctId(String tranAcctId);
    
    Page<Transaction> findByTranAcctId(String tranAcctId, Pageable pageable);
    
    @Query("SELECT MAX(t.tranId) FROM Transaction t")
    Optional<Long> findMaxTranId();
    
    List<Transaction> findByTranTypeCd(String tranTypeCd);
    
    Page<Transaction> findByTranTypeCd(String tranTypeCd, Pageable pageable);
    
    List<Transaction> findByTranOrigTsBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<Transaction> findByTranOrigTsBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    long countByTranAcctId(String tranAcctId);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.tranAcctId = :tranAcctId AND t.tranTypeCd = :tranTypeCd")
    long countByTranAcctIdAndTranTypeCd(@Param("tranAcctId") String tranAcctId, @Param("tranTypeCd") String tranTypeCd);
    
    @Query("SELECT SUM(t.tranAmt) FROM Transaction t WHERE t.tranAcctId = :tranAcctId")
    Optional<BigDecimal> sumTranAmtByTranAcctId(@Param("tranAcctId") String tranAcctId);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranTypeCd = '02' AND t.tranCatCd = 2 ORDER BY t.tranOrigTs DESC")
    List<Transaction> findBillPaymentTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.tranTypeCd = '02' AND t.tranCatCd = 2 ORDER BY t.tranOrigTs DESC")
    Page<Transaction> findBillPaymentTransactions(Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranAcctId = :tranAcctId AND t.tranTypeCd = '02' AND t.tranCatCd = 2 ORDER BY t.tranOrigTs DESC")
    List<Transaction> findBillPaymentTransactionsByTranAcctId(@Param("tranAcctId") String tranAcctId);
    
    @Query("SELECT t FROM Transaction t WHERE t.tranAcctId = :tranAcctId AND t.tranTypeCd = '02' AND t.tranCatCd = 2 ORDER BY t.tranOrigTs DESC")
    Page<Transaction> findBillPaymentTransactionsByTranAcctId(@Param("tranAcctId") String tranAcctId, Pageable pageable);
}
