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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    // Basic finder methods
    Optional<Account> findByAcctId(String acctId);
    
    boolean existsByAcctId(String acctId);
    
    Page<Account> findByAcctActiveStatus(String acctActiveStatus, Pageable pageable);
    
    Page<Account> findByAcctGroupId(String acctGroupId, Pageable pageable);
    
    // Sequential reading methods based on CBACT01C functionality
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyFromStarting(@Param("startingAcctId") String startingAcctId, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctActiveStatus = :status ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByStatus(@Param("startingAcctId") String startingAcctId, @Param("status") String status, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctGroupId = :groupId ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByGroup(@Param("startingAcctId") String startingAcctId, @Param("groupId") String groupId, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctActiveStatus = :status AND a.acctGroupId = :groupId ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByStatusAndGroup(@Param("startingAcctId") String startingAcctId, @Param("status") String status, @Param("groupId") String groupId, Pageable pageable);
    
    // Balance-based queries
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal >= :minBalance AND a.acctCurrBal <= :maxBalance")
    Page<Account> findByCurrentBalanceBetween(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctCreditLimit >= :minLimit AND a.acctCreditLimit <= :maxLimit")
    Page<Account> findByCreditLimitBetween(@Param("minLimit") BigDecimal minLimit, @Param("maxLimit") BigDecimal maxLimit, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctCashCreditLimit >= :minLimit AND a.acctCashCreditLimit <= :maxLimit")
    Page<Account> findByCashCreditLimitBetween(@Param("minLimit") BigDecimal minLimit, @Param("maxLimit") BigDecimal maxLimit, Pageable pageable);
    
    // Date-based queries
    @Query("SELECT a FROM Account a WHERE a.acctOpenDate >= :fromDate AND a.acctOpenDate <= :toDate")
    Page<Account> findByOpenDateBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctExpirationDate >= :fromDate AND a.acctExpirationDate <= :toDate")
    Page<Account> findByExpirationDateBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctExpirationDate <= :date")
    List<Account> findExpiringAccounts(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.acctReissueDate IS NOT NULL AND a.acctReissueDate >= :since")
    List<Account> findRecentlyReissuedAccounts(@Param("since") LocalDate since);
    
    // Cycle amount queries
    @Query("SELECT a FROM Account a WHERE a.acctCurrCycCredit >= :minCredit")
    Page<Account> findByCurrentCycleCreditGreaterThanEqual(@Param("minCredit") BigDecimal minCredit, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrCycDebit >= :minDebit")
    Page<Account> findByCurrentCycleDebitGreaterThanEqual(@Param("minDebit") BigDecimal minDebit, Pageable pageable);
    
    // Status-based queries
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus IN :statuses")
    Page<Account> findByAcctActiveStatusIn(@Param("statuses") List<String> statuses, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctGroupId IN :groupIds")
    Page<Account> findByAcctGroupIdIn(@Param("groupIds") List<String> groupIds, Pageable pageable);
    
    // Over-limit accounts
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > a.acctCreditLimit")
    List<Account> findOverLimitAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > a.acctCashCreditLimit")
    List<Account> findOverCashLimitAccounts();
    
    // Active accounts
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus IN ('Y', 'A')")
    Page<Account> findActiveAccounts(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus NOT IN ('Y', 'A')")
    Page<Account> findInactiveAccounts(Pageable pageable);
    
    // Count methods
    long countByAcctActiveStatus(String acctActiveStatus);
    
    long countByAcctGroupId(String acctGroupId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctActiveStatus = :status AND a.acctGroupId = :groupId")
    long countByAcctActiveStatusAndAcctGroupId(@Param("status") String status, @Param("groupId") String groupId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctId >= :startingAcctId")
    long countAccountsFromStarting(@Param("startingAcctId") String startingAcctId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctActiveStatus = :status")
    long countAccountsFromStartingByStatus(@Param("startingAcctId") String startingAcctId, @Param("status") String status);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctGroupId = :groupId")
    long countAccountsFromStartingByGroup(@Param("startingAcctId") String startingAcctId, @Param("groupId") String groupId);
    
    // Distinct values
    @Query("SELECT DISTINCT a.acctActiveStatus FROM Account a ORDER BY a.acctActiveStatus")
    List<String> findDistinctActiveStatuses();
    
    @Query("SELECT DISTINCT a.acctGroupId FROM Account a WHERE a.acctGroupId IS NOT NULL ORDER BY a.acctGroupId")
    List<String> findDistinctGroupIds();
    
    // Navigation methods
    @Query("SELECT MAX(a.acctId) FROM Account a")
    Optional<String> findMaxAcctId();
    
    @Query("SELECT MIN(a.acctId) FROM Account a")
    Optional<String> findMinAcctId();
    
    @Query("SELECT a FROM Account a WHERE a.acctId > :acctId ORDER BY a.acctId ASC")
    Optional<Account> findNextAccount(@Param("acctId") String acctId);
    
    @Query("SELECT a FROM Account a WHERE a.acctId < :acctId ORDER BY a.acctId DESC")
    Optional<Account> findPreviousAccount(@Param("acctId") String acctId);
    
    // Sequential reading with filters for CBACT01C-like functionality
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctCurrBal >= :minBalance ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByMinimumBalance(@Param("startingAcctId") String startingAcctId, @Param("minBalance") BigDecimal minBalance, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctCreditLimit >= :minLimit ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByMinimumCreditLimit(@Param("startingAcctId") String startingAcctId, @Param("minLimit") BigDecimal minLimit, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctOpenDate >= :fromDate ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByOpenDateFrom(@Param("startingAcctId") String startingAcctId, @Param("fromDate") LocalDate fromDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctExpirationDate <= :toDate ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByExpirationDateTo(@Param("startingAcctId") String startingAcctId, @Param("toDate") LocalDate toDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctActiveStatus IN :statuses ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByStatusIn(@Param("startingAcctId") String startingAcctId, @Param("statuses") List<String> statuses, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctId >= :startingAcctId AND a.acctGroupId IN :groupIds ORDER BY a.acctId ASC")
    List<Account> findAccountsSequentiallyByGroupIn(@Param("startingAcctId") String startingAcctId, @Param("groupIds") List<String> groupIds, Pageable pageable);
}