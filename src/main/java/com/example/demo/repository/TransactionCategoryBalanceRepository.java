package com.example.demo.repository;

import com.example.demo.entity.TransactionCategoryBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionCategoryBalanceRepository extends JpaRepository<TransactionCategoryBalance, TransactionCategoryBalance.TransactionCategoryBalanceId> {
    
    Optional<TransactionCategoryBalance> findByAccountIdAndTransactionTypeCodeAndTransactionCategoryCode(
            String accountId, String transactionTypeCode, Integer transactionCategoryCode);
    
    List<TransactionCategoryBalance> findByAccountId(String accountId);
}
