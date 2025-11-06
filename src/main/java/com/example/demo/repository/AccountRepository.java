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
    
    @Query("SELECT a FROM Account a WHERE a.accountId = :accountId")
    Optional<Account> findByAccountIdForRandomAccess(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE LENGTH(CAST(a.accountId AS string)) = 11")
    List<Account> findAllWithValidAccountIdLength();
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.accountId = :accountId AND LENGTH(CAST(a.accountId AS string)) = 11")
    boolean existsByAccountIdWithValidLength(@Param("accountId") Long accountId);
    
    @Query("SELECT a FROM Account a WHERE a.accountId IN :accountIds")
    List<Account> findAllByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    Page<Account> findAll(Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE LENGTH(CAST(a.accountId AS string)) = 11")
    long countValidAccounts();
    
    @Query("SELECT a FROM Account a WHERE LENGTH(a.accountData) = 289")
    List<Account> findAllWithValidAccountDataLength();

}