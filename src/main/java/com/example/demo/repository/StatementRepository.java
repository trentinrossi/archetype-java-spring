package com.example.demo.repository;

import com.example.demo.entity.Statement;
import com.example.demo.enums.StatementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
    
    List<Statement> findByAccountId(Long accountId);
    
    Page<Statement> findByAccountId(Long accountId, Pageable pageable);
    
    List<Statement> findByCustomer_CustomerId(Long customerId);
    
    Page<Statement> findByCustomer_CustomerId(Long customerId, Pageable pageable);
    
    List<Statement> findByAccount_AccountId(Long accountId);
    
    Page<Statement> findByAccount_AccountId(Long accountId, Pageable pageable);
    
    List<Statement> findByStatus(StatementStatus status);
    
    Page<Statement> findByStatus(StatementStatus status, Pageable pageable);
    
    @Query("SELECT s FROM Statement s WHERE s.accountId = :accountId AND s.status = :status")
    List<Statement> findByAccountIdAndStatus(@Param("accountId") Long accountId, 
                                              @Param("status") StatementStatus status);
    
    @Query("SELECT s FROM Statement s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    List<Statement> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Statement s WHERE s.statementPeriodStart >= :startDate AND s.statementPeriodEnd <= :endDate")
    List<Statement> findByStatementPeriod(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT s FROM Statement s WHERE s.accountId = :accountId ORDER BY s.createdAt DESC")
    List<Statement> findLatestStatementsByAccountId(@Param("accountId") Long accountId, Pageable pageable);
    
    @Query("SELECT s FROM Statement s WHERE s.customer.customerId = :customerId ORDER BY s.createdAt DESC")
    List<Statement> findLatestStatementsByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
    
    long countByAccountId(Long accountId);
    
    long countByCustomer_CustomerId(Long customerId);
    
    long countByStatus(StatementStatus status);
    
    @Query("SELECT COUNT(s) FROM Statement s WHERE s.accountId = :accountId AND s.status = :status")
    long countByAccountIdAndStatus(@Param("accountId") Long accountId, 
                                    @Param("status") StatementStatus status);
}
