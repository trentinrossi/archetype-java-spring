package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAcctId(Long acctId);
    
    boolean existsByAcctId(Long acctId);
    
    @Query("SELECT COUNT(c) FROM Card c WHERE c.account.acctId = :acctId")
    long countCardsByAccountId(@Param("acctId") Long acctId);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.acctId = :acctId")
    long countTransactionsByAccountId(@Param("acctId") Long acctId);
}
