package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.enums.AccountStatus;
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
 * Repository interface for Account entity
 * Provides data access methods for account operations
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);
    
    /**
     * Check if account exists by account number
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Find accounts by customer ID
     */
    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId")
    List<Account> findByCustomerId(@Param("customerId") String customerId);
    
    /**
     * Find accounts by customer ID with pagination
     */
    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId")
    Page<Account> findByCustomerId(@Param("customerId") String customerId, Pageable pageable);
    
    /**
     * Find accounts by status
     */
    Page<Account> findByStatus(AccountStatus status, Pageable pageable);
    
    /**
     * Find accounts by account type
     */
    Page<Account> findByAccountType(String accountType, Pageable pageable);
    
    /**
     * Find delinquent accounts
     */
    @Query("SELECT a FROM Account a WHERE a.daysDelinquent > 0")
    Page<Account> findDelinquentAccounts(Pageable pageable);
    
    /**
     * Find accounts with fraud alerts
     */
    Page<Account> findByFraudAlertTrue(Pageable pageable);
    
    /**
     * Find accounts with temporary holds
     */
    Page<Account> findByTemporaryHoldTrue(Pageable pageable);
    
    /**
     * Find over-limit accounts
     */
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    Page<Account> findOverLimitAccounts(Pageable pageable);
    
    /**
     * Find accounts with balance greater than amount
     */
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :amount")
    Page<Account> findByCurrentBalanceGreaterThan(@Param("amount") BigDecimal amount, Pageable pageable);
    
    /**
     * Find accounts with credit limit greater than amount
     */
    @Query("SELECT a FROM Account a WHERE a.creditLimit >= :amount")
    Page<Account> findByCreditLimitGreaterThanEqual(@Param("amount") BigDecimal amount, Pageable pageable);
    
    /**
     * Find accounts with payment due date before date
     */
    @Query("SELECT a FROM Account a WHERE a.paymentDueDate < :date AND a.minimumPaymentDue > 0")
    List<Account> findPastDueAccounts(@Param("date") LocalDate date);
    
    /**
     * Find accounts with payment due in date range
     */
    @Query("SELECT a FROM Account a WHERE a.paymentDueDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsWithPaymentDueBetween(@Param("startDate") LocalDate startDate, 
                                                     @Param("endDate") LocalDate endDate);
    
    /**
     * Find accounts opened in date range
     */
    @Query("SELECT a FROM Account a WHERE a.accountOpenDate BETWEEN :startDate AND :endDate")
    List<Account> findAccountsOpenedBetween(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    /**
     * Find accounts with autopay enabled
     */
    Page<Account> findByAutopayEnabledTrue(Pageable pageable);
    
    /**
     * Find accounts with paperless statements
     */
    Page<Account> findByPaperlessStatementsTrue(Pageable pageable);
    
    /**
     * Count accounts by status
     */
    long countByStatus(AccountStatus status);
    
    /**
     * Count delinquent accounts
     */
    @Query("SELECT COUNT(a) FROM Account a WHERE a.daysDelinquent > 0")
    long countDelinquentAccounts();
    
    /**
     * Count over-limit accounts
     */
    @Query("SELECT COUNT(a) FROM Account a WHERE a.currentBalance > a.creditLimit")
    long countOverLimitAccounts();
    
    /**
     * Calculate total balance across all accounts
     */
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.status = :status")
    BigDecimal sumBalanceByStatus(@Param("status") AccountStatus status);
    
    /**
     * Calculate total credit limit across all accounts
     */
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.status = :status")
    BigDecimal sumCreditLimitByStatus(@Param("status") AccountStatus status);
    
    /**
     * Find accounts with high credit utilization
     */
    @Query("SELECT a FROM Account a WHERE (a.currentBalance / a.creditLimit) > :threshold")
    Page<Account> findHighUtilizationAccounts(@Param("threshold") BigDecimal threshold, Pageable pageable);
    
    /**
     * Find accounts by customer and status
     */
    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId AND a.status = :status")
    List<Account> findByCustomerIdAndStatus(@Param("customerId") String customerId, 
                                            @Param("status") AccountStatus status);
}
