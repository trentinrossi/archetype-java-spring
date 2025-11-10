package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Account entity.
 * Provides data access methods for account operations.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    /**
     * Find account by account ID
     * @param accountId the 11-digit account identifier
     * @return Optional containing the account if found
     */
    Optional<Account> findByAccountId(String accountId);
    
    /**
     * Check if account exists by account ID
     * @param accountId the 11-digit account identifier
     * @return true if account exists
     */
    boolean existsByAccountId(String accountId);
    
    /**
     * Find accounts that have credit cards
     * @return list of accounts with at least one credit card
     */
    @Query("SELECT DISTINCT a FROM Account a JOIN a.creditCards c")
    List<Account> findAccountsWithCreditCards();
    
    /**
     * Find accounts with a specific number of credit cards
     * @param count the number of credit cards
     * @return list of accounts
     */
    @Query("SELECT a FROM Account a WHERE SIZE(a.creditCards) = :count")
    List<Account> findAccountsWithCreditCardCount(@Param("count") int count);
    
    /**
     * Find accounts with credit card count greater than specified value
     * @param count the minimum number of credit cards
     * @return list of accounts
     */
    @Query("SELECT a FROM Account a WHERE SIZE(a.creditCards) > :count")
    List<Account> findAccountsWithMoreThanCreditCards(@Param("count") int count);
}
