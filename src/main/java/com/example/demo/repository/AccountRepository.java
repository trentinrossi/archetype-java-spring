package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Account entity
 * Business Rule: Account serves as the primary container for credit card management
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    /**
     * Find account by account ID
     */
    Optional<Account> findByAccountId(String accountId);
    
    /**
     * Check if account exists by account ID
     */
    boolean existsByAccountId(String accountId);
    
    /**
     * Find accounts with credit cards
     */
    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.creditCards WHERE a.accountId IN :accountIds")
    List<Account> findAccountsWithCreditCards(@Param("accountIds") List<String> accountIds);
    
    /**
     * Find accounts accessible by a specific user
     */
    @Query("SELECT a FROM Account a JOIN a.users u WHERE u.userId = :userId")
    List<Account> findAccountsByUserId(@Param("userId") String userId);
    
    /**
     * Count credit cards for an account
     */
    @Query("SELECT COUNT(c) FROM CreditCard c WHERE c.account.accountId = :accountId")
    Long countCreditCardsByAccountId(@Param("accountId") String accountId);
    
    /**
     * Find accounts with active credit cards
     */
    @Query("SELECT DISTINCT a FROM Account a JOIN a.creditCards c WHERE c.cardStatus = 'ACTIVE'")
    Page<Account> findAccountsWithActiveCards(Pageable pageable);
    
    /**
     * Find accounts by ID pattern (for search functionality)
     */
    @Query("SELECT a FROM Account a WHERE a.accountId LIKE CONCAT('%', :pattern, '%')")
    Page<Account> findByAccountIdContaining(@Param("pattern") String pattern, Pageable pageable);
}
