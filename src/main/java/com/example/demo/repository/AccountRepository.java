package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    
    List<Account> findByActiveStatus(String activeStatus);
    
    List<Account> findByGroupId(String groupId);
}
