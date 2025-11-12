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

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    Optional<Account> findAccountByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.xrefAcctId = :xrefAcctId")
    Optional<Account> findByXrefAcctId(@Param("xrefAcctId") Long xrefAcctId);
    
    boolean existsByXrefAcctId(Long xrefAcctId);
    
    List<Account> findByActiveStatus(String activeStatus);
    
    Page<Account> findByActiveStatus(String activeStatus, Pageable pageable);
    
    List<Account> findByAccountStatus(String accountStatus);
    
    Page<Account> findByAccountStatus(String accountStatus, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = :status ORDER BY a.accountId")
    List<Account> findAllByActiveStatusOrderByAccountId(@Param("status") String status);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :balance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > 0")
    List<Account> findAccountsWithPositiveBalance();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance <= 0")
    List<Account> findAccountsWithZeroOrNegativeBalance();
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit > :limit")
    List<Account> findAccountsWithCreditLimitGreaterThan(@Param("limit") BigDecimal limit);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    List<Account> findAccountsOverCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate < :date")
    List<Account> findExpiredAccounts(@Param("date") String date);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate >= :startDate AND a.expirationDate <= :endDate")
    List<Account> findAccountsExpiringBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    @Query("SELECT a FROM Account a WHERE a.openDate >= :date")
    List<Account> findAccountsOpenedSince(@Param("date") String date);
    
    @Query("SELECT a FROM Account a WHERE a.openDate <= :date")
    List<Account> findAccountsOpenedBefore(@Param("date") String date);
    
    List<Account> findByGroupId(String groupId);
    
    Page<Account> findByGroupId(String groupId, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.groupId = :groupId AND a.activeStatus = :status")
    List<Account> findByGroupIdAndActiveStatus(@Param("groupId") String groupId, @Param("status") String status);
    
    long countByActiveStatus(String activeStatus);
    
    long countByAccountStatus(String accountStatus);
    
    long countByGroupId(String groupId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.currentBalance > 0")
    long countAccountsWithPositiveBalance();
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.expirationDate < :date")
    long countExpiredAccounts(@Param("date") String date);
    
    @Query("SELECT a FROM Account a WHERE a.accountId >= :startId AND a.accountId <= :endId ORDER BY a.accountId")
    List<Account> findAccountsInRange(@Param("startId") Long startId, @Param("endId") Long endId);
    
    @Query("SELECT a FROM Account a ORDER BY a.accountId")
    List<Account> findAllOrderedByAccountId();
    
    @Query("SELECT a FROM Account a ORDER BY a.accountId")
    Page<Account> findAllOrderedByAccountId(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit > 0 OR a.currentCycleDebit > 0")
    List<Account> findAccountsWithCurrentCycleActivity();
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit > :amount")
    List<Account> findAccountsWithCycleCredit(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleDebit > :amount")
    List<Account> findAccountsWithCycleDebit(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit > 0")
    List<Account> findAccountsWithCashCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate IS NOT NULL")
    List<Account> findAccountsWithReissueDate();
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate >= :date")
    List<Account> findAccountsReissuedSince(@Param("date") String date);
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.activeStatus = :status")
    BigDecimal sumCurrentBalanceByActiveStatus(@Param("status") String status);
    
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.activeStatus = :status")
    BigDecimal sumCreditLimitByActiveStatus(@Param("status") String status);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.activeStatus = 'Y'")
    Optional<Account> findActiveAccountByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.accountStatus = 'Y'")
    Optional<Account> findActiveAccountByAccountIdUsingStatus(@Param("accountId") Long accountId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.accountId = :accountId AND a.activeStatus = 'Y'")
    boolean isAccountActive(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId <> 0 AND a.accountId IS NOT NULL")
    List<Account> findAllNonZeroAccounts();
    
    @Query("SELECT a FROM Account a WHERE " +
           "(:accountId IS NULL OR a.accountId = :accountId) AND " +
           "(:status IS NULL OR a.activeStatus = :status) AND " +
           "(:groupId IS NULL OR a.groupId = :groupId)")
    Page<Account> findAccountsByFilters(
        @Param("accountId") Long accountId,
        @Param("status") String status,
        @Param("groupId") String groupId,
        Pageable pageable
    );
    
    @Query("SELECT a FROM Account a WHERE " +
           "a.accountId = :accountId AND " +
           "a.currentBalance > 0")
    Optional<Account> findAccountWithPositiveBalance(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.groupId = :groupId ORDER BY a.currentBalance DESC")
    List<Account> findAccountsByGroupOrderedByBalance(@Param("groupId") String groupId);
    
    @Query("SELECT DISTINCT a.groupId FROM Account a WHERE a.groupId IS NOT NULL ORDER BY a.groupId")
    List<String> findAllDistinctGroupIds();
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findAccountsByAccountIdIn(@Param("accountIds") List<Long> accountIds);
}
