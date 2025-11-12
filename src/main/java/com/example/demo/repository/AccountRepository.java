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
    
    Page<Account> findByActiveStatus(String activeStatus, Pageable pageable);
    
    List<Account> findByActiveStatus(String activeStatus);
    
    Optional<Account> findByGroupId(String groupId);
    
    Page<Account> findByGroupId(String groupId, Pageable pageable);
    
    List<Account> findByGroupId(String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :balance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > 0")
    List<Account> findAccountsWithBalanceGreaterThanZero();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > 0")
    Page<Account> findAccountsWithBalanceGreaterThanZero(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate < :currentDate")
    List<Account> findExpiredAccounts(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate < :currentDate")
    Page<Account> findExpiredAccounts(@Param("currentDate") LocalDate currentDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate <= :expirationDate AND a.activeStatus = :status")
    List<Account> findByExpirationDateBeforeAndActiveStatus(@Param("expirationDate") LocalDate expirationDate, @Param("status") String status);
    
    @Query("SELECT a FROM Account a WHERE a.openDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsByOpenDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.openDate BETWEEN :startDate AND :endDate")
    Page<Account> findAccountsByOpenDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.openDate >= :startDate")
    List<Account> findAccountsByOpenDateAfter(@Param("startDate") LocalDate startDate);
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsByReissueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.activeStatus = :status")
    long countByActiveStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.groupId = :groupId")
    long countByGroupId(@Param("groupId") String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.activeStatus = 'Y'")
    Optional<Account> findActiveAccountByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit > :limit")
    List<Account> findAccountsByCreditLimitGreaterThan(@Param("limit") BigDecimal limit);
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit > :limit")
    List<Account> findAccountsByCashCreditLimitGreaterThan(@Param("limit") BigDecimal limit);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance < 0")
    List<Account> findAccountsWithNegativeBalance();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    List<Account> findAccountsOverCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit > 0 OR a.currentCycleDebit > 0")
    List<Account> findAccountsWithCurrentCycleActivity();
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit > 0 OR a.currentCycleDebit > 0")
    Page<Account> findAccountsWithCurrentCycleActivity(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT a FROM Account a WHERE a.groupId IN :groupIds")
    List<Account> findByGroupIdIn(@Param("groupIds") List<String> groupIds);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = :status AND a.groupId = :groupId")
    List<Account> findByActiveStatusAndGroupId(@Param("status") String status, @Param("groupId") String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = :status AND a.groupId = :groupId")
    Page<Account> findByActiveStatusAndGroupId(@Param("status") String status, @Param("groupId") String groupId, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.openDate <= :date AND (a.expirationDate IS NULL OR a.expirationDate >= :date)")
    List<Account> findAccountsActiveOnDate(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a ORDER BY a.accountId ASC")
    List<Account> findAllOrderedByAccountId();
    
    @Query("SELECT a FROM Account a ORDER BY a.accountId ASC")
    Page<Account> findAllOrderedByAccountId(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.accountId > :lastAccountId ORDER BY a.accountId ASC")
    List<Account> findNextAccountsSequentially(@Param("lastAccountId") Long lastAccountId, Pageable pageable);
    
    @Query("SELECT DISTINCT a.groupId FROM Account a WHERE a.groupId IS NOT NULL")
    List<String> findDistinctGroupIds();
    
    @Query("SELECT a FROM Account a WHERE a.accountStatus = :accountStatus")
    List<Account> findByAccountStatus(@Param("accountStatus") String accountStatus);
    
    @Query("SELECT a FROM Account a WHERE a.accountStatus = :accountStatus")
    Page<Account> findByAccountStatus(@Param("accountStatus") String accountStatus, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.xrefAcctId = :xrefAcctId")
    Optional<Account> findByXrefAcctId(@Param("xrefAcctId") Long xrefAcctId);
    
    boolean existsByXrefAcctId(Long xrefAcctId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.accountId = :accountId AND a.currentBalance > 0")
    boolean hasPositiveBalance(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.currentBalance > 0")
    Optional<Account> findAccountWithPositiveBalance(@Param("accountId") Long accountId);
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCurrentBalanceForActiveAccounts();
    
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCreditLimitForActiveAccounts();
    
    @Query("SELECT AVG(a.currentBalance) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal averageCurrentBalanceForActiveAccounts();
}
