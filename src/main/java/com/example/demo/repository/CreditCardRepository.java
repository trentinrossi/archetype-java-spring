package com.example.demo.repository;

import com.example.demo.entity.CreditCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CreditCard entity.
 * Provides data access methods for credit card operations.
 * Implements business rules for filtering and querying cards.
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    
    /**
     * Find credit card by card number
     * @param cardNumber the 16-digit card number
     * @return Optional containing the credit card if found
     */
    Optional<CreditCard> findByCardNumber(String cardNumber);
    
    /**
     * Check if credit card exists by card number
     * @param cardNumber the 16-digit card number
     * @return true if card exists
     */
    boolean existsByCardNumber(String cardNumber);
    
    /**
     * Find all credit cards for a specific account
     * BR001: Non-admin users can only view cards associated with their specific account
     * @param accountId the account identifier
     * @param pageable pagination information
     * @return page of credit cards for the account
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId")
    Page<CreditCard> findByAccountId(@Param("accountId") String accountId, Pageable pageable);
    
    /**
     * Find credit cards by account ID (list version)
     * @param accountId the account identifier
     * @return list of credit cards for the account
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId")
    List<CreditCard> findByAccountIdList(@Param("accountId") String accountId);
    
    /**
     * Find credit cards by status
     * @param cardStatus the status code
     * @param pageable pagination information
     * @return page of credit cards with the specified status
     */
    Page<CreditCard> findByCardStatus(String cardStatus, Pageable pageable);
    
    /**
     * Find credit cards by account ID and status
     * BR007: Records are filtered based on account ID and/or card number criteria
     * @param accountId the account identifier
     * @param cardStatus the status code
     * @param pageable pagination information
     * @return page of credit cards matching both criteria
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId AND c.cardStatus = :cardStatus")
    Page<CreditCard> findByAccountIdAndCardStatus(@Param("accountId") String accountId, 
                                                   @Param("cardStatus") String cardStatus, 
                                                   Pageable pageable);
    
    /**
     * Find credit cards by card number pattern
     * BR007: Records are filtered based on card number criteria when specified
     * @param cardNumberPattern the card number pattern to search
     * @param pageable pagination information
     * @return page of credit cards matching the pattern
     */
    @Query("SELECT c FROM CreditCard c WHERE c.cardNumber LIKE :cardNumberPattern")
    Page<CreditCard> findByCardNumberPattern(@Param("cardNumberPattern") String cardNumberPattern, 
                                             Pageable pageable);
    
    /**
     * Find credit cards by account ID and card number pattern
     * BR007: Both filters can be applied simultaneously
     * @param accountId the account identifier
     * @param cardNumberPattern the card number pattern to search
     * @param pageable pagination information
     * @return page of credit cards matching both criteria
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId AND c.cardNumber LIKE :cardNumberPattern")
    Page<CreditCard> findByAccountIdAndCardNumberPattern(@Param("accountId") String accountId,
                                                          @Param("cardNumberPattern") String cardNumberPattern,
                                                          Pageable pageable);
    
    /**
     * Count credit cards by account ID
     * @param accountId the account identifier
     * @return count of credit cards for the account
     */
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.account.accountId = :accountId")
    long countByAccountId(@Param("accountId") String accountId);
    
    /**
     * Count credit cards by status
     * @param cardStatus the status code
     * @return count of credit cards with the specified status
     */
    long countByCardStatus(String cardStatus);
    
    /**
     * Find active credit cards for an account
     * @param accountId the account identifier
     * @return list of active credit cards
     */
    @Query("SELECT c FROM CreditCard c WHERE c.account.accountId = :accountId AND c.cardStatus = 'A'")
    List<CreditCard> findActiveCardsByAccountId(@Param("accountId") String accountId);
    
    /**
     * Find credit cards by cardholder name
     * @param cardholderName the name to search
     * @param pageable pagination information
     * @return page of credit cards
     */
    @Query("SELECT c FROM CreditCard c WHERE LOWER(c.cardholderName) LIKE LOWER(CONCAT('%', :cardholderName, '%'))")
    Page<CreditCard> findByCardholderNameContaining(@Param("cardholderName") String cardholderName, 
                                                     Pageable pageable);
    
    /**
     * Find expired credit cards
     * @return list of expired credit cards
     */
    @Query("SELECT c FROM CreditCard c WHERE " +
           "CAST(c.expiryYear AS int) < YEAR(CURRENT_DATE) OR " +
           "(CAST(c.expiryYear AS int) = YEAR(CURRENT_DATE) AND CAST(c.expiryMonth AS int) < MONTH(CURRENT_DATE))")
    List<CreditCard> findExpiredCards();
}
