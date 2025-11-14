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
    
    Optional<Account> findByAcctId(String acctId);
    
    boolean existsByAcctId(String acctId);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > :balance")
    List<Account> findAccountsWithPositiveBalance(@Param("balance") BigDecimal balance);
    
    List<Account> findByAcctCurrBalGreaterThan(BigDecimal balance);
    
    Page<Account> findByAcctCurrBalGreaterThan(BigDecimal balance, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > 0")
    List<Account> findAllAccountsWithPositiveBalance();
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > 0")
    Page<Account> findAllAccountsWithPositiveBalance(Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.acctCurrBal > 0")
    long countAccountsWithPositiveBalance();
    
    @Query("SELECT a FROM Account a WHERE a.acctId = :acctId AND a.acctCurrBal > 0")
    Optional<Account> findByAcctIdWithPositiveBalance(@Param("acctId") String acctId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.acctId = :acctId AND a.acctCurrBal > 0")
    boolean existsByAcctIdAndPositiveBalance(@Param("acctId") String acctId);
    
    long countByAcctCurrBalGreaterThan(BigDecimal balance);
    
    @Query("SELECT SUM(a.acctCurrBal) FROM Account a WHERE a.acctCurrBal > 0")
    BigDecimal sumAllPositiveBalances();
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal BETWEEN :minBalance AND :maxBalance")
    List<Account> findByBalanceRange(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal BETWEEN :minBalance AND :maxBalance")
    Page<Account> findByBalanceRange(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance, Pageable pageable);
}
