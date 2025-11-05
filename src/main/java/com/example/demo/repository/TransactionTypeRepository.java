package com.example.demo.repository;

import com.example.demo.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, String> {
    
    Optional<TransactionType> findByTypeCode(String typeCode);
    
    boolean existsByTypeCode(String typeCode);
}
