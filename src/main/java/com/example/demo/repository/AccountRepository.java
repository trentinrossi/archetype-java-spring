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
    
    List<Account> findByActiveStatus(String activeStatus);
    
    Page<Account> findByActiveStatus(String activeStatus, Pageable pageable);
    
    List<Account> findByGroupId(String groupId);
    
    Page<Account> findByGroupId(String groupId, Pageable pageable);
    
    List<Account> findByAccountStatus(String accountStatus);
    
    Page<Account> findByAccountStatus(String accountStatus, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.activeStatus = 'Y'")
    Optional<Account> findActiveAccountByAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.openDate >= :startDate AND a.openDate <= :endDate")
    List<Account> findByOpenDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate >= :startDate AND a.expirationDate <= :endDate")
    List<Account> findByExpirationDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate < :date")
    List<Account> findExpiredAccounts(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate >= :date")
    List<Account> findNonExpiredAccounts(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.openDate <= :date")
    List<Account> findAccountsOpenedBefore(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.openDate > :date")
    List<Account> findAccountsOpenedAfter(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate >= :startDate AND a.reissueDate <= :endDate")
    List<Account> findByReissueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :minBalance")
    List<Account> findByCurrentBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance < :maxBalance")
    List<Account> findByCurrentBalanceLessThan(@Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance >= :minBalance AND a.currentBalance <= :maxBalance")
    List<Account> findByCurrentBalanceBetween(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit > :minLimit")
    List<Account> findByCreditLimitGreaterThan(@Param("minLimit") BigDecimal minLimit);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit >= :minLimit AND a.creditLimit <= :maxLimit")
    List<Account> findByCreditLimitBetween(@Param("minLimit") BigDecimal minLimit, @Param("maxLimit") BigDecimal maxLimit);
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit > :minLimit")
    List<Account> findByCashCreditLimitGreaterThan(@Param("minLimit") BigDecimal minLimit);
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit >= :minLimit AND a.cashCreditLimit <= :maxLimit")
    List<Account> findByCashCreditLimitBetween(@Param("minLimit") BigDecimal minLimit, @Param("maxLimit") BigDecimal maxLimit);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit > :amount")
    List<Account> findByCurrentCycleCreditGreaterThan(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleDebit > :amount")
    List<Account> findByCurrentCycleDebitGreaterThan(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE (a.currentCycleCredit > 0 OR a.currentCycleDebit > 0) AND a.activeStatus = 'Y'")
    List<Account> findActiveAccountsWithCycleActivity();
    
    @Query("SELECT a FROM Account a WHERE a.groupId = :groupId AND a.activeStatus = 'Y'")
    List<Account> findActiveAccountsByGroupId(@Param("groupId") String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = 'Y' ORDER BY a.accountId")
    List<Account> findAllActiveAccountsOrderedById();
    
    @Query("SELECT a FROM Account a ORDER BY a.accountId")
    List<Account> findAllAccountsOrderedById();
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.activeStatus = :activeStatus")
    long countByActiveStatus(@Param("activeStatus") String activeStatus);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.accountStatus = :accountStatus")
    long countByAccountStatus(@Param("accountStatus") String accountStatus);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.groupId = :groupId")
    long countByGroupId(@Param("groupId") String groupId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.expirationDate < :date")
    long countExpiredAccounts(@Param("date") LocalDate date);
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCurrentBalanceForActiveAccounts();
    
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCreditLimitForActiveAccounts();
    
    @Query("SELECT SUM(a.currentCycleCredit) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCurrentCycleCreditForActiveAccounts();
    
    @Query("SELECT SUM(a.currentCycleDebit) FROM Account a WHERE a.activeStatus = 'Y'")
    BigDecimal sumCurrentCycleDebitForActiveAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    List<Account> findAccountsOverCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance < 0")
    List<Account> findAccountsWithNegativeBalance();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance >= 0")
    List<Account> findAccountsWithPositiveOrZeroBalance();
    
    @Query("SELECT DISTINCT a.groupId FROM Account a WHERE a.groupId IS NOT NULL")
    List<String> findDistinctGroupIds();
    
    @Query("SELECT a FROM Account a WHERE a.groupId IS NULL")
    List<Account> findAccountsWithoutGroupId();
    
    @Query("SELECT a FROM Account a WHERE a.groupId IS NOT NULL")
    List<Account> findAccountsWithGroupId();
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate IS NULL")
    List<Account> findAccountsWithoutReissueDate();
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate IS NOT NULL")
    List<Account> findAccountsWithReissueDate();
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT a FROM Account a WHERE a.groupId IN :groupIds")
    List<Account> findByGroupIdIn(@Param("groupIds") List<String> groupIds);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = 'Y' AND a.currentCycleCredit > 0")
    List<Account> findActiveAccountsWithCycleCredit();
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = 'Y' AND a.currentCycleDebit > 0")
    List<Account> findActiveAccountsWithCycleDebit();
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = 'Y' AND (a.currentCycleCredit <> 0 OR a.currentCycleDebit <> 0)")
    List<Account> findActiveAccountsForInterestCalculation();
    
    @Query("SELECT a FROM Account a WHERE a.openDate = :openDate")
    List<Account> findByOpenDate(@Param("openDate") LocalDate openDate);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate = :expirationDate")
    List<Account> findByExpirationDate(@Param("expirationDate") LocalDate expirationDate);
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate = :reissueDate")
    List<Account> findByReissueDate(@Param("reissueDate") LocalDate reissueDate);
}
