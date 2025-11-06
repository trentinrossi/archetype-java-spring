package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByCustomerId(Long customerId);
    
    boolean existsByCustomerId(Long customerId);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
    Optional<Customer> findByCustomerIdForRandomAccess(@Param("customerId") Long customerId);
    
    @Query("SELECT c FROM Customer c WHERE LENGTH(CAST(c.customerId AS string)) = 9")
    Page<Customer> findAllValidCustomers(Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId AND LENGTH(CAST(c.customerId AS string)) = 9")
    Optional<Customer> findByCustomerIdWithValidation(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE LENGTH(CAST(c.customerId AS string)) = 9")
    long countValidCustomers();
    
    @Query("SELECT c FROM Customer c WHERE c.customerId IN :customerIds")
    List<Customer> findByCustomerIdIn(@Param("customerIds") List<Long> customerIds);
    
    @Query("SELECT c FROM Customer c ORDER BY c.customerId ASC")
    List<Customer> findAllOrderedByCustomerId();
    
    @Query("SELECT c FROM Customer c WHERE c.createdAt >= :since ORDER BY c.createdAt DESC")
    List<Customer> findRecentCustomers(@Param("since") java.time.LocalDateTime since);
    
    @Query("SELECT c FROM Customer c WHERE LENGTH(c.customerData) = 491")
    Page<Customer> findCustomersWithCompleteData(Pageable pageable);
}
