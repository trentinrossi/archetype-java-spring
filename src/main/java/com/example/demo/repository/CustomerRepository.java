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
    
    Optional<Customer> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.customerId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> searchCustomers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId IN :customerIds")
    List<Customer> findByCustomerIdIn(@Param("customerIds") List<String> customerIds);
    
    @Query("SELECT c FROM Customer c WHERE SIZE(c.accounts) > 0")
    List<Customer> findCustomersWithAccounts();
    
    @Query("SELECT c FROM Customer c WHERE SIZE(c.creditCards) > 0")
    List<Customer> findCustomersWithCreditCards();
    
    @Query("SELECT c FROM Customer c WHERE SIZE(c.accounts) = 0 AND SIZE(c.creditCards) = 0")
    List<Customer> findCustomersWithoutAccountsOrCards();
    
    long countByCustomerId(String customerId);
}
