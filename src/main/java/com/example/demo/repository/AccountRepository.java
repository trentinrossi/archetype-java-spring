package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountId(Long accountId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    Optional<Account> findByAccountIdForUpdate(@Param("accountId") Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    List<Account> findByCustomerId(Long customerId);
    
    Page<Account> findByCustomerId(Long customerId, Pageable pageable);
    
    Optional<Account> findByAccountIdAndCustomerId(Long accountId, Long customerId);
    
    List<Account> findByActiveStatus(String activeStatus);
    
    Page<Account> findByActiveStatus(String activeStatus, Pageable pageable);
    
    long countByActiveStatus(String activeStatus);
    
    List<Account> findByAccountStatus(String accountStatus);
    
    Page<Account> findByAccountStatus(String accountStatus, Pageable pageable);
    
    long countByAccountStatus(String accountStatus);
    
    List<Account> findByActiveStatusAndAccountStatus(String activeStatus, String accountStatus);
    
    Page<Account> findByActiveStatusAndAccountStatus(String activeStatus, String accountStatus, Pageable pageable);
    
    List<Account> findByGroupId(String groupId);
    
    Page<Account> findByGroupId(String groupId, Pageable pageable);
    
    long countByGroupId(String groupId);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance >= :minBalance AND a.currentBalance <= :maxBalance")
    List<Account> findByCurrentBalanceBetween(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance >= :minBalance AND a.currentBalance <= :maxBalance")
    Page<Account> findByCurrentBalanceBetween(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :balance")
    List<Account> findByCurrentBalanceGreaterThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance < :balance")
    List<Account> findByCurrentBalanceLessThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit >= :minLimit AND a.creditLimit <= :maxLimit")
    List<Account> findByCreditLimitBetween(@Param("minLimit") BigDecimal minLimit, @Param("maxLimit") BigDecimal maxLimit);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit > :limit")
    List<Account> findByCreditLimitGreaterThan(@Param("limit") BigDecimal limit);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit < :limit")
    List<Account> findByCreditLimitLessThan(@Param("limit") BigDecimal limit);
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit >= :minLimit AND a.cashCreditLimit <= :maxLimit")
    List<Account> findByCashCreditLimitBetween(@Param("minLimit") BigDecimal minLimit, @Param("maxLimit") BigDecimal maxLimit);
    
    @Query("SELECT a FROM Account a WHERE a.cashCreditLimit > :limit")
    List<Account> findByCashCreditLimitGreaterThan(@Param("limit") BigDecimal limit);
    
    @Query("SELECT a FROM Account a WHERE a.openDate >= :startDate AND a.openDate <= :endDate")
    List<Account> findByOpenDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.openDate >= :startDate AND a.openDate <= :endDate")
    Page<Account> findByOpenDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.openDate >= :date")
    List<Account> findByOpenDateAfter(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.openDate <= :date")
    List<Account> findByOpenDateBefore(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate >= :startDate AND a.expirationDate <= :endDate")
    List<Account> findByExpirationDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate >= :startDate AND a.expirationDate <= :endDate")
    Page<Account> findByExpirationDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate <= :date")
    List<Account> findByExpirationDateBefore(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate >= :date")
    List<Account> findByExpirationDateAfter(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.expirationDate <= :date AND a.activeStatus = 'Y'")
    List<Account> findActiveAccountsExpiringBefore(@Param("date") LocalDate date);
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate >= :startDate AND a.reissueDate <= :endDate")
    List<Account> findByReissueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.reissueDate >= :startDate AND a.reissueDate <= :endDate")
    Page<Account> findByReissueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit >= :minCredit AND a.currentCycleCredit <= :maxCredit")
    List<Account> findByCurrentCycleCreditBetween(@Param("minCredit") BigDecimal minCredit, @Param("maxCredit") BigDecimal maxCredit);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleDebit >= :minDebit AND a.currentCycleDebit <= :maxDebit")
    List<Account> findByCurrentCycleDebitBetween(@Param("minDebit") BigDecimal minDebit, @Param("maxDebit") BigDecimal maxDebit);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleCredit > :amount")
    List<Account> findByCurrentCycleCreditGreaterThan(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.currentCycleDebit > :amount")
    List<Account> findByCurrentCycleDebitGreaterThan(@Param("amount") BigDecimal amount);
    
    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.activeStatus = :activeStatus")
    List<Account> findByCustomerIdAndActiveStatus(@Param("customerId") Long customerId, @Param("activeStatus") String activeStatus);
    
    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.accountStatus = :accountStatus")
    List<Account> findByCustomerIdAndAccountStatus(@Param("customerId") Long customerId, @Param("accountStatus") String accountStatus);
    
    @Query("SELECT a FROM Account a WHERE a.groupId = :groupId AND a.activeStatus = :activeStatus")
    List<Account> findByGroupIdAndActiveStatus(@Param("groupId") String groupId, @Param("activeStatus") String activeStatus);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.customerId = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.customerId = :customerId AND a.activeStatus = 'Y'")
    long countActiveAccountsByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.customerId = :customerId")
    BigDecimal sumCurrentBalanceByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.customerId = :customerId")
    BigDecimal sumCreditLimitByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    List<Account> findAccountsOverCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    Page<Account> findAccountsOverCreditLimit(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE (a.creditLimit - a.currentBalance) < :threshold AND a.activeStatus = 'Y'")
    List<Account> findAccountsNearCreditLimit(@Param("threshold") BigDecimal threshold);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = 'Y' AND a.accountStatus = :accountStatus")
    List<Account> findActiveAccountsByAccountStatus(@Param("accountStatus") String accountStatus);
    
    @Query("SELECT a FROM Account a WHERE a.groupId IS NULL")
    List<Account> findAccountsWithoutGroup();
    
    @Query("SELECT a FROM Account a WHERE a.groupId IS NOT NULL")
    List<Account> findAccountsWithGroup();
    
    @Query("SELECT DISTINCT a.groupId FROM Account a WHERE a.groupId IS NOT NULL")
    List<String> findAllDistinctGroupIds();
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT a FROM Account a WHERE a.customerId IN :customerIds")
    List<Account> findByCustomerIdIn(@Param("customerIds") List<Long> customerIds);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = :activeStatus AND a.openDate >= :sinceDate")
    List<Account> findRecentAccountsByActiveStatus(@Param("activeStatus") String activeStatus, @Param("sinceDate") LocalDate sinceDate);
    
    @Query("SELECT a FROM Account a WHERE a.activeStatus = :activeStatus AND a.openDate >= :sinceDate")
    Page<Account> findRecentAccountsByActiveStatus(@Param("activeStatus") String activeStatus, @Param("sinceDate") LocalDate sinceDate, Pageable pageable);
}
