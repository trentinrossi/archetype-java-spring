package com.example.demo.repository;

import com.example.demo.entity.CreditCard;
import com.example.demo.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CreditCard entity
 * Business Rule BR004: Credit card records can be filtered by account ID and/or card number.
 * Business Rule BR008: Records that do not match the specified filter criteria are excluded from display.
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    
    /**
     * Find credit card by card number
     */
    Optional<CreditCard> findByCardNumber(String cardNumber);
    
    /**
     * Check if credit card exists by card number
     */
    boolean existsByCardNumber(String cardNumber);
    
    /**
     * Find credit cards by account ID
     * Business Rule BR004: Filter by account ID
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId")
    Page<CreditCard> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    
    /**
     * Find credit cards by account ID and card number
     * Business Rule BR004: Filters are applied cumulatively if both are specified
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId AND c.cardNumber = :cardNumber")
    Page<CreditCard> findByAccountIdAndCardNumber(
            @Param("accountId") String accountId, 
            @Param("cardNumber") String cardNumber, 
            Pageable pageable);
    
    /**
     * Find credit cards by card number only
     * Business Rule BR004: Filter by card number
     */
    Page<CreditCard> findByCardNumber(String cardNumber, Pageable pageable);
    
    /**
     * Find all credit cards with pagination
     * Business Rule BR001: Admin users can view all credit cards when no context is passed
     * Business Rule BR002: Maximum of 7 records per page
     */
    Page<CreditCard> findAll(Pageable pageable);
    
    /**
     * Find credit cards by status
     */
    Page<CreditCard> findByCardStatus(CardStatus cardStatus, Pageable pageable);
    
    /**
     * Find credit cards by account ID and status
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId AND c.cardStatus = :status")
    Page<CreditCard> findByAccountIdAndStatus(
            @Param("accountId") String accountId, 
            @Param("status") CardStatus status, 
            Pageable pageable);
    
    /**
     * Find credit cards accessible by user
     * Business Rule BR001: Non-admin users can only view cards associated with their specific account
     */
    @Query("SELECT c FROM CreditCard c JOIN c.account a JOIN a.users u WHERE u.userId = :userId")
    Page<CreditCard> findCreditCardsByUserId(@Param("userId") String userId, Pageable pageable);
    
    /**
     * Find credit cards accessible by user with account filter
     * Business Rule BR001 + BR004: Combined user access and account filter
     */
    @Query("SELECT c FROM CreditCard c JOIN c.account a JOIN a.users u " +
           "WHERE u.userId = :userId AND a.accountId = :accountId")
    Page<CreditCard> findCreditCardsByUserIdAndAccountId(
            @Param("userId") String userId, 
            @Param("accountId") String accountId, 
            Pageable pageable);
    
    /**
     * Count credit cards by account ID
     */
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.account.accountId = :accountId")
    Long countByAccountId(@Param("accountId") String accountId);
    
    /**
     * Count active credit cards by account ID
     */
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.account.accountId = :accountId AND c.cardStatus = 'ACTIVE'")
    Long countActiveCardsByAccountId(@Param("accountId") String accountId);
    
    /**
     * Find credit cards by account IDs (for batch operations)
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId IN :accountIds")
    List<CreditCard> findByAccountIdIn(@Param("accountIds") List<String> accountIds);
}
