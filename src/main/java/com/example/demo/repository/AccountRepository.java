package com.example.demo.repository;

import com.example.demo.entity.Account;
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

/**
 * Repository interface for Account entity.
 * Provides data access methods for account operations including
 * sequential processing, filtering, and business-specific queries.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    /**
     * Find account by account ID.
     * @param acctId the account identifier
     * @return Optional containing the account if found
     */
    Optional<Account> findByAcctId(Long acctId);
    
    /**
     * Check if an account exists by account ID.
     * @param acctId the account identifier
     * @return true if account exists, false otherwise
     */
    boolean existsByAcctId(Long acctId);
    
    /**
     * Find all accounts by active status.
     * Supports BR-001: Account File Sequential Processing
     * @param acctActiveStatus the active status ('A' or 'I')
     * @param pageable pagination information
     * @return page of accounts with the specified status
     */
    Page<Account> findByAcctActiveStatus(String acctActiveStatus, Pageable pageable);
    
    /**
     * Find all active accounts.
     * @param pageable pagination information
     * @return page of active accounts
     */
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus = 'A'")
    Page<Account> findAllActiveAccounts(Pageable pageable);
    
    /**
     * Find all inactive accounts.
     * @param pageable pagination information
     * @return page of inactive accounts
     */
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus = 'I'")
    Page<Account> findAllInactiveAccounts(Pageable pageable);
    
    /**
     * Find accounts by group ID.
     * @param acctGroupId the account group identifier
     * @param pageable pagination information
     * @return page of accounts in the specified group
     */
    Page<Account> findByAcctGroupId(String acctGroupId, Pageable pageable);
    
    /**
     * Find accounts expiring before a specific date.
     * @param expirationDate the cutoff date
     * @param pageable pagination information
     * @return page of accounts expiring before the date
     */
    @Query("SELECT a FROM Account a WHERE a.acctExpirationDate < :expirationDate")
    Page<Account> findAccountsExpiringBefore(@Param("expirationDate") LocalDate expirationDate, Pageable pageable);
    
    /**
     * Find accounts expiring within a date range.
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination information
     * @return page of accounts expiring within the range
     */
    @Query("SELECT a FROM Account a WHERE a.acctExpirationDate BETWEEN :startDate AND :endDate")
    Page<Account> findAccountsExpiringBetween(@Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate, 
                                               Pageable pageable);
    
    /**
     * Find accounts with balance exceeding credit limit.
     * @param pageable pagination information
     * @return page of accounts over their credit limit
     */
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > a.acctCreditLimit")
    Page<Account> findAccountsOverCreditLimit(Pageable pageable);
    
    /**
     * Find accounts with balance exceeding cash credit limit.
     * @param pageable pagination information
     * @return page of accounts over their cash credit limit
     */
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > a.acctCashCreditLimit")
    Page<Account> findAccountsOverCashCreditLimit(Pageable pageable);
    
    /**
     * Find accounts opened after a specific date.
     * @param openDate the cutoff date
     * @param pageable pagination information
     * @return page of accounts opened after the date
     */
    @Query("SELECT a FROM Account a WHERE a.acctOpenDate >= :openDate")
    Page<Account> findAccountsOpenedAfter(@Param("openDate") LocalDate openDate, Pageable pageable);
    
    /**
     * Find accounts with reissue date set.
     * @param pageable pagination information
     * @return page of reissued accounts
     */
    @Query("SELECT a FROM Account a WHERE a.acctReissueDate IS NOT NULL")
    Page<Account> findReissuedAccounts(Pageable pageable);
    
    /**
     * Find all accounts ordered by account ID for sequential processing.
     * Supports BR-001: Account File Sequential Processing
     * @return list of all accounts in sequential order
     */
    @Query("SELECT a FROM Account a ORDER BY a.acctId ASC")
    List<Account> findAllAccountsSequentially();
    
    /**
     * Count accounts by active status.
     * @param acctActiveStatus the active status
     * @return count of accounts with the status
     */
    long countByAcctActiveStatus(String acctActiveStatus);
    
    /**
     * Count active accounts.
     * @return count of active accounts
     */
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctActiveStatus = 'A'")
    long countActiveAccounts();
    
    /**
     * Count inactive accounts.
     * @return count of inactive accounts
     */
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctActiveStatus = 'I'")
    long countInactiveAccounts();
    
    /**
     * Calculate total balance across all accounts.
     * @return sum of all account balances
     */
    @Query("SELECT SUM(a.acctCurrBal) FROM Account a")
    BigDecimal calculateTotalBalance();
    
    /**
     * Calculate total balance for active accounts.
     * @return sum of active account balances
     */
    @Query("SELECT SUM(a.acctCurrBal) FROM Account a WHERE a.acctActiveStatus = 'A'")
    BigDecimal calculateTotalActiveBalance();
    
    /**
     * Calculate total credit limit across all accounts.
     * @return sum of all credit limits
     */
    @Query("SELECT SUM(a.acctCreditLimit) FROM Account a")
    BigDecimal calculateTotalCreditLimit();
    
    /**
     * Find accounts by group ID (null-safe).
     * @param acctGroupId the account group identifier (can be null)
     * @return list of accounts in the group
     */
    List<Account> findByAcctGroupIdOrAcctGroupIdIsNull(String acctGroupId);
}
