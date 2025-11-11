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
 * CreditCardRepository
 * 
 * Data access layer for CreditCard entity.
 * Provides methods for querying and managing credit cards with filtering and pagination.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR002: Card Number Filter Validation
 * - BR004: Account Filter Validation
 * - BR005: Card Status Filter Validation
 * - BR006: Filter Record Matching
 * - BR014: Forward Pagination
 * - BR015: Backward Pagination
 */
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    
    /**
     * Finds a credit card by card number
     * Implements BR002: Card Number Filter Validation
     * 
     * @param cardNumber The card number to search for
     * @return Optional containing the credit card if found
     */
    Optional<CreditCard> findByCardNumber(String cardNumber);
    
    /**
     * Checks if a credit card exists by card number
     * Implements BR002: Card Number Filter Validation
     * 
     * @param cardNumber The card number to check
     * @return true if card exists, false otherwise
     */
    boolean existsByCardNumber(String cardNumber);
    
    /**
     * Finds credit cards by account ID with pagination
     * Implements BR004: Account Filter Validation
     * Implements BR014, BR015: Pagination
     * 
     * @param accountId The account ID to filter by
     * @param pageable Pagination information
     * @return Page of credit cards for the account
     */
    Page<CreditCard> findByAccountId(String accountId, Pageable pageable);
    
    /**
     * Finds credit cards by card status with pagination
     * Implements BR005: Card Status Filter Validation
     * Implements BR014, BR015: Pagination
     * 
     * @param cardStatus The card status to filter by
     * @param pageable Pagination information
     * @return Page of credit cards with the specified status
     */
    @Query("SELECT c FROM CreditCard c WHERE c.cardStatus = :cardStatus")
    Page<CreditCard> findByCardStatus(@Param("cardStatus") char cardStatus, Pageable pageable);
    
    /**
     * Finds credit cards by multiple filters with pagination
     * Implements BR006: Filter Record Matching
     * Implements BR014, BR015: Pagination
     * 
     * All supplied filter criteria must match for records to be displayed.
     * 
     * @param cardNumber The card number filter (optional)
     * @param accountId The account ID filter (optional)
     * @param cardStatus The card status filter (optional)
     * @param pageable Pagination information
     * @return Page of credit cards matching all supplied filters
     */
    @Query("SELECT c FROM CreditCard c WHERE " +
           "(:cardNumber IS NULL OR c.cardNumber = :cardNumber) AND " +
           "(:accountId IS NULL OR c.accountId = :accountId) AND " +
           "(:cardStatus IS NULL OR c.cardStatus = :cardStatus)")
    Page<CreditCard> findByFilters(@Param("cardNumber") String cardNumber,
                                    @Param("accountId") String accountId,
                                    @Param("cardStatus") Character cardStatus,
                                    Pageable pageable);
    
    /**
     * Finds credit cards by account IDs with pagination
     * Implements BR001: User Permission Based Card Access
     * Implements BR014, BR015: Pagination
     * 
     * Used for regular users who can only view cards for their accessible accounts.
     * 
     * @param accountIds List of accessible account IDs
     * @param pageable Pagination information
     * @return Page of credit cards for the specified accounts
     */
    @Query("SELECT c FROM CreditCard c WHERE c.accountId IN :accountIds")
    Page<CreditCard> findByAccountIdIn(@Param("accountIds") List<String> accountIds, Pageable pageable);
    
    /**
     * Finds credit cards by account IDs and additional filters with pagination
     * Implements BR001: User Permission Based Card Access
     * Implements BR006: Filter Record Matching
     * Implements BR014, BR015: Pagination
     * 
     * Used for regular users with additional filter criteria.
     * 
     * @param accountIds List of accessible account IDs
     * @param cardNumber The card number filter (optional)
     * @param cardStatus The card status filter (optional)
     * @param pageable Pagination information
     * @return Page of credit cards matching all criteria
     */
    @Query("SELECT c FROM CreditCard c WHERE c.accountId IN :accountIds AND " +
           "(:cardNumber IS NULL OR c.cardNumber = :cardNumber) AND " +
           "(:cardStatus IS NULL OR c.cardStatus = :cardStatus)")
    Page<CreditCard> findByAccountIdInAndFilters(@Param("accountIds") List<String> accountIds,
                                                   @Param("cardNumber") String cardNumber,
                                                   @Param("cardStatus") Character cardStatus,
                                                   Pageable pageable);
    
    /**
     * Counts credit cards by card status
     * Implements BR005: Card Status Filter Validation
     * 
     * @param cardStatus The card status to count
     * @return Number of credit cards with the specified status
     */
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.cardStatus = :cardStatus")
    long countByCardStatus(@Param("cardStatus") char cardStatus);
    
    /**
     * Counts credit cards by account ID
     * Implements BR004: Account Filter Validation
     * 
     * @param accountId The account ID to count
     * @return Number of credit cards for the account
     */
    long countByAccountId(String accountId);
    
    /**
     * Counts credit cards by account IDs
     * Implements BR001: User Permission Based Card Access
     * 
     * @param accountIds List of account IDs
     * @return Number of credit cards for the specified accounts
     */
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.accountId IN :accountIds")
    long countByAccountIdIn(@Param("accountIds") List<String> accountIds);
    
    /**
     * Finds all credit cards by account IDs
     * Implements BR001: User Permission Based Card Access
     * 
     * @param accountIds List of account IDs
     * @return List of credit cards for the specified accounts
     */
    List<CreditCard> findByAccountIdIn(List<String> accountIds);
    
    /**
     * Finds distinct account IDs by card number
     * 
     * @param cardNumber The card number to search for
     * @return List of account IDs associated with the card number
     */
    @Query("SELECT DISTINCT c.accountId FROM CreditCard c WHERE c.cardNumber = :cardNumber")
    List<String> findAccountIdsByCardNumber(@Param("cardNumber") String cardNumber);
    
    /**
     * Finds credit cards by card number starting with prefix
     * Implements BR002: Card Number Filter Validation
     * 
     * @param cardNumber The card number prefix
     * @param pageable Pagination information
     * @return Page of credit cards with card numbers starting with the prefix
     */
    @Query("SELECT c FROM CreditCard c WHERE c.cardNumber LIKE CONCAT(:cardNumber, '%')")
    Page<CreditCard> findByCardNumberStartingWith(@Param("cardNumber") String cardNumber, Pageable pageable);
    
    /**
     * Finds credit cards by account ID starting with prefix
     * Implements BR004: Account Filter Validation
     * 
     * @param accountId The account ID prefix
     * @param pageable Pagination information
     * @return Page of credit cards with account IDs starting with the prefix
     */
    @Query("SELECT c FROM CreditCard c WHERE c.accountId LIKE CONCAT(:accountId, '%')")
    Page<CreditCard> findByAccountIdStartingWith(@Param("accountId") String accountId, Pageable pageable);
}
