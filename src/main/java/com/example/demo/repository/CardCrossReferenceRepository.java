package com.example.demo.repository;

import com.example.demo.entity.CardCrossReference;
import com.example.demo.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CardCrossReference entity
 * Provides data access methods for card cross reference operations
 */
@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, String> {
    
    /**
     * Find card cross references by customer ID
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.customer.customerId = :customerId")
    List<CardCrossReference> findByCustomerId(@Param("customerId") String customerId);
    
    /**
     * Find card cross references by customer ID with pagination
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.customer.customerId = :customerId")
    Page<CardCrossReference> findByCustomerId(@Param("customerId") String customerId, Pageable pageable);
    
    /**
     * Find card cross references by account ID
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.account.accountId = :accountId")
    List<CardCrossReference> findByAccountId(@Param("accountId") String accountId);
    
    /**
     * Find card cross references by account ID with pagination
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.account.accountId = :accountId")
    Page<CardCrossReference> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    
    /**
     * Find card cross reference by customer and account
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.customer.customerId = :customerId " +
           "AND c.account.accountId = :accountId")
    List<CardCrossReference> findByCustomerIdAndAccountId(@Param("customerId") String customerId,
                                                           @Param("accountId") String accountId);
    
    /**
     * Find card cross references by card status
     */
    Page<CardCrossReference> findByCardStatus(CardStatus cardStatus, Pageable pageable);
    
    /**
     * Find active cards
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.cardStatus = 'ACTIVE'")
    Page<CardCrossReference> findActiveCards(Pageable pageable);
    
    /**
     * Find primary cards
     */
    Page<CardCrossReference> findByIsPrimaryCardTrue(Pageable pageable);
    
    /**
     * Find primary card for account
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.account.accountId = :accountId " +
           "AND c.isPrimaryCard = true")
    Optional<CardCrossReference> findPrimaryCardByAccountId(@Param("accountId") String accountId);
    
    /**
     * Find cards by card type
     */
    Page<CardCrossReference> findByCardType(String cardType, Pageable pageable);
    
    /**
     * Find cards expiring before date
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.expirationDate < :date")
    List<CardCrossReference> findExpiringBefore(@Param("date") LocalDate date);
    
    /**
     * Find cards expiring in date range
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.expirationDate BETWEEN :startDate AND :endDate")
    List<CardCrossReference> findExpiringBetween(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);
    
    /**
     * Find expired cards
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.expirationDate < CURRENT_DATE")
    Page<CardCrossReference> findExpiredCards(Pageable pageable);
    
    /**
     * Find cards with PIN set
     */
    Page<CardCrossReference> findByPinSetTrue(Pageable pageable);
    
    /**
     * Find cards without PIN set
     */
    Page<CardCrossReference> findByPinSetFalse(Pageable pageable);
    
    /**
     * Find cards issued in date range
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.issueDate BETWEEN :startDate AND :endDate")
    List<CardCrossReference> findIssuedBetween(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
    
    /**
     * Find cards activated in date range
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.activationDate BETWEEN :startDate AND :endDate")
    List<CardCrossReference> findActivatedBetween(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);
    
    /**
     * Find cards by last used date
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.lastUsedDate >= :since")
    List<CardCrossReference> findUsedSince(@Param("since") LocalDate since);
    
    /**
     * Find inactive cards (not used recently)
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.lastUsedDate < :date OR c.lastUsedDate IS NULL")
    Page<CardCrossReference> findInactiveCards(@Param("date") LocalDate date, Pageable pageable);
    
    /**
     * Find replacement cards
     */
    @Query("SELECT c FROM CardCrossReference c WHERE c.replacementCardNumber IS NOT NULL")
    List<CardCrossReference> findReplacementCards();
    
    /**
     * Find card by replacement card number
     */
    Optional<CardCrossReference> findByReplacementCardNumber(String replacementCardNumber);
    
    /**
     * Find card by replaced card number
     */
    Optional<CardCrossReference> findByReplacedCardNumber(String replacedCardNumber);
    
    /**
     * Count cards by customer ID
     */
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.customer.customerId = :customerId")
    long countByCustomerId(@Param("customerId") String customerId);
    
    /**
     * Count cards by account ID
     */
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.account.accountId = :accountId")
    long countByAccountId(@Param("accountId") String accountId);
    
    /**
     * Count cards by status
     */
    long countByCardStatus(CardStatus cardStatus);
    
    /**
     * Count active cards
     */
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.cardStatus = 'ACTIVE'")
    long countActiveCards();
    
    /**
     * Count expired cards
     */
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.expirationDate < CURRENT_DATE")
    long countExpiredCards();
    
    /**
     * Check if card number exists
     */
    boolean existsByCardNumber(String cardNumber);
}
