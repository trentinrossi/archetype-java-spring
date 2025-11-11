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
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    List<Account> findByCustomer_CustomerId(Long customerId);
    
    Page<Account> findByCustomer_CustomerId(Long customerId, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    List<Account> findOverLimitAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance > a.creditLimit")
    Page<Account> findOverLimitAccounts(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance >= :minBalance AND a.currentBalance <= :maxBalance")
    List<Account> findByBalanceRange(@Param("minBalance") BigDecimal minBalance, 
                                      @Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.creditLimit >= :minLimit")
    List<Account> findByCreditLimitGreaterThanEqual(@Param("minLimit") BigDecimal minLimit);
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.currentBalance > a.creditLimit")
    long countOverLimitAccounts();
    
    long countByCustomer_CustomerId(Long customerId);
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a WHERE a.customer.customerId = :customerId")
    BigDecimal sumBalancesByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT SUM(a.creditLimit) FROM Account a WHERE a.customer.customerId = :customerId")
    BigDecimal sumCreditLimitsByCustomerId(@Param("customerId") Long customerId);
}
