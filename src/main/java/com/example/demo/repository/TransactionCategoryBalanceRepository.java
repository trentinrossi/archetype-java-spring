package com.example.demo.repository;

import com.example.demo.entity.TransactionCategoryBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionCategoryBalanceRepository extends JpaRepository<TransactionCategoryBalance, Long> {
    
    List<TransactionCategoryBalance> findByTrancatAcctId(Long trancatAcctId);
    
    List<TransactionCategoryBalance> findByTrancatTypeCd(String trancatTypeCd);
    
    List<TransactionCategoryBalance> findByTrancatCd(String trancatCd);
}
