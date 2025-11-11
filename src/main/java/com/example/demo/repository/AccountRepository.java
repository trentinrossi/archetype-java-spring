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
public interface AccountRepository extends JpaRepository<Account, String> {
    
    Optional<Account> findByAcctId(String acctId);
    
    boolean existsByAcctId(String acctId);
    
    List<Account> findByAcctActiveStatus(String acctActiveStatus);
    
    Page<Account> findByAcctActiveStatus(String acctActiveStatus, Pageable pageable);
    
    List<Account> findByAcctGroupId(String acctGroupId);
    
    Page<Account> findByAcctGroupId(String acctGroupId, Pageable pageable);
    
    List<Account> findByAcctOpenDateBetween(LocalDate startDate, LocalDate endDate);
    
    Page<Account> findByAcctOpenDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    List<Account> findByAcctExpirationDateBefore(LocalDate expirationDate);
    
    Page<Account> findByAcctExpirationDateBefore(LocalDate expirationDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctExpirationDate < :currentDate")
    List<Account> findExpiredAccounts(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT a FROM Account a WHERE a.acctExpirationDate < :currentDate")
    Page<Account> findExpiredAccounts(@Param("currentDate") LocalDate currentDate, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus = 'A'")
    List<Account> findActiveAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus = 'A'")
    Page<Account> findActiveAccounts(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus = 'I'")
    List<Account> findInactiveAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.acctActiveStatus = 'I'")
    Page<Account> findInactiveAccounts(Pageable pageable);
    
    long countByAcctActiveStatus(String acctActiveStatus);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctExpirationDate < :currentDate")
    long countExpiredAccounts(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > :balance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > a.acctCreditLimit")
    List<Account> findAccountsOverCreditLimit();
    
    @Query("SELECT a FROM Account a WHERE a.acctOpenDate >= :startDate AND a.acctOpenDate <= :endDate")
    List<Account> findAccountsOpenedInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Account a WHERE a.acctReissueDate IS NOT NULL")
    List<Account> findReissuedAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.acctReissueDate IS NOT NULL")
    Page<Account> findReissuedAccounts(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctGroupId = :groupId AND a.acctActiveStatus = :status")
    List<Account> findByAcctGroupIdAndAcctActiveStatus(@Param("groupId") String groupId, @Param("status") String status);
    
    @Query("SELECT a FROM Account a ORDER BY a.acctId ASC")
    List<Account> findAllOrderedByAcctId();
    
    @Query("SELECT a FROM Account a ORDER BY a.acctId ASC")
    Page<Account> findAllOrderedByAcctId(Pageable pageable);
}
