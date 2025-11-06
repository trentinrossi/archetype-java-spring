package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    Optional<Account> findByAccountIdForRandomAccess(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE LENGTH(CAST(a.accountId AS string)) = 11")
    Page<Account> findAllValidAccounts(Pageable pageable);
    
    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.accountId = :accountId AND LENGTH(CAST(a.accountId AS string)) = 11")
    boolean existsByAccountIdWithValidFormat(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE CAST(a.accountId AS string) LIKE :accountIdPattern")
    List<Account> findByAccountIdPattern(@Param("accountIdPattern") String accountIdPattern);
    
    @Query("SELECT a FROM Account a WHERE a.accountData IS NOT NULL AND LENGTH(a.accountData) = 289")
    Page<Account> findAllWithCompleteAccountData(Pageable pageable);
    
    long countByAccountIdNotNull();
    
    List<Account> findByCustomer_CustomerId(Long customerId);
}
