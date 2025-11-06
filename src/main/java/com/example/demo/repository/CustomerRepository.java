package com.example.demo.repository;

import com.example.demo.entity.Customer;
import com.example.demo.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity
 * Provides data access methods for customer operations
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    
    /**
     * Find customer by email address
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Check if customer exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if customer exists by SSN
     */
    boolean existsBySsn(String ssn);
    
    /**
     * Find customers by status
     */
    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);
    
    /**
     * Find VIP customers
     */
    Page<Customer> findByVipStatusTrue(Pageable pageable);
    
    /**
     * Find customers by name (case-insensitive search)
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find customers by city and state
     */
    Page<Customer> findByCityAndState(String city, String state, Pageable pageable);
    
    /**
     * Find customers by state
     */
    Page<Customer> findByState(String state, Pageable pageable);
    
    /**
     * Find customers by ZIP code
     */
    List<Customer> findByZipCode(String zipCode);
    
    /**
     * Find customers created since a specific date
     */
    @Query("SELECT c FROM Customer c WHERE c.createdAt >= :since")
    List<Customer> findRecentCustomers(@Param("since") LocalDate since);
    
    /**
     * Find customers with credit score above threshold
     */
    @Query("SELECT c FROM Customer c WHERE c.creditScore >= :minScore")
    Page<Customer> findByMinimumCreditScore(@Param("minScore") Integer minScore, Pageable pageable);
    
    /**
     * Find customers with credit score in range
     */
    @Query("SELECT c FROM Customer c WHERE c.creditScore BETWEEN :minScore AND :maxScore")
    Page<Customer> findByCreditScoreRange(@Param("minScore") Integer minScore, 
                                          @Param("maxScore") Integer maxScore, 
                                          Pageable pageable);
    
    /**
     * Count customers by status
     */
    long countByStatus(CustomerStatus status);
    
    /**
     * Count VIP customers
     */
    long countByVipStatusTrue();
    
    /**
     * Find customers by phone number (any phone field)
     */
    @Query("SELECT c FROM Customer c WHERE c.homePhone = :phone OR c.workPhone = :phone OR c.mobilePhone = :phone")
    Optional<Customer> findByPhoneNumber(@Param("phone") String phone);
    
    /**
     * Find customers by date of birth
     */
    List<Customer> findByDateOfBirth(LocalDate dateOfBirth);
    
    /**
     * Find customers by customer since date range
     */
    @Query("SELECT c FROM Customer c WHERE c.customerSince BETWEEN :startDate AND :endDate")
    List<Customer> findByCustomerSinceDateRange(@Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);
    
    /**
     * Find customers with accounts
     */
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.accounts a WHERE a IS NOT NULL")
    Page<Customer> findCustomersWithAccounts(Pageable pageable);
    
    /**
     * Find customers without accounts
     */
    @Query("SELECT c FROM Customer c WHERE c.accounts IS EMPTY")
    Page<Customer> findCustomersWithoutAccounts(Pageable pageable);
}
