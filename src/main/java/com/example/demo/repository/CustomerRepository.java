package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByCustomerId(String customerId);
    
    boolean existsByCustomerId(String customerId);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
    Optional<Customer> findByCustomerIdForRandomAccess(@Param("customerId") String customerId);
    
    @Query("SELECT c FROM Customer c WHERE LENGTH(c.customerId) = 9 AND c.customerId LIKE :pattern")
    List<Customer> findByCustomerIdPattern(@Param("pattern") String pattern);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE LENGTH(c.customerId) = 9")
    long countValidCustomers();
    
    @Query("SELECT c FROM Customer c WHERE LENGTH(c.customerId) <> 9")
    List<Customer> findInvalidCustomers();
}