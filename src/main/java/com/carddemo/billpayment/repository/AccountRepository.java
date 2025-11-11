package com.carddemo.billpayment.repository;

import com.carddemo.billpayment.entity.Account;
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
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > :minBalance")
    List<Account> findAccountsWithPositiveBalance(@Param("minBalance") BigDecimal minBalance);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > 0")
    List<Account> findAccountsWithPositiveBalance();
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal > 0")
    Page<Account> findAccountsWithPositiveBalancePaged(Pageable pageable);
    
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.transactions WHERE a.acctId = :acctId")
    Optional<Account> findByAcctIdWithTransactions(@Param("acctId") String acctId);
    
    @Query("SELECT COUNT(t) FROM Account a JOIN a.transactions t WHERE a.acctId = :acctId")
    long countTransactionsByAcctId(@Param("acctId") String acctId);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal = :balance")
    List<Account> findByAcctCurrBal(@Param("balance") BigDecimal balance);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal >= :minBalance AND a.acctCurrBal <= :maxBalance")
    List<Account> findByBalanceRange(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
    
    @Query("SELECT a FROM Account a WHERE a.acctCurrBal >= :minBalance AND a.acctCurrBal <= :maxBalance")
    Page<Account> findByBalanceRangePaged(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance, Pageable pageable);
    
    long countByAcctCurrBalGreaterThan(BigDecimal balance);
    
    long countByAcctCurrBalLessThanEqual(BigDecimal balance);
    
    @Query("SELECT COALESCE(SUM(a.acctCurrBal), 0) FROM Account a")
    BigDecimal sumAllAccountBalances();
    
    @Query("SELECT COALESCE(SUM(a.acctCurrBal), 0) FROM Account a WHERE a.acctCurrBal > 0")
    BigDecimal sumPositiveAccountBalances();
    
    @Query("SELECT a FROM Account a WHERE LOWER(a.acctId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Account> findByAcctIdContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT a FROM Account a ORDER BY a.acctCurrBal DESC")
    Page<Account> findAllOrderByBalanceDesc(Pageable pageable);
    
    @Query("SELECT a FROM Account a ORDER BY a.acctCurrBal ASC")
    Page<Account> findAllOrderByBalanceAsc(Pageable pageable);
}
