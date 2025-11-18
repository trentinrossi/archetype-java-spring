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

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountId(Long accountId);
    
    boolean existsByAccountId(Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.accountId <> 0")
    Optional<Account> findByAccountIdAndNotZero(@Param("accountId") Long accountId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.accountId = :accountId AND a.accountId <> 0")
    boolean existsByValidAccountId(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.accountId <> 0")
    Optional<Account> findByAccountIdWithValidation(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE CAST(a.accountId AS string) LIKE :accountPattern")
    List<Account> findByAccountIdPattern(@Param("accountPattern") String accountPattern);
    
    @Query("SELECT a FROM Account a WHERE a.accountId >= :minAccountId AND a.accountId <= :maxAccountId")
    List<Account> findByAccountIdRange(@Param("minAccountId") Long minAccountId, @Param("maxAccountId") Long maxAccountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.accountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);
    
    Page<Account> findAll(Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.accountId <> 0")
    List<Account> findAllNonZeroAccounts();
    
    @Query("SELECT a FROM Account a WHERE CAST(a.accountId AS string) LIKE %:searchTerm%")
    Page<Account> searchByAccountId(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    List<Account> findByAccountIdNotNull();
    
    @Query("SELECT a FROM Account a WHERE a.accountId IS NOT NULL AND a.accountId <> 0")
    List<Account> findAllValidNonZeroAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.accountId >= 10000000000 AND a.accountId <= 99999999999")
    List<Account> findAllElevenDigitAccounts();
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId AND a.accountId >= 10000000000 AND a.accountId <= 99999999999")
    Optional<Account> findByElevenDigitAccountId(@Param("accountId") Long accountId);
}
