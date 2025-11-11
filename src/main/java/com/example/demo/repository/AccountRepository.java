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
    
    /**
     * BR001: Account Validation - Find account by account ID
     */
    Optional<Account> findByAccountId(String accountId);
    
    /**
     * BR001: Account Validation - Check if account exists
     */
    boolean existsByAccountId(String accountId);
    
    /**
     * BR002: Balance Check - Find accounts with balance greater than specified amount
     */
    List<Account> findByCurrentBalanceGreaterThan(BigDecimal balance);
    
    List<Account> findByCurrentBalanceGreaterThanEqual(BigDecimal balance);
    
    List<Account> findByCurrentBalanceLessThanEqual(BigDecimal balance);
    
    /**
     * BR002: Balance Check - Find accounts with positive balance (paginated)
     */
    @Query("SELECT a FROM Account a WHERE a.currentBalance > :minBalance")
    Page<Account> findAccountsWithPositiveBalance(@Param("minBalance") BigDecimal minBalance, Pageable pageable);
    
    /**
     * BR001 & BR002: Find account by ID with positive balance
     */
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.currentBalance > :minBalance")
    Optional<Account> findByAccountIdAndBalanceGreaterThan(@Param("accountId") String accountId, @Param("minBalance") BigDecimal minBalance);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.currentBalance > :balance")
    long countAccountsWithBalanceGreaterThan(@Param("balance") BigDecimal balance);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.currentBalance = :balance")
    long countAccountsWithBalance(@Param("balance") BigDecimal balance);
    
    @Query("SELECT SUM(a.currentBalance) FROM Account a")
    BigDecimal sumAllAccountBalances();
    
    @Query("SELECT a FROM Account a WHERE a.currentBalance BETWEEN :minBalance AND :maxBalance")
    List<Account> findAccountsByBalanceRange(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.accountId LIKE CONCAT(:prefix, '%')")
    List<Account> findAccountsByIdPrefix(@Param("prefix") String prefix);
}
