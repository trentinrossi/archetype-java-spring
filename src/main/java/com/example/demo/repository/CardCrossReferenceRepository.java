package com.example.demo.repository;

import com.example.demo.entity.CardCrossReference;
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
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, String> {
    
    // Basic finder methods
    Optional<CardCrossReference> findByCardNumber(String cardNumber);
    
    boolean existsByCardNumber(String cardNumber);
    
    Page<CardCrossReference> findByCardStatus(String cardStatus, Pageable pageable);
    
    Page<CardCrossReference> findByCardType(String cardType, Pageable pageable);
    
    Page<CardCrossReference> findByCardStatusAndCardType(String cardStatus, String cardType, Pageable pageable);
    
    List<CardCrossReference> findByCustomerId(String customerId);
    
    Page<CardCrossReference> findByCustomerId(String customerId, Pageable pageable);
    
    List<CardCrossReference> findByAccountNumber(String accountNumber);
    
    Page<CardCrossReference> findByAccountNumber(String accountNumber, Pageable pageable);
    
    List<CardCrossReference> findByBranchCode(String branchCode);
    
    Page<CardCrossReference> findByBranchCode(String branchCode, Pageable pageable);
    
    // Sequential reading methods based on CBACT03C and CBSTM03B functionality
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findSequentialByCardNumber(@Param("startingCardNumber") String startingCardNumber, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber AND c.cardStatus = :cardStatus ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findSequentialByCardNumberAndStatus(@Param("startingCardNumber") String startingCardNumber, @Param("cardStatus") String cardStatus, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber AND c.cardType = :cardType ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findSequentialByCardNumberAndType(@Param("startingCardNumber") String startingCardNumber, @Param("cardType") String cardType, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber AND c.cardStatus = :cardStatus AND c.cardType = :cardType ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findSequentialByCardNumberStatusAndType(@Param("startingCardNumber") String startingCardNumber, @Param("cardStatus") String cardStatus, @Param("cardType") String cardType, Pageable pageable);
    
    // Card number pattern searches
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber LIKE CONCAT(:cardNumberPrefix, '%') ORDER BY c.cardNumber ASC")
    Page<CardCrossReference> findByCardNumberPrefix(@Param("cardNumberPrefix") String cardNumberPrefix, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE LOWER(c.embossName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<CardCrossReference> findByEmbossNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Recent activity queries
    @Query("SELECT c FROM CardCrossReference c WHERE c.createdAt >= :since ORDER BY c.createdAt DESC")
    List<CardCrossReference> findRecentCards(@Param("since") LocalDateTime since);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.updatedAt >= :since ORDER BY c.updatedAt DESC")
    List<CardCrossReference> findRecentlyUpdatedCards(@Param("since") LocalDateTime since);
    
    // Count methods
    long countByCardStatus(String cardStatus);
    
    long countByCardType(String cardType);
    
    long countByCardStatusAndCardType(String cardStatus, String cardType);
    
    long countByCustomerId(String customerId);
    
    long countByAccountNumber(String accountNumber);
    
    long countByBranchCode(String branchCode);
    
    // Status-specific counts
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardStatus = 'A'")
    long countActiveCards();
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardStatus = 'B'")
    long countBlockedCards();
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardStatus = 'S'")
    long countSuspendedCards();
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardStatus = 'C'")
    long countCancelledCards();
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardStatus = 'E'")
    long countExpiredCards();
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardStatus = 'P'")
    long countPendingCards();
    
    // Block code queries
    @Query("SELECT c FROM CardCrossReference c WHERE c.blockCode IS NOT NULL AND c.blockCode != '' AND c.blockCode != '00'")
    Page<CardCrossReference> findCardsWithBlockCode(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.blockCode = :blockCode")
    Page<CardCrossReference> findByBlockCode(@Param("blockCode") String blockCode, Pageable pageable);
    
    // Replacement card queries
    @Query("SELECT c FROM CardCrossReference c WHERE c.replacementCard IS NOT NULL AND c.replacementCard != '' AND c.replacementCard != '0000000000000000'")
    Page<CardCrossReference> findCardsWithReplacement(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.previousCard IS NOT NULL AND c.previousCard != '' AND c.previousCard != '0000000000000000'")
    Page<CardCrossReference> findCardsWithPrevious(Pageable pageable);
    
    // Feature flag queries
    @Query("SELECT c FROM CardCrossReference c WHERE c.internationalFlag = 'Y' OR c.internationalFlag = '1'")
    Page<CardCrossReference> findInternationalEnabledCards(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.contactlessFlag = 'Y' OR c.contactlessFlag = '1'")
    Page<CardCrossReference> findContactlessEnabledCards(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.mobilePaymentFlag = 'Y' OR c.mobilePaymentFlag = '1'")
    Page<CardCrossReference> findMobilePaymentEnabledCards(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.priorityFlag = 'Y' OR c.priorityFlag = '1'")
    Page<CardCrossReference> findPriorityCards(Pageable pageable);
    
    // Delivery and product queries
    @Query("SELECT c FROM CardCrossReference c WHERE c.deliveryMethod = :deliveryMethod")
    Page<CardCrossReference> findByDeliveryMethod(@Param("deliveryMethod") String deliveryMethod, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.accountType = :accountType")
    Page<CardCrossReference> findByAccountType(@Param("accountType") String accountType, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.productCode = :productCode")
    Page<CardCrossReference> findByProductCode(@Param("productCode") String productCode, Pageable pageable);
    
    // Date range queries
    @Query("SELECT c FROM CardCrossReference c WHERE c.issueDate >= :fromDate AND c.issueDate <= :toDate")
    Page<CardCrossReference> findByIssueDateRange(@Param("fromDate") String fromDate, @Param("toDate") String toDate, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.expiryDate >= :fromDate AND c.expiryDate <= :toDate")
    Page<CardCrossReference> findByExpiryDateRange(@Param("fromDate") String fromDate, @Param("toDate") String toDate, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.activationDate >= :fromDate AND c.activationDate <= :toDate")
    Page<CardCrossReference> findByActivationDateRange(@Param("fromDate") String fromDate, @Param("toDate") String toDate, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.lastActivityDate >= :fromDate AND c.lastActivityDate <= :toDate")
    Page<CardCrossReference> findByLastActivityDateRange(@Param("fromDate") String fromDate, @Param("toDate") String toDate, Pageable pageable);
    
    // Limit-based queries
    @Query("SELECT c FROM CardCrossReference c WHERE CAST(c.dailyLimit AS DOUBLE) >= :minLimit AND CAST(c.dailyLimit AS DOUBLE) <= :maxLimit")
    Page<CardCrossReference> findByDailyLimitRange(@Param("minLimit") Double minLimit, @Param("maxLimit") Double maxLimit, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE CAST(c.monthlyLimit AS DOUBLE) >= :minLimit AND CAST(c.monthlyLimit AS DOUBLE) <= :maxLimit")
    Page<CardCrossReference> findByMonthlyLimitRange(@Param("minLimit") Double minLimit, @Param("maxLimit") Double maxLimit, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE CAST(c.dailyLimit AS DOUBLE) >= 500000")
    Page<CardCrossReference> findHighValueCards(Pageable pageable);
    
    // Active and usable cards
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardStatus = 'A' AND (c.blockCode IS NULL OR c.blockCode = '' OR c.blockCode = '00') AND c.expiryDate > :currentDate")
    Page<CardCrossReference> findActiveUsableCards(@Param("currentDate") String currentDate, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.expiryDate <= :currentDate")
    Page<CardCrossReference> findExpiredCards(@Param("currentDate") String currentDate, Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.lastActivityDate >= :recentDate")
    Page<CardCrossReference> findRecentlyActiveCards(@Param("recentDate") String recentDate, Pageable pageable);
    
    // Card type specific queries
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardType IN ('01', 'DB')")
    Page<CardCrossReference> findDebitCards(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardType IN ('02', 'CR')")
    Page<CardCrossReference> findCreditCards(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardType IN ('03', 'PP')")
    Page<CardCrossReference> findPrepaidCards(Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardType IN ('04', 'BZ')")
    Page<CardCrossReference> findBusinessCards(Pageable pageable);
    
    // Distinct values
    @Query("SELECT DISTINCT c.cardStatus FROM CardCrossReference c ORDER BY c.cardStatus")
    List<String> findDistinctCardStatuses();
    
    @Query("SELECT DISTINCT c.cardType FROM CardCrossReference c ORDER BY c.cardType")
    List<String> findDistinctCardTypes();
    
    @Query("SELECT DISTINCT c.branchCode FROM CardCrossReference c WHERE c.branchCode IS NOT NULL ORDER BY c.branchCode")
    List<String> findDistinctBranchCodes();
    
    @Query("SELECT DISTINCT c.productCode FROM CardCrossReference c WHERE c.productCode IS NOT NULL ORDER BY c.productCode")
    List<String> findDistinctProductCodes();
    
    @Query("SELECT DISTINCT c.accountType FROM CardCrossReference c WHERE c.accountType IS NOT NULL ORDER BY c.accountType")
    List<String> findDistinctAccountTypes();
    
    @Query("SELECT DISTINCT c.deliveryMethod FROM CardCrossReference c WHERE c.deliveryMethod IS NOT NULL ORDER BY c.deliveryMethod")
    List<String> findDistinctDeliveryMethods();
    
    // Complex filtering
    @Query("SELECT c FROM CardCrossReference c WHERE " +
           "(:cardStatus IS NULL OR c.cardStatus = :cardStatus) AND " +
           "(:cardType IS NULL OR c.cardType = :cardType) AND " +
           "(:branchCode IS NULL OR c.branchCode = :branchCode) AND " +
           "(:customerId IS NULL OR c.customerId = :customerId) AND " +
           "(:accountNumber IS NULL OR c.accountNumber = :accountNumber) " +
           "ORDER BY c.cardNumber ASC")
    Page<CardCrossReference> findWithFilters(@Param("cardStatus") String cardStatus,
                                           @Param("cardType") String cardType,
                                           @Param("branchCode") String branchCode,
                                           @Param("customerId") String customerId,
                                           @Param("accountNumber") String accountNumber,
                                           Pageable pageable);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber AND " +
           "(:cardStatus IS NULL OR c.cardStatus = :cardStatus) AND " +
           "(:cardType IS NULL OR c.cardType = :cardType) AND " +
           "(:branchCode IS NULL OR c.branchCode = :branchCode) " +
           "ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findSequentialWithFilters(@Param("startingCardNumber") String startingCardNumber,
                                                      @Param("cardStatus") String cardStatus,
                                                      @Param("cardType") String cardType,
                                                      @Param("branchCode") String branchCode,
                                                      Pageable pageable);
    
    // Search functionality
    @Query("SELECT c FROM CardCrossReference c WHERE " +
           "c.cardNumber LIKE CONCAT(:cardNumberPattern, '%') OR " +
           "LOWER(c.embossName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "c.customerId LIKE CONCAT('%', :searchTerm, '%') OR " +
           "c.accountNumber LIKE CONCAT('%', :searchTerm, '%')")
    Page<CardCrossReference> findBySearchTerm(@Param("cardNumberPattern") String cardNumberPattern,
                                            @Param("searchTerm") String searchTerm,
                                            Pageable pageable);
    
    // Batch operations
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber IN :cardNumbers ORDER BY c.cardNumber ASC")
    List<CardCrossReference> findByCardNumberIn(@Param("cardNumbers") List<String> cardNumbers);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.customerId IN :customerIds ORDER BY c.customerId, c.cardNumber ASC")
    List<CardCrossReference> findByCustomerIdIn(@Param("customerIds") List<String> customerIds);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.accountNumber IN :accountNumbers ORDER BY c.accountNumber, c.cardNumber ASC")
    List<CardCrossReference> findByAccountNumberIn(@Param("accountNumbers") List<String> accountNumbers);
    
    // Count methods for sequential reading
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber")
    long countSequentialRecords(@Param("startingCardNumber") String startingCardNumber);
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber AND c.cardStatus = :cardStatus")
    long countSequentialRecordsByStatus(@Param("startingCardNumber") String startingCardNumber, @Param("cardStatus") String cardStatus);
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardNumber >= :startingCardNumber AND c.cardType = :cardType")
    long countSequentialRecordsByType(@Param("startingCardNumber") String startingCardNumber, @Param("cardType") String cardType);
    
    // Navigation methods
    @Query("SELECT MAX(c.cardNumber) FROM CardCrossReference c")
    Optional<String> findMaxCardNumber();
    
    @Query("SELECT MIN(c.cardNumber) FROM CardCrossReference c")
    Optional<String> findMinCardNumber();
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber > :cardNumber ORDER BY c.cardNumber ASC")
    Optional<CardCrossReference> findNextCard(@Param("cardNumber") String cardNumber);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardNumber < :cardNumber ORDER BY c.cardNumber DESC")
    Optional<CardCrossReference> findPreviousCard(@Param("cardNumber") String cardNumber);
}