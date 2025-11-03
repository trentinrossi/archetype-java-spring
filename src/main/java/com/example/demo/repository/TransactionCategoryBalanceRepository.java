package com.example.demo.repository;

import com.example.demo.entity.TransactionCategoryBalance;
import com.example.demo.entity.TransactionCategoryBalanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionCategoryBalanceRepository extends JpaRepository<TransactionCategoryBalance, TransactionCategoryBalanceId> {
    
    List<TransactionCategoryBalance> findByAccountId(String accountId);
}
