package com.example.demo.repository;

import com.example.demo.entity.DisclosureGroup;
import com.example.demo.entity.DisclosureGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DisclosureGroupRepository extends JpaRepository<DisclosureGroup, DisclosureGroupId> {
    
    Optional<DisclosureGroup> findByAccountGroupIdAndTransactionTypeCodeAndTransactionCategoryCode(
        String accountGroupId, String transactionTypeCode, String transactionCategoryCode);
}
