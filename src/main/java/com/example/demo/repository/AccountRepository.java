package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AccountRepository
 * 
 * Data access layer for Account entity.
 * Provides methods for querying and managing customer accounts.
 * 
 * Business Rules Implemented:
 * - BR001: User Permission Based Card Access
 * - BR004: Account Filter Validation
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    /**
     * Finds an account by account ID
     * 
     * @param accountId The account ID to search for
     * @return Optional containing the account if found
     */
    Optional<Account> findByAccountId(String accountId);
    
    /**
     * Checks if an account exists by account ID
     * 
     * @param accountId The account ID to check
     * @return true if account exists, false otherwise
     */
    boolean existsByAccountId(String accountId);
    
    /**
     * Finds accounts by a list of account IDs
     * Implements BR001: User Permission Based Card Access
     * 
     * @param accountIds List of account IDs
     * @return List of accounts
     */
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findAllByAccountIdIn(@Param("accountIds") List<String> accountIds);
    
    /**
     * Finds all accounts with pagination
     * 
     * @param pageable Pagination information
     * @return Page of accounts
     */
    Page<Account> findAll(Pageable pageable);
    
    /**
     * Counts accounts by account ID
     * 
     * @param accountId The account ID to count
     * @return Number of accounts with the given ID
     */
    long countByAccountId(String accountId);
    
    /**
     * Finds accounts accessible by a specific user
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @param pageable Pagination information
     * @return Page of accounts accessible by the user
     */
    @Query("SELECT a FROM Account a JOIN a.users u WHERE u.userId = :userId")
    Page<Account> findAccountsAccessibleByUser(@Param("userId") String userId, Pageable pageable);
    
    /**
     * Checks if a user has access to a specific account
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @param accountId The account ID
     * @return true if user has access, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM Account a JOIN a.users u " +
           "WHERE u.userId = :userId AND a.accountId = :accountId")
    boolean userHasAccessToAccount(@Param("userId") String userId, @Param("accountId") String accountId);
    
    /**
     * Finds all account IDs accessible by a specific user
     * Implements BR001: User Permission Based Card Access
     * 
     * @param userId The user ID
     * @return List of accessible account IDs
     */
    @Query("SELECT a.accountId FROM Account a JOIN a.users u WHERE u.userId = :userId")
    List<String> findAccountIdsByUserId(@Param("userId") String userId);
}
