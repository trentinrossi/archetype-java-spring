package com.example.demo.repository;

import com.example.demo.entity.CardCrossReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, Long> {
    
    Optional<CardCrossReference> findByAccountId(Long accountId);
    
    List<CardCrossReference> findByCustomerId(Long customerId);
    
    Optional<CardCrossReference> findByAccountIdAndCustomerId(Long accountId, Long customerId);
    
    boolean existsByAccountId(Long accountId);
    
    boolean existsByCustomerId(Long customerId);
    
    @Query("SELECT COUNT(c) FROM CardCrossReference c WHERE c.customerId = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.accountId IN :accountIds")
    List<CardCrossReference> findByAccountIdIn(@Param("accountIds") List<Long> accountIds);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.customerId IN :customerIds")
    List<CardCrossReference> findByCustomerIdIn(@Param("customerIds") List<Long> customerIds);
}
