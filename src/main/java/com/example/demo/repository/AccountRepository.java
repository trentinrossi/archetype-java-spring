package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    Optional<Account> findByAccountId(String accountId);
    
    List<Account> findByActiveStatus(String activeStatus);
    
    List<Account> findByGroupId(String groupId);
    
    boolean existsByAccountId(String accountId);
}
