package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Transaction.TransactionId> {
    
    // Composite key operations
    Optional<Transaction> findByCardNumberAndTransactionId(String cardNumber, String transactionId);
    
    boolean existsByCardNumberAndTransactionId(String cardNumber, String transactionId);
    
    void deleteByCardNumberAndTransactionId(String cardNumber, String transactionId);
    
    // Sequential reading operations based on CBSTM03B functionality
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber >= :cardNumber ORDER BY t.cardNumber ASC, t.transactionId ASC")
    List<Transaction> findTransactionsSequentiallyFromCardNumber(@Param("cardNumber") String cardNumber, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionId >= :transactionId ORDER BY t.transactionId ASC")
    List<Transaction> findTransactionsSequentiallyFromTransactionId(@Param("cardNumber") String cardNumber, @Param("transactionId") String transactionId, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE (t.cardNumber > :cardNumber) OR (t.cardNumber = :cardNumber AND t.transactionId >= :transactionId) ORDER BY t.cardNumber ASC, t.transactionId ASC")
    List<Transaction> findTransactionsSequentiallyFromCompositeKey(@Param("cardNumber") String cardNumber, @Param("transactionId") String transactionId, Pageable pageable);
    
    // Card number based queries
    List<Transaction> findByCardNumberOrderByTransactionIdAsc(String cardNumber);
    
    Page<Transaction> findByCardNumber(String cardNumber, Pageable pageable);
    
    List<Transaction> findByCardNumberAndTransactionStatus(String cardNumber, String transactionStatus);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber = :cardNumber")
    long countTransactionsByCardNumber(@Param("cardNumber") String cardNumber);
    
    // Transaction status queries
    Page<Transaction> findByTransactionStatus(String transactionStatus, Pageable pageable);
    
    long countByTransactionStatus(String transactionStatus);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionStatus = :status ORDER BY t.createdAt DESC")
    List<Transaction> findRecentTransactionsByStatus(@Param("status") String status, Pageable pageable);
    
    // Date and timestamp filtering
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :fromDate AND t.transactionDate <= :toDate ORDER BY t.transactionDate ASC, t.transactionTime ASC")
    List<Transaction> findByTransactionDateBetween(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
    
    @Query("SELECT t FROM Transaction t WHERE t.createdAt >= :fromDate AND t.createdAt <= :toDate ORDER BY t.createdAt ASC")
    Page<Transaction> findByCreatedAtBetween(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionDate >= :fromDate AND t.transactionDate <= :toDate ORDER BY t.transactionDate ASC, t.transactionTime ASC")
    List<Transaction> findByCardNumberAndDateRange(@Param("cardNumber") String cardNumber, @Param("fromDate") String fromDate, @Param("toDate") String toDate);
    
    // Transaction type queries
    Page<Transaction> findByTransactionType(String transactionType, Pageable pageable);
    
    List<Transaction> findByCardNumberAndTransactionType(String cardNumber, String transactionType);
    
    @Query("SELECT t FROM Transaction t WHERE t.transactionType = :type AND t.transactionStatus = :status ORDER BY t.createdAt DESC")
    List<Transaction> findByTransactionTypeAndStatus(@Param("type") String transactionType, @Param("status") String transactionStatus);
    
    // Amount-based queries
    @Query("SELECT t FROM Transaction t WHERE CAST(t.transactionAmount AS double) >= :minAmount AND CAST(t.transactionAmount AS double) <= :maxAmount")
    List<Transaction> findByAmountRange(@Param("minAmount") Double minAmount, @Param("maxAmount") Double maxAmount);
    
    @Query("SELECT t FROM Transaction t WHERE CAST(t.transactionAmount AS double) >= 100000.0 ORDER BY CAST(t.transactionAmount AS double) DESC")
    List<Transaction> findHighValueTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber AND CAST(t.transactionAmount AS double) >= :minAmount ORDER BY CAST(t.transactionAmount AS double) DESC")
    List<Transaction> findByCardNumberAndMinimumAmount(@Param("cardNumber") String cardNumber, @Param("minAmount") Double minAmount);
    
    // Merchant and location queries
    Page<Transaction> findByMerchantId(String merchantId, Pageable pageable);
    
    Page<Transaction> findByMerchantName(String merchantName, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.merchantId = :merchantId AND t.transactionStatus = :status")
    List<Transaction> findByMerchantIdAndStatus(@Param("merchantId") String merchantId, @Param("status") String status);
    
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.merchantName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Transaction> findByMerchantNameContaining(@Param("name") String name, Pageable pageable);
    
    // Terminal-based queries
    List<Transaction> findByTerminalId(String terminalId);
    
    @Query("SELECT t FROM Transaction t WHERE t.terminalId = :terminalId AND t.transactionDate >= :fromDate ORDER BY t.transactionDate DESC, t.transactionTime DESC")
    List<Transaction> findByTerminalIdAndDateFrom(@Param("terminalId") String terminalId, @Param("fromDate") String fromDate);
    
    // Authorization and response code queries
    List<Transaction> findByAuthorizationCode(String authorizationCode);
    
    Page<Transaction> findByResponseCode(String responseCode, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.responseCode != '00' ORDER BY t.createdAt DESC")
    List<Transaction> findFailedTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.responseCode = '00' ORDER BY t.createdAt DESC")
    List<Transaction> findSuccessfulTransactions(Pageable pageable);
    
    // Currency-based queries
    List<Transaction> findByCurrencyCode(String currencyCode);
    
    @Query("SELECT t FROM Transaction t WHERE t.currencyCode = :currency AND CAST(t.transactionAmount AS double) >= :minAmount")
    List<Transaction> findByCurrencyAndMinAmount(@Param("currency") String currencyCode, @Param("minAmount") Double minAmount);
    
    @Query("SELECT t FROM Transaction t WHERE t.currencyCode != 'USD' AND t.currencyCode != '840'")
    List<Transaction> findInternationalTransactions();
    
    // Processing code queries
    @Query("SELECT t FROM Transaction t WHERE t.processingCode = :code")
    List<Transaction> findByProcessingCode(@Param("code") String processingCode);
    
    @Query("SELECT t FROM Transaction t WHERE t.processingCode IN ('00', '01', '20', '31')")
    List<Transaction> findCommonTransactionTypes();
    
    // Entry mode queries
    @Query("SELECT t FROM Transaction t WHERE t.posEntryMode = :mode")
    List<Transaction> findByPosEntryMode(@Param("mode") String posEntryMode);
    
    @Query("SELECT t FROM Transaction t WHERE t.posEntryMode IN ('07', '91')")
    List<Transaction> findContactlessTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.posEntryMode IN ('05', '95')")
    List<Transaction> findChipTransactions();
    
    // Settlement queries
    @Query("SELECT t FROM Transaction t WHERE t.settlementDate IS NOT NULL AND t.settlementDate != ''")
    List<Transaction> findSettledTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.settlementDate IS NULL OR t.settlementDate = ''")
    List<Transaction> findUnsettledTransactions();
    
    @Query("SELECT t FROM Transaction t WHERE t.settlementDate = :date")
    List<Transaction> findBySettlementDate(@Param("date") String settlementDate);
    
    // Batch and sequence queries
    @Query("SELECT t FROM Transaction t WHERE t.batchNumber = :batch")
    List<Transaction> findByBatchNumber(@Param("batch") String batchNumber);
    
    @Query("SELECT t FROM Transaction t WHERE t.sequenceNumber = :sequence")
    List<Transaction> findBySequenceNumber(@Param("sequence") String sequenceNumber);
    
    // Network and acquirer queries
    @Query("SELECT t FROM Transaction t WHERE t.networkId = :network")
    List<Transaction> findByNetworkId(@Param("network") String networkId);
    
    @Query("SELECT t FROM Transaction t WHERE t.acquirerId = :acquirer")
    List<Transaction> findByAcquirerId(@Param("acquirer") String acquirerId);
    
    // Complex filtering for sequential reads
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:cardNumber IS NULL OR t.cardNumber >= :cardNumber) AND " +
           "(:transactionType IS NULL OR t.transactionType = :transactionType) AND " +
           "(:status IS NULL OR t.transactionStatus = :status) AND " +
           "(:fromDate IS NULL OR t.transactionDate >= :fromDate) AND " +
           "(:toDate IS NULL OR t.transactionDate <= :toDate) AND " +
           "(:merchantId IS NULL OR t.merchantId = :merchantId) AND " +
           "(:terminalId IS NULL OR t.terminalId = :terminalId) " +
           "ORDER BY t.cardNumber ASC, t.transactionId ASC")
    List<Transaction> findTransactionsWithFilters(
            @Param("cardNumber") String cardNumber,
            @Param("transactionType") String transactionType,
            @Param("status") String status,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("merchantId") String merchantId,
            @Param("terminalId") String terminalId,
            Pageable pageable);
    
    // Statistical queries
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionStatus = 'A'")
    long countApprovedTransactionsByCard(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT SUM(CAST(t.transactionAmount AS double)) FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionStatus = 'A'")
    Double getTotalAmountByCard(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.transactionDate >= :date")
    long countTransactionsSince(@Param("date") String date);
    
    // Recent activity queries
    @Query("SELECT t FROM Transaction t WHERE t.createdAt >= :since ORDER BY t.createdAt DESC")
    List<Transaction> findRecentTransactions(@Param("since") LocalDateTime since);
    
    @Query("SELECT t FROM Transaction t WHERE t.cardNumber = :cardNumber ORDER BY t.createdAt DESC")
    List<Transaction> findLatestTransactionsByCard(@Param("cardNumber") String cardNumber, Pageable pageable);
    
    // Duplicate detection
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber = :cardNumber AND t.transactionAmount = :amount AND t.merchantId = :merchantId AND t.transactionDate = :date AND t.transactionTime = :time")
    long countPotentialDuplicates(@Param("cardNumber") String cardNumber, @Param("amount") String amount, @Param("merchantId") String merchantId, @Param("date") String date, @Param("time") String time);
    
    // Count methods for sequential reading
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber >= :cardNumber")
    long countTransactionsFromCardNumber(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber >= :cardNumber AND t.transactionStatus = :status")
    long countTransactionsFromCardNumberByStatus(@Param("cardNumber") String cardNumber, @Param("status") String status);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.cardNumber >= :cardNumber AND t.transactionType = :type")
    long countTransactionsFromCardNumberByType(@Param("cardNumber") String cardNumber, @Param("type") String type);
    
    // Navigation methods
    @Query("SELECT MAX(t.cardNumber) FROM Transaction t")
    Optional<String> findMaxCardNumber();
    
    @Query("SELECT MIN(t.cardNumber) FROM Transaction t")
    Optional<String> findMinCardNumber();
    
    @Query("SELECT t FROM Transaction t WHERE (t.cardNumber > :cardNumber) OR (t.cardNumber = :cardNumber AND t.transactionId > :transactionId) ORDER BY t.cardNumber ASC, t.transactionId ASC")
    Optional<Transaction> findNextTransaction(@Param("cardNumber") String cardNumber, @Param("transactionId") String transactionId);
    
    @Query("SELECT t FROM Transaction t WHERE (t.cardNumber < :cardNumber) OR (t.cardNumber = :cardNumber AND t.transactionId < :transactionId) ORDER BY t.cardNumber DESC, t.transactionId DESC")
    Optional<Transaction> findPreviousTransaction(@Param("cardNumber") String cardNumber, @Param("transactionId") String transactionId);
    
    // Distinct values
    @Query("SELECT DISTINCT t.transactionStatus FROM Transaction t ORDER BY t.transactionStatus")
    List<String> findDistinctTransactionStatuses();
    
    @Query("SELECT DISTINCT t.transactionType FROM Transaction t WHERE t.transactionType IS NOT NULL ORDER BY t.transactionType")
    List<String> findDistinctTransactionTypes();
    
    @Query("SELECT DISTINCT t.merchantCategory FROM Transaction t WHERE t.merchantCategory IS NOT NULL ORDER BY t.merchantCategory")
    List<String> findDistinctMerchantCategories();
    
    @Query("SELECT DISTINCT t.currencyCode FROM Transaction t WHERE t.currencyCode IS NOT NULL ORDER BY t.currencyCode")
    List<String> findDistinctCurrencyCodes();
    
    @Query("SELECT DISTINCT t.networkId FROM Transaction t WHERE t.networkId IS NOT NULL ORDER BY t.networkId")
    List<String> findDistinctNetworkIds();
}