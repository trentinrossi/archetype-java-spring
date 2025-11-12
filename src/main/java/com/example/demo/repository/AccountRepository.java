package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    Optional<Account> findByAccountId(String accountId);
    
    boolean existsByAccountId(String accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.accountId <> '00000000000'")
    Optional<Account> findByAccountIdNotZero(@Param("accountId") String accountId);
    
    Page<Account> findByActiveStatus(String activeStatus, Pageable pageable);
    
    List<Account> findByActiveStatus(String activeStatus);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus IN ('Y', 'N')")
    List<Account> findAllWithValidStatus();
    
    Page<Account> findByGroupId(String groupId, Pageable pageable);
    
    List<Account> findByGroupId(String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :minBalance")
    List<Account> findByCurrentBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > 0")
    List<Account> findAccountsWithPositiveBalance();
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit >= :amount")
    List<Account> findByCreditLimitGreaterThanOrEqual(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit >= :amount")
    List<Account> findByCashCreditLimitGreaterThanOrEqual(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit > 0 OR a.currentCycleDebit > 0")
    List<Account> findAccountsWithCurrentCycleActivity();
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit >= :amount")
    List<Account> findByCurrentCycleCreditGreaterThanOrEqual(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleDebit >= :amount")
    List<Account> findByCurrentCycleDebitGreaterThanOrEqual(@Param("amount") BigDecimal amount);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.activeStatus = :status")
    long countByActiveStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.groupId = :groupId")
    long countByGroupId(@Param("groupId") String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = 'Y'")
    List<Account> findAllActiveAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = 'N'")
    List<Account> findAllInactiveAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.groupId IS NOT NULL")
    List<Account> findAccountsWithGroupId();
    
    @Query("SELECT a FROM Account a WHERE a.groupId IS NULL")
    List<Account> findAccountsWithoutGroupId();
    
    @Query("SELECT a FROM Account a ORDER BY a.accountId ASC")
    List<Account> findAllOrderedByAccountIdAsc();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance BETWEEN :minBalance AND :maxBalance")
    List<Account> findByCurrentBalanceBetween(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit > a.currentBalance")
    List<Account> findAccountsWithAvailableCredit();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance >= a.creditLimit")
    List<Account> findAccountsOverCreditLimit();
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCurrentBalanceForActiveAccounts();
    
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCreditLimitForActiveAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findByAccountIdIn(@Param("accountIds") List<String> accountIds);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = :status AND a.groupId = :groupId")
    List<Account> findByActiveStatusAndGroupId(@Param("status") String status, @Param("groupId") String groupId);
    
    @Query("SELECT DISTINCT a.groupId FROM Account a WHERE a.groupId IS NOT NULL")
    List<String> findDistinctGroupIds();
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit = 0 AND a.currentCycleDebit = 0")
    List<Account> findAccountsWithNoCycleActivity();
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit > 0")
    List<Account> findAccountsWithCashCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.activeStatus = 'Y'")
    Optional<Account> findActiveAccountByAccountId(@Param("accountId") String accountId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.accountId = :accountId AND a.accountId <> '00000000000'")
    boolean existsByAccountIdAndNotZero(@Param("accountId") String accountId);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > 0 AND a.activeStatus = 'Y'")
    List<Account> findActiveAccountsWithPositiveBalance();
}
