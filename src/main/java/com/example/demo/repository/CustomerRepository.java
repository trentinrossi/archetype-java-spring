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
    
    Optional<Customer> findByCustomerId(Long customerId);
    
    boolean existsByCustomerId(Long customerId);
    
    List<Customer> findByFicoCreditScoreGreaterThanEqual(Integer ficoCreditScore);
    
    List<Customer> findByStateCode(String stateCode);
    
    Page<Customer> findByStateCode(String stateCode, Pageable pageable);
    
    List<Customer> findByCountryCode(String countryCode);
    
    Page<Customer> findByCountryCode(String countryCode, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.stateCode = :stateCode AND c.countryCode = :countryCode")
    List<Customer> findByStateCodeAndCountryCode(@Param("stateCode") String stateCode, 
                                                   @Param("countryCode") String countryCode);
    
    @Query("SELECT c FROM Customer c WHERE c.ficoCreditScore BETWEEN :minScore AND :maxScore")
    List<Customer> findByFicoCreditScoreRange(@Param("minScore") Integer minScore, 
                                                @Param("maxScore") Integer maxScore);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.zipCode = :zipCode")
    List<Customer> findByZipCode(@Param("zipCode") String zipCode);
    
    @Query("SELECT c FROM Customer c WHERE c.customerId IN :customerIds")
    List<Customer> findByCustomerIdIn(@Param("customerIds") List<Long> customerIds);
    
    long countByStateCode(String stateCode);
    
    long countByCountryCode(String countryCode);
    
    long countByFicoCreditScoreGreaterThanEqual(Integer ficoCreditScore);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.ficoCreditScore BETWEEN :minScore AND :maxScore")
    long countByFicoCreditScoreRange(@Param("minScore") Integer minScore, 
                                      @Param("maxScore") Integer maxScore);
}
