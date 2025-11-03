package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    List<Transaction> findByCardNumber(String cardNumber);
    
    List<Transaction> findByAccountId(String accountId);
    
    List<Transaction> findByCardNumberOrderByOriginalTimestampDesc(String cardNumber);
    
    List<Transaction> findByAccountIdOrderByOriginalTimestampDesc(String accountId);
}
