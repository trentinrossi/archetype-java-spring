package com.example.demo.repository;

import com.example.demo.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, TransactionCategory.TransactionCategoryId> {
    
    Optional<TransactionCategory> findByTypeCodeAndCategoryCode(String typeCode, String categoryCode);
    
    boolean existsByTypeCodeAndCategoryCode(String typeCode, String categoryCode);
}
